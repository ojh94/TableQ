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
import groovy.lang.DeprecationException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookmarkService extends BaseService<BookmarkRequest, BookmarkResponse, Bookmark> {

    private final RestaurantRepository restaurantRepository;

    private final UserRepository userRepository;

    // 생성자
    @Autowired
    public BookmarkService(BookmarkRepository bookmarkRepository,
                           RestaurantRepository restaurantRepository,
                           UserRepository userRepository) {
        super(bookmarkRepository);
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
    }

    @Override
    protected BookmarkRepository getBaseRepository() {
        return (BookmarkRepository) baseRepository;
    }

    @Override
    protected BookmarkResponse response(Bookmark bookmark) {
        return BookmarkResponse.of(bookmark);
    }

    @Override
    protected Bookmark convertBaseEntityFromRequest(BookmarkRequest requestEntity) {

        return Bookmark.builder()
                .restaurant(restaurantRepository.findById(requestEntity.getRestaurant().getId())
                        .orElseThrow(() -> new EntityNotFoundException()))
                .user(userRepository.findById(requestEntity.getUser().getId())
                        .orElseThrow(() -> new EntityNotFoundException()))
                .build();
    }

    @Override
    @Deprecated
    public Header<BookmarkResponse> update(Long id, Header<BookmarkRequest> request) throws DeprecationException {
        throw new DeprecationException("연결 테이블이므로 delete, create API 이용할 것");
    }

    public Header<List<BookmarkResponse>> readByUserId(Long userId, Pageable pageable){
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new RuntimeException("Not Found ID"));

        List<Bookmark> bookmarkList = ((BookmarkRepository)baseRepository).findByUser(user)
                .orElseThrow(()-> new EntityNotFoundException());

        return Header.OK(responseList(bookmarkList));
    }

    public Header<Integer> countBookmarks(Long restaurantId){
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(()-> new EntityNotFoundException());
        List<Bookmark> bookmarkList = ((BookmarkRepository)baseRepository).findByRestaurant(restaurant);

        return Header.OK(bookmarkList.size());
    }
}
