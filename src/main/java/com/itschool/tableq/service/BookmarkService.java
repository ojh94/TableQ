package com.itschool.tableq.service;

import com.itschool.tableq.domain.Bookmark;
import com.itschool.tableq.domain.Restaurant;
import com.itschool.tableq.domain.User;
import com.itschool.tableq.network.Header;
import com.itschool.tableq.network.request.BookmarkRequest;
import com.itschool.tableq.network.response.BookmarkResponse;
import com.itschool.tableq.repository.BookmarkRepository;
import com.itschool.tableq.repository.RestaurantRepository;
import com.itschool.tableq.repository.UserRepository;
import com.itschool.tableq.service.base.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BookmarkService extends
        BaseService<BookmarkRequest, BookmarkResponse, Bookmark> {

    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    protected BookmarkResponse response(Bookmark bookmark) {
        return BookmarkResponse.of(bookmark);
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
