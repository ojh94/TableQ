package com.itschool.tableq.service;

import com.itschool.tableq.domain.Restaurant;
import com.itschool.tableq.domain.Review;
import com.itschool.tableq.domain.User;
import com.itschool.tableq.ifs.annotation.AuthorCheck;
import com.itschool.tableq.network.Header;
import com.itschool.tableq.network.Pagination;
import com.itschool.tableq.network.request.ReviewRequest;
import com.itschool.tableq.network.response.ReviewResponse;
import com.itschool.tableq.repository.RestaurantRepository;
import com.itschool.tableq.repository.ReviewRepository;
import com.itschool.tableq.repository.UserRepository;
import com.itschool.tableq.service.base.BaseService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ReviewService extends BaseService<ReviewRequest, ReviewResponse, Review> {
    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    protected ReviewResponse response(Review review) {
        return new ReviewResponse(review);
    }

    protected List<ReviewResponse> responseList(List<Review> reviewList) {
        List<ReviewResponse> responseList = new ArrayList<>();

        for(Review review : reviewList){
            responseList.add(response(review));
        }

        return responseList;
    }

    @Override
    public Header<ReviewResponse> create(Header<ReviewRequest> request){
        ReviewRequest reviewRequest= request.getData();

        Restaurant restaurant = restaurantRepository.findById(reviewRequest.getRestaurant().getId())
                .orElseThrow(() -> new EntityNotFoundException("레스토랑을 찾을 수 없습니다."));

        User user = userRepository.findById(reviewRequest.getUser().getId())
                .orElseThrow(() -> new EntityNotFoundException("유저를 찾을 수 없습니다."));

        Review review = Review.builder()
                .content(reviewRequest.getContent())
                .starRating(reviewRequest.getStarRating())
                .createdAt(LocalDateTime.now())
                .lastModifiedAt(LocalDateTime.now())
                .restaurant(restaurant)
                .user(user)
                .build();

        baseRepository.save(review);

        baseRepository.save(review);
        return Header.OK(response(review));
    }

    @Override
    public Header<ReviewResponse> read(Long id) {
        return Header.OK(response(baseRepository.findById(id).orElse(null)));
    }

    // ID에 해당하는 식당에 대한 리뷰 조회
    public Header<List<ReviewResponse>> readByRestaurantId(Long restaurantId, Pageable pageable){
        // 레스토랑 ID로 레스토랑 먼저 조회
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new EntityNotFoundException("레스토랑을 찾을 수 없습니다. ID: " + restaurantId));

        // 레스토랑에 해당하는 리뷰 목록 조회
        Page<Review> entities = ((ReviewRepository)baseRepository).findByRestaurant(restaurant, pageable);

        List<ReviewResponse> reviewList = entities.stream()
                .map(entity -> response(entity))
                .collect(Collectors.toList());

        Pagination pagination = Pagination.builder()
                .totalPages(entities.getTotalPages())
                .totalElements(entities.getTotalElements())
                .currentPage(entities.getNumber())
                .currentElements(entities.getNumberOfElements())
                .build();

        return Header.OK(reviewList, pagination);
    }

    // ID에 해당하는 유저가 남긴 리뷰 조회
    public Header<List<ReviewResponse>> readByUserId(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("유저를 찾을 수 없습니다. ID: " + userId));

        List<Review> reviewList = ((ReviewRepository)baseRepository).findByUser(user);

        return Header.OK(responseList(reviewList));
    }

    @Override
    public Header<ReviewResponse> update(Long id, Header<ReviewRequest> request) {
        return null;
    }

    @AuthorCheck
    @Override
    public Header delete(Long id) {
        return baseRepository.findById(id).map(review -> {
            baseRepository.delete(review);
            return Header.OK(response(review));
        }).orElseThrow(()->new RuntimeException("review delete fail"));
    }
}
