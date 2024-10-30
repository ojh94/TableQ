package com.itschool.tableq.service;

import com.itschool.tableq.domain.Restaurant;
import com.itschool.tableq.domain.Review;
import com.itschool.tableq.domain.User;
import com.itschool.tableq.ifs.AuthorCheck;
import com.itschool.tableq.network.Header;
import com.itschool.tableq.network.request.ReviewRequest;
import com.itschool.tableq.network.response.ReviewResponse;
import com.itschool.tableq.repository.RestaurantRepository;
import com.itschool.tableq.repository.ReviewRepository;
import com.itschool.tableq.repository.UserRepository;
import com.itschool.tableq.service.base.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

        Review review = Review.builder()
                .content(reviewRequest.getContent())
                .createdAt(LocalDateTime.now())
                .lastModifiedAt(LocalDateTime.now())
                .restaurant(reviewRequest.getRestaurant())
                .user(reviewRequest.getUser())
                .build();

        baseRepository.save(review);
        return Header.OK(response(review));
    }

    @Override
    public Header<ReviewResponse> read(Long id) {
        return Header.OK(response(baseRepository.findById(id).orElse(null)));
    }

    // ID에 해당하는 식당에 대한 리뷰 조회
    public Header<List<ReviewResponse>> readByRestaurantId(Long restaurantId){
        Restaurant restaurant = restaurantRepository.findById(restaurantId).get();
        List<Review> reviewList = ((ReviewRepository)baseRepository).findByRestaurant(restaurant).orElse(null);

        return Header.OK(responseList(reviewList));
    }

    // ID에 해당하는 유저가 남긴 리뷰 조회
    public Header<List<ReviewResponse>> readByUserId(Long userId){
        User user = userRepository.findById(userId).get();
        List<Review> reviewList = ((ReviewRepository)baseRepository).findByUser(user).orElse(null);

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
