package com.itschool.tableq.service;

import com.itschool.tableq.domain.Reservation;
import com.itschool.tableq.domain.Restaurant;
import com.itschool.tableq.domain.User;
import com.itschool.tableq.network.Header;
import com.itschool.tableq.network.request.ReservationRequest;
import com.itschool.tableq.network.response.ReservationResponse;
import com.itschool.tableq.network.response.RestaurantResponse;
import com.itschool.tableq.repository.ReservationRepository;
import com.itschool.tableq.repository.RestaurantRepository;
import com.itschool.tableq.repository.UserRepository;
import com.itschool.tableq.service.base.BaseService;
import com.itschool.tableq.util.DateUtil;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReservationService extends BaseService<ReservationRequest, ReservationResponse, Reservation> {

    private final UserRepository userRepository;

    private final RestaurantRepository restaurantRepository;

    // 생성자
    @Autowired
    public ReservationService(ReservationRepository baseRepository,
                              UserRepository userRepository,
                              RestaurantRepository restaurantRepository) {
        super(baseRepository);
        this.userRepository = userRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    protected ReservationRepository getBaseRepository() {
        return (ReservationRepository) baseRepository;
    }

    @Override
    protected ReservationResponse response(Reservation reservation) {
        return ReservationResponse.of(reservation);
    }

    @Override
    protected Reservation convertBaseEntityFromRequest(ReservationRequest requestEntity) {

        User user = userRepository.findById(requestEntity.getUser().getId())
                .orElseThrow(()-> new EntityNotFoundException());

        Restaurant restaurant = restaurantRepository.findById(requestEntity.getRestaurant().getId())
                .orElseThrow(()-> new EntityNotFoundException());

        if(isExist(user, restaurant)) {
            throw new RuntimeException("Already Reserved User");
        }

        return Reservation.builder()
                .reservationNumber(getWaitingNubmer(restaurant))
                .people(requestEntity.getPeople())
                .restaurant(restaurant)
                .user(user)
                .build();
    }

    public Boolean isExist(User user, Restaurant restaurant){
        // 대기중인 예약이 존재한다면 true 반환
        // 그 이미 완료된 줄서기라면 대기중이 아닌 것으로 판단하여 false 반환
        List<Reservation> reservations = getBaseRepository().findByUserAndRestaurantAndCreatedAtBetween(
                        user, restaurant, DateUtil.getStartOfDay(), DateUtil.getEndOfDay());
        if(reservations != null){
            for(Reservation entity : reservations){
                if (entity.getIsEntered() == null) return true;
            }
        }

        return false;
    }

    public Boolean isExistByRestaurantId(User user, Long restaurantId){
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new EntityNotFoundException());

        return isExist(user, restaurant);
    }

    public Header<List<ReservationResponse>> readVisitedRestaurantsFor3Day(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(()->new EntityNotFoundException("유저가 존재하지 않습니다."));

        List<Reservation> reservationList = getBaseRepository().findByIsEnteredAndUserAndCreatedAtBetween(
                    true, user, DateUtil.get3DaysAgo(),DateUtil.getEndOfDay()
                );

        return Header.OK(responseList(reservationList));
    }

    public Header<Long> countUserReservationsFor3Days(Long userId, Long restaurantId){
        Long count = 0L;
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new EntityNotFoundException("유저를 조회할 수 없습니다."));

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(()->new EntityNotFoundException("식당을 조회할 수 없습니다."));

        List<Reservation> reservationList = getBaseRepository().findByIsEnteredAndUserAndRestaurantAndCreatedAtBetween(true, user, restaurant,
                                                                                                        DateUtil.get3DaysAgo(),DateUtil.getEndOfDay());

        count = (long) reservationList.size();

        return Header.OK(count);
    }

    public Long getWaitingNubmer(Restaurant restaurant) {
        // 대기번호를 계산하는 메소드
        Long number = 1L;

        LocalDate today = LocalDate.now();

        List<Reservation> reservationList = getBaseRepository().findByRestaurant(restaurant)
                .orElseThrow(()-> new EntityNotFoundException());

        for (Reservation reservation : reservationList) {
            LocalDate reservationTime = reservation.getCreatedAt().toLocalDate();

            if (reservationTime.isEqual(today)) {
                number += 1;
            } else continue;
        }
        return number;
    }

    public Header<Integer> getQueue(Long restaurantId){
        Integer queue = 0;
        queue = getBaseRepository().countByRestaurantIdAndIsEnteredAndCreatedAtBetween(
                restaurantId, null, DateUtil.getStartOfDay(), DateUtil.getEndOfDay()
        );
        return Header.OK(queue);
    }

    public Header<Long> getUserQueue(Long reservationId) {
        Long userTurn = 1L;

        Reservation entity = getBaseRepository().findById(reservationId)
                .orElseThrow(() -> new EntityNotFoundException());

        Restaurant restaurant = restaurantRepository.findById(entity.getRestaurant().getId())
                .orElseThrow(() -> new EntityNotFoundException());

        List<Reservation> allReservationForRestraunt = getBaseRepository()
                .findByIsEnteredAndRestaurantAndCreatedAtBetweenOrderByIdAsc(null, restaurant, DateUtil.getStartOfDay(), DateUtil.getEndOfDay());

        for (Reservation reservation : allReservationForRestraunt) {
            if (reservation.getId() == reservationId) break;
            else userTurn++;
        }
        // 현재 해당 레스토랑에 유효한 예약이 하나도 없는 경우 + 못 찾았을 경우
        if(userTurn > allReservationForRestraunt.size()) {
            // 해당 레스토랑에 대한 예약 정보가 없거나, 이미 처리가 완료된 예약을 예약 내역 화면에서 조회하려고 할때
            throw new RuntimeException("INVALID_RESERVATION");
        }

        return Header.OK(userTurn);
    }

    public Header<List<ReservationResponse>> readByRestaurantId(Long restaurantId, Pageable pageable){
        // 식당을 예약한 손님 조회
        // --> 필요한 정보 : reservationNumber(대기번호), people(인원), User.contactNumber(예약자 전화번호)
        Restaurant restaurant = restaurantRepository.findById(restaurantId).get();
        List<Reservation> reservationList = getBaseRepository().findByRestaurant(restaurant)
                .orElseThrow(()-> new EntityNotFoundException());

        return Header.OK(responseList(reservationList));
    }

    public Header<List<ReservationResponse>> readByUserId(Long userId, Pageable pageable){
        // 유저가 예약했던 정보 조회
        // --> 유저가 예약했던 식당을 조회하는 방식으로 변경 건의
        User user = userRepository.findById(userId).get();
        List<Reservation> reservationList = getBaseRepository().findByUser(user)
                .orElseThrow(()-> new EntityNotFoundException());

        return Header.OK(responseList(reservationList));
    }
}
