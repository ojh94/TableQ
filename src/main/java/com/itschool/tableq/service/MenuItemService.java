package com.itschool.tableq.service;

import com.itschool.tableq.domain.MenuItem;
import com.itschool.tableq.domain.Restaurant;
import com.itschool.tableq.domain.Review;
import com.itschool.tableq.domain.User;
import com.itschool.tableq.network.Header;
import com.itschool.tableq.network.Pagination;
import com.itschool.tableq.network.request.MenuItemRequest;
import com.itschool.tableq.network.request.RestaurantRequest;
import com.itschool.tableq.network.response.MenuItemResponse;
import com.itschool.tableq.network.response.RestaurantResponse;
import com.itschool.tableq.network.response.ReviewResponse;
import com.itschool.tableq.network.response.UserResponse;
import com.itschool.tableq.repository.MenuItemRepository;
import com.itschool.tableq.repository.RestaurantRepository;
import com.itschool.tableq.service.base.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MenuItemService extends BaseService<MenuItemRequest, MenuItemResponse, MenuItem> {

    @Autowired
    RestaurantRepository restaurantRepository;

    @Override
    public Header<List<MenuItemResponse>> getPaginatedList(Pageable pageable) {
        Page<MenuItem> entities =  baseRepository.findAll(pageable);

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
        return new MenuItemResponse(menuItem);
    }

    public List<MenuItemResponse> responseList(List<MenuItem> menuList) {
        List<MenuItemResponse> responseList = new ArrayList<>();

        for(MenuItem menuItem : menuList){
            responseList.add(response(menuItem));
        }

        return responseList;
    }

    @Override
    public Header<MenuItemResponse> create(Header<MenuItemRequest> request) {
        MenuItemRequest menuItemRequest = request.getData();

        MenuItem menuItem = MenuItem.builder()
                .name(menuItemRequest.getName())
                .price(menuItemRequest.getPrice())
                .description(menuItemRequest.getDescription())
                .imageUrl(menuItemRequest.getDescription())
                .restaurant(restaurantRepository.findById(menuItemRequest.getRestaurantId())
                        .orElseThrow(() -> new IllegalArgumentException("not found")))
                .build();

        baseRepository.save(menuItem);
        return Header.OK(response(menuItem));
    }

    @Override
    public Header<MenuItemResponse> read(Long id) {
        return Header.OK(response(baseRepository.findById(id).orElse(null)));
    }

    @Override
    @Transactional
    public Header<MenuItemResponse> update(Long id, Header<MenuItemRequest> request) {
        MenuItemRequest menuItemRequest = request.getData();

        MenuItem menuItem = baseRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("not found"));
        menuItem.update(menuItemRequest);

        return Header.OK(response(menuItem));
    }

    @Override
    public Header delete(Long id) {
        return baseRepository.findById(id)
                .map(restaurant -> {
                    baseRepository.delete(restaurant);
                    return Header.OK(response(restaurant));
                }).orElseThrow(() -> new RuntimeException("Restaurant delete fail"));
    }

    public Header<List<MenuItemResponse>> readByRestaurantId(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow();

        return Header.OK(
                responseList(
                        ((MenuItemRepository)baseRepository).findByRestaurant(restaurant).orElseThrow()));
    }
}
