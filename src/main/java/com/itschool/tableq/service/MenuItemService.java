package com.itschool.tableq.service;

import com.itschool.tableq.domain.MenuItem;
import com.itschool.tableq.domain.Restaurant;
import com.itschool.tableq.network.Header;
import com.itschool.tableq.network.request.MenuItemRequest;
import com.itschool.tableq.network.response.MenuItemResponse;
import com.itschool.tableq.repository.MenuItemRepository;
import com.itschool.tableq.repository.RestaurantRepository;
import com.itschool.tableq.service.base.BaseServiceWithS3;
import com.itschool.tableq.service.base.S3Service;
import com.itschool.tableq.util.FileUtil;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class MenuItemService extends BaseServiceWithS3<MenuItemRequest, MenuItemResponse, MenuItem> {

    private final RestaurantRepository restaurantRepository;

    private static final String DIRECTORY_NAME = "menu";

    // 생성자
    @Autowired
    public MenuItemService(MenuItemRepository baseRepository, S3Service s3Service, RestaurantRepository restaurantRepository) {
        super(baseRepository, s3Service);
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    protected MenuItemRepository getBaseRepository() {
        return (MenuItemRepository) baseRepository;
    }

    @Override
    protected MenuItemResponse response(MenuItem menuItem) {
        return MenuItemResponse.of(menuItem);
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
                    .restaurant(restaurantRepository.findById(menuItemRequest.getRestaurant().getId())
                            .orElseThrow(() -> new EntityNotFoundException()))
                    .build();

            entity = getBaseRepository().save(entity);

            String fileUrl = s3Service.uploadFile(menuItemRequest.getFile(), DIRECTORY_NAME,
                    entity.getId() + FileUtil.getFileExtension(request.getData().getFile()));

            entity.updateFileUrl(fileUrl);

            MenuItemResponse menuItemResponse = response(entity);

            return Header.OK(menuItemResponse);
        } catch (Exception e){
            throw new RuntimeException(this.getClass() + "의 create 메소드 실패", e);
        }
    }


    @Override
    public Header<MenuItemResponse> read(Long id) {
        return Header.OK(response(getBaseRepository().findById(id).
                orElseThrow(() -> new EntityNotFoundException())));
    }

    @Transactional
    @Override
    public Header<MenuItemResponse> update(Long id, Header<MenuItemRequest> request) {
        try {
            MenuItemRequest menuItemRequest = request.getData();

            MultipartFile file = menuItemRequest.getFile();

            MenuItem findEntity = getBaseRepository().findById(id)
                    .orElseThrow(() -> new EntityNotFoundException());

            String existingUrl = findEntity.getFileUrl();

            if (menuItemRequest.isNeedFileChange()) { // 프론트에서 파일 변경이 필요하다 한 경우
                if (file.isEmpty() && existingUrl != null) { // 대체 파일이 없고 기존 url이 있는 경우
                    s3Service.deleteFile(existingUrl); // 기존 파일 삭제
                    findEntity.updateFileUrl(null); // 파일 URL 삭제
                } else if (!file.isEmpty()) { // 대체 파일이 있는 경우
                    String newUrl = s3Service.updateFile(existingUrl, file); // 새 파일 업로드
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
            MenuItem entity = getBaseRepository().findById(id)
                    .orElseThrow(() -> new EntityNotFoundException());

            if(entity.getFileUrl() != null)
                s3Service.deleteFile(entity.getFileUrl());

            getBaseRepository().delete(entity);

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
