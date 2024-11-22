package com.itschool.tableq.service;

import com.itschool.tableq.domain.MenuItem;
import com.itschool.tableq.domain.Restaurant;
import com.itschool.tableq.network.Header;
import com.itschool.tableq.network.Pagination;
import com.itschool.tableq.network.request.MenuItemRequest;
import com.itschool.tableq.network.response.MenuItemResponse;
import com.itschool.tableq.repository.MenuItemRepository;
import com.itschool.tableq.repository.RestaurantRepository;
import com.itschool.tableq.service.base.BaseServiceWithS3;
import com.itschool.tableq.util.FileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MenuItemService extends BaseServiceWithS3<MenuItemRequest, MenuItemResponse, MenuItem> {

    @Autowired
    RestaurantRepository restaurantRepository;

    private static final String DIRECTORY_NAME = "menu";

    @Override
    public Header<List<MenuItemResponse>> getPaginatedList(Pageable pageable) {
        Page<MenuItem> entities = baseRepository.findAll(pageable);

        List<MenuItemResponse> menuItemResponsesList = entities.stream()
                .map(entity -> response(entity))
                .collect(Collectors.toList());

        Pagination pagination = Pagination.builder()
                .totalPages(entities.getTotalPages())
                .totalElements(entities.getTotalElements())
                .currentPage(entities.getNumber())
                .currentElements(entities.getNumberOfElements())
                .build();

        return Header.OK(menuItemResponsesList, pagination);
    }

    @Override
    protected MenuItemResponse response(MenuItem menuItem) {
        return MenuItemResponse.of(menuItem);
    }

    public List<MenuItemResponse> responseList(List<MenuItem> menuList) {
        List<MenuItemResponse> responseList = new ArrayList<>();

        for(MenuItem menuItem : menuList){
            responseList.add(response(menuItem));
        }

        return responseList;
    }

    @Transactional
    @Override
    public Header<MenuItemResponse> create(Header<MenuItemRequest> request) {
        try {
            MenuItemRequest menuItemRequest = request.getData();

            MenuItem entity = MenuItem.builder()
                    .name(menuItemRequest.getName())
                    .price(menuItemRequest.getPrice())
                    .description(menuItemRequest.getDescription())
                    .recommendation(menuItemRequest.getRecommendation())
                    .restaurant(restaurantRepository.findById(menuItemRequest.getRestaurantId())
                            .orElseThrow(() -> new IllegalArgumentException("not found")))
                    .build();

            entity = baseRepository.save(entity);

            String fileUrl = uploadFile(menuItemRequest.getFile(), DIRECTORY_NAME,
                    String.valueOf(entity.getId()) + FileUtil.getFileExtension(request.getData().getFile()));

            entity.updateFileUrl(fileUrl);

            MenuItemResponse menuItemResponse = response(entity);

            return Header.OK(menuItemResponse);
        } catch (Exception e){
            throw new RuntimeException(this.getClass() + "의 create 메소드 실패", e);
        }
    }


    @Override
    public Header<MenuItemResponse> read(Long id) {
        return Header.OK(response(baseRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("not found"))));
    }

    @Override
    @Transactional
    public Header<MenuItemResponse> update(Long id, Header<MenuItemRequest> request) {
        try {
            MenuItemRequest menuItemRequest = request.getData();

            MultipartFile file = menuItemRequest.getFile();

            MenuItem findEntity = baseRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("not found"));

            String existingUrl = findEntity.getFileUrl();

            if (menuItemRequest.isNeedFileChange()) { // 프론트에서 파일 변경이 필요하다 한 경우
                if (file.isEmpty() && existingUrl != null) { // 대체 파일이 없고 기존 url이 있는 경우
                    deleteFile(existingUrl); // 기존 파일 삭제
                    findEntity.updateFileUrl(null); // 파일 URL 삭제
                } else if (!file.isEmpty()) { // 대체 파일이 있는 경우
                    String newUrl = updateFile(existingUrl, file); // 새 파일 업로드
                    findEntity.updateFileUrl(newUrl); // 새 파일 URL 설정
                }     
            }

            findEntity.updateWithoutFileUrl(menuItemRequest);

            return Header.OK(response(findEntity));
        } catch (Exception e){
            throw new RuntimeException(this.getClass() + "의 update 메소드 실패", e);
        }
    }

    @Transactional
    @Override
    public Header delete(Long id) {
        try {
            MenuItem entity = baseRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("not found"));

            if(entity.getFileUrl() != null)
                deleteFile(entity.getFileUrl());

            baseRepository.delete(entity);

            return Header.OK(response(entity));
        } catch (Exception e){
            throw new RuntimeException(this.getClass() + "의 delete 메소드 실패", e);
        }
    }

    public Header<List<MenuItemResponse>> readByRestaurantId(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow();

        return Header.OK(
                responseList(
                        ((MenuItemRepository)baseRepository).findByRestaurant(restaurant).orElseThrow()));
    }
}
