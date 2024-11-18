package com.itschool.tableq.service;

import com.itschool.tableq.domain.Bookmark;
import com.itschool.tableq.domain.Restaurant;
import com.itschool.tableq.domain.User;
import com.itschool.tableq.network.Header;
import com.itschool.tableq.network.Pagination;
import com.itschool.tableq.network.request.BookmarkRequest;
import com.itschool.tableq.network.response.BookmarkResponse;
import com.itschool.tableq.repository.BookmarkRepository;
import com.itschool.tableq.repository.RestaurantRepository;
import com.itschool.tableq.repository.UserRepository;
import com.itschool.tableq.service.base.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BookmarkService extends
        BaseService<BookmarkRequest, BookmarkResponse, Bookmark> {

    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    public Header<List<BookmarkResponse>> getPaginatedList(Pageable pageable) {
        Page<Bookmark> entities =  baseRepository.findAll(pageable);

        List<BookmarkResponse> bookmarkResponseList = entities.stream()
                .map(entity -> response(entity))
                .collect(Collectors.toList());

        Pagination pagination = Pagination.builder()
                .totalPages(entities.getTotalPages())
                .totalElements(entities.getTotalElements())
                .currentPage(entities.getNumber())
                .currentElements(entities.getNumberOfElements())
                .build();

        return Header.OK(bookmarkResponseList, pagination);
    }

    @Override
    protected BookmarkResponse response(Bookmark bookmark) {
        return new BookmarkResponse(bookmark);
    }

    protected List<BookmarkResponse> responseList(List<Bookmark> bookmarkList){
        List<BookmarkResponse> responseList = new ArrayList<>();

        for(Bookmark bookmark : bookmarkList){
            responseList.add(response(bookmark));
        }

        return responseList;
    }

    @Override
    public Header<BookmarkResponse> create(Header<BookmarkRequest> request) {
        BookmarkRequest bookmarkRequest = request.getData();

        Restaurant restaurant =restaurantRepository.findById(bookmarkRequest.getRestaurantId())
                .orElseThrow(() -> new RuntimeException("Not Found ID"));

        User user = userRepository.findById(bookmarkRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("Not Found ID"));

        Bookmark bookmark = Bookmark.builder()
                .restaurant(restaurant)
                .user(user)
                .build();

        baseRepository.save(bookmark);
        return Header.OK(response(bookmark));
    }

    @Override
    public Header<BookmarkResponse> read(Long id) {
        return Header.OK(response(baseRepository.findById(id).orElse(null)));
    }

    public Header<List<BookmarkResponse>> readByUserId(Long userId, Pageable pageable){
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new RuntimeException("Not Found ID"));

        List<Bookmark> bookmarkList = ((BookmarkRepository)baseRepository).findByUser(user).orElse(null);

        return Header.OK(responseList(bookmarkList));
    }

    public Header<Integer> countBookmarks(Long restaurantId){
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElse(null);
        List<Bookmark> bookmarkList = ((BookmarkRepository)baseRepository).findByRestaurant(restaurant);

        return Header.OK(bookmarkList.size());
    }
    @Override
    public Header<BookmarkResponse> update(Long id, Header<BookmarkRequest> request) {
        return null;
    }

    @Override
    public Header delete(Long id) {
        return baseRepository.findById(id)
                .map(bookmark -> {
                    baseRepository.delete(bookmark);
                    return Header.OK(response(bookmark));
                })
                .orElseThrow(() -> new RuntimeException("Bookmark delete fail"));
    }
}
