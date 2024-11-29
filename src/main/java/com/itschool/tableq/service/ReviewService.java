package com.itschool.tableq.service;

import com.itschool.tableq.domain.*;
import com.itschool.tableq.ifs.annotation.AuthorCheck;
import com.itschool.tableq.network.Header;
import com.itschool.tableq.network.Pagination;
import com.itschool.tableq.network.request.BusinessInformationRequest;
import com.itschool.tableq.network.request.ReviewRequest;
import com.itschool.tableq.network.response.ReviewResponse;
import com.itschool.tableq.repository.ReservationRepository;
import com.itschool.tableq.repository.RestaurantRepository;
import com.itschool.tableq.repository.ReviewRepository;
import com.itschool.tableq.repository.UserRepository;
import com.itschool.tableq.service.base.BaseService;
import com.itschool.tableq.util.DateUtil;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService extends BaseService<ReviewRequest, ReviewResponse, Review> {
    @Autowired
    ReservationRepository reservationRepository;

    private final UserRepository userRepository;

    // 생성자

    private final RestaurantRepository restaurantRepository;

    @Autowired
    public ReviewService(ReviewRepository baseRepository,
                         RestaurantRepository restaurantRepository,
                         UserRepository userRepository) {
        super(baseRepository);
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
    }

    @Override
    protected ReviewRepository getBaseRepository() {
        return (ReviewRepository) baseRepository;
    }

    @Override
    protected ReviewResponse response(Review review) {
        return ReviewResponse.of(review);
    }

    public Header<Long> countUserReviewsFor3Days(Long restaurantId, Long userId){
        Long count = 0L;
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new EntityNotFoundException("유저를 조회할 수 없습니다."));

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(()->new EntityNotFoundException("식당을 조회할 수 없습니다."));


        List<Review> reviewList = ((ReviewRepository)baseRepository).findByUserAndRestaurantAndCreatedAtBetween(user, restaurant,
                                                                                    DateUtil.get3DaysAgo(), DateUtil.getEndOfDay());

        count = (long) reviewList.size();

        return Header.OK(count);
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
                .restaurant(restaurant)
                .user(user)
                .build();

        getBaseRepository().save(review);

        return Header.OK(response(review));
    }

    @Override
    public Header<ReviewResponse> read(Long id) {
        return Header.OK(response(getBaseRepository().findById(id).orElseThrow(()-> new EntityNotFoundException())));
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
    public Header<List<ReviewResponse>> readByUserId(Long userId, Pageable pageable){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("유저를 찾을 수 없습니다. ID: " + userId));

        Page<Review> entities = ((ReviewRepository)baseRepository).findByUser(user, pageable);

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

    @Override
    public Header<ReviewResponse> update(Long id, Header<ReviewRequest> request) {
        ReviewRequest reviewRequest = request.getData();

        Review review = getBaseRepository().findById(id)
                .orElseThrow(() -> new EntityNotFoundException());

        review.update(reviewRequest);

        return Header.OK(response(review));
    }

    @AuthorCheck
    @Override
    public Header delete(Long id) {
        return getBaseRepository().findById(id).map(review -> {
            getBaseRepository().delete(review);
            return Header.OK(response(review));
        }).orElseThrow(()->new RuntimeException("review delete fail"));
    }
}
