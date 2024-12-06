package com.itschool.tableq.service;

import com.itschool.tableq.domain.*;
import com.itschool.tableq.network.Header;
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

import java.util.List;
import java.util.Optional;

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

    @Override
    protected Review convertBaseEntityFromRequest(ReviewRequest requestEntity) {

        return Review.builder()
                .content(requestEntity.getContent())
                .starRating(requestEntity.getStarRating())
                .restaurant(restaurantRepository.findById(requestEntity.getRestaurant().getId())
                        .orElseThrow(() -> new EntityNotFoundException("레스토랑을 찾을 수 없습니다.")))
                .user(userRepository.findById(requestEntity.getUser().getId())
                        .orElseThrow(() -> new EntityNotFoundException("유저를 찾을 수 없습니다.")))
                .reservation(reservationRepository.findById(requestEntity.getReservation().getId())
                        .orElseThrow(() -> new EntityNotFoundException("예약을 찾을 수 없습니다.")))
                .build();
    }

    public Header<Boolean> isReviewable(Long reservationId){
        Reservation reservation = reservationRepository.findByIdAndIsEnteredAndCreatedAtBetween(
                reservationId, true, DateUtil.get3DaysAgo(), DateUtil.getEndOfDay()
        );

        if(reservation != null) {
            Review review = getBaseRepository().findByReservation(reservation);

            if(review == null)
                return Header.OK(true);
        }

        return Header.OK(false);
    }

    public Header<Long> countUserReviewsFor3Days(Long restaurantId, Long userId){
        Long count = 0L;
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new EntityNotFoundException("유저를 조회할 수 없습니다."));

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(()->new EntityNotFoundException("식당을 조회할 수 없습니다."));


        List<Review> reviewList = getBaseRepository().findByUserAndRestaurantAndCreatedAtBetween(user, restaurant,
                                                                                    DateUtil.get3DaysAgo(), DateUtil.getEndOfDay());

        count = (long) reviewList.size();

        return Header.OK(count);
    }

    // ID에 해당하는 식당에 대한 리뷰 조회
    public Header<List<ReviewResponse>> readByRestaurantId(Long restaurantId, Pageable pageable){
        Page<Review> entities = getBaseRepository().findByRestaurantId(restaurantId, pageable);

        return convertPageToList(entities);
    }

    // ID에 해당하는 유저가 남긴 리뷰 조회
    public Header<List<ReviewResponse>> readByUserId(Long userId, Pageable pageable){
        Page<Review> entities = getBaseRepository().findByUserId(userId, pageable);

        return convertPageToList(entities);
    }
}
