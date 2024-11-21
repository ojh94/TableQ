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
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Pageable;

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

            MenuItem menuItem = MenuItem.builder()
                    .name(menuItemRequest.getName())
                    .price(menuItemRequest.getPrice())
                    .description(menuItemRequest.getDescription())
                    .recommendation(menuItemRequest.getRecommendation())
                    .restaurant(restaurantRepository.findById(menuItemRequest.getRestaurantId())
                            .orElseThrow(() -> new IllegalArgumentException("not found")))
                    .build();

            menuItem = baseRepository.save(menuItem);

            String originalFilename = menuItemRequest.getFile().getOriginalFilename();
            String fileExtension = "." + originalFilename.substring(originalFilename.lastIndexOf(".") + 1);

            String fileUrl = uploadFile(menuItemRequest.getFile(), DIRECTORY_NAME, menuItem.getId() + fileExtension);

            menuItem.updateFileUrl(fileUrl);

            MenuItemResponse menuItemResponse = response(menuItem);

            return Header.OK(menuItemResponse);
        } catch (Exception e){
            throw new RuntimeException("MenuItemService의 create 메소드 실패");
        }
    }


    @Override
    public Header<MenuItemResponse> read(Long id) {
        return Header.OK(response(baseRepository.findById(id).orElse(null)));
    }

    @Override
    @Transactional
    public Header<MenuItemResponse> update(Long id, Header<MenuItemRequest> request) {
        try {
            MenuItemRequest menuItemRequest = request.getData();

            MenuItem menuItem = baseRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("not found"));

            updateFile(menuItem, request.getData().getFile(), DIRECTORY_NAME, "" + menuItem.getId());

            menuItem.update(menuItemRequest);

            return Header.OK(response(menuItem));
        } catch (Exception e){
            throw new RuntimeException("MenuItemService의 update 메소드 실패");
        }
    }

    @Transactional
    @Override
    public Header delete(Long id) {
        try {
            MenuItem menuItem = baseRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("MenuItem delete fail"));

            deleteFile(menuItem);

            return Header.OK(response(menuItem));
        } catch (Exception e){
            throw new RuntimeException("MenuItemService의 delete 메소드 실패");
        }
    }

    public Header<List<MenuItemResponse>> readByRestaurantId(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow();

        return Header.OK(
                responseList(
                        ((MenuItemRepository)baseRepository).findByRestaurant(restaurant).orElseThrow()));
    }
}
