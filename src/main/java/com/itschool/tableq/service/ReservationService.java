package com.itschool.tableq.service;

import com.itschool.tableq.domain.Reservation;
import com.itschool.tableq.domain.Restaurant;
import com.itschool.tableq.domain.Review;
import com.itschool.tableq.domain.User;
import com.itschool.tableq.network.Header;
import com.itschool.tableq.network.response.ReservationResponse;
import com.itschool.tableq.network.request.ReservationRequest;
import com.itschool.tableq.network.response.ReviewResponse;
import com.itschool.tableq.repository.ReservationRepository;
import com.itschool.tableq.repository.RestaurantRepository;
import com.itschool.tableq.repository.ReviewRepository;
import com.itschool.tableq.repository.UserRepository;
import com.itschool.tableq.service.base.BaseService;
import com.itschool.tableq.util.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

import org.springframework.data.domain.Pageable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ReservationService extends
        BaseService<ReservationRequest, ReservationResponse, Reservation> {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RestaurantRepository restaurantRepository;

    @Override
    public Header<List<ReservationResponse>> getPaginatedList(Pageable pageable) {
        return null;
    }

    @Override
    protected ReservationResponse response(Reservation reservation) {
        return new ReservationResponse(reservation);
    }

    protected List<ReservationResponse> responseList(List<Reservation> reservationList) {
        List<ReservationResponse> responseList = new ArrayList<>();

        for(Reservation reservation : reservationList){
            responseList.add(response(reservation));
        }

        return responseList;
    }

    public Boolean isExist(User user, Restaurant restaurant){
        // 대기중인 예약이 존재한다면 true 반환
        // 그 이미 완료된 줄서기라면 대기중이 아닌 것으로 판단하여 false 반환
        List<Reservation> reservations = ((ReservationRepository)baseRepository)
                .findByUserAndRestaurantAndCreatedAtBetween(
                        user, restaurant, DateUtil.getStartOfDay(), DateUtil.getEndOfDay()
                );
        if(reservations != null){
            for(Reservation entity : reservations){
                if (entity.getIsEntered() == null) return true;
            }
        }

        return false;
    }

    public Integer getWaitingNubmer(Restaurant restaurant){
        // 대기번호를 계산하는 메소드
        int number = 1;

        LocalDate today = LocalDate.now();

        List<Reservation> reservationList = ((ReservationRepository)baseRepository)
                .findByRestaurant(restaurant).orElse(null);

        for(Reservation reservation : reservationList){
            LocalDate reservationTime = reservation.getCreatedAt().toLocalDate();

            if(reservationTime.isEqual(today)){
                number+=1;
            }

            else continue;
        }

        return number;
    }

    @Override
    public Header<ReservationResponse> create(Header<ReservationRequest> request) {
        ReservationRequest reservationRequest = request.getData();

        User user = userRepository.findById(reservationRequest.getUserId())
                .orElseThrow(()-> new RuntimeException("None Exist User ID"));

        Restaurant restaurant = restaurantRepository.findById(reservationRequest.getRestaurantId())
                .orElseThrow(()-> new RuntimeException("None Exist Restaurant ID"));

        if(isExist(user, restaurant)) {
            throw new RuntimeException("Already Reserved User");
        } else {
            Reservation reservation = Reservation.builder()
                    .reservationNumber(getWaitingNubmer(restaurant))
                    .people(reservationRequest.getPeople())
                    .restaurant(restaurant)
                    .user(user)
                    .build();

            baseRepository.save(reservation);
            return Header.OK(response(reservation));
        }
    }

    @Override
    public Header<ReservationResponse> read(Long id) {
        // ID에 해당하는 예약에 대한 정보 조회
        return Header.OK(response(baseRepository.findById(id).orElse(null)));
    }

    public Header<List<ReservationResponse>> readByRestaurantId(Long restaurantId, Pageable pageable){
        // 식당을 예약한 손님 조회
        // --> 필요한 정보 : reservationNumber(대기번호), people(인원), User.contactNumber(예약자 전화번호)
        Restaurant restaurant = restaurantRepository.findById(restaurantId).get();
        List<Reservation> reservationList = ((ReservationRepository)baseRepository)
                .findByRestaurant(restaurant).orElse(null);

        return Header.OK(responseList(reservationList));
    }

    public Header<List<ReservationResponse>> readByUserId(Long userId, Pageable pageable){
        // 유저가 예약했던 정보 조회
        // --> 유저가 예약했던 식당을 조회하는 방식으로 변경 건의
        User user = userRepository.findById(userId).get();
        List<Reservation> reservationList = ((ReservationRepository)baseRepository)
                .findByUser(user).orElse(null);

        return Header.OK(responseList(reservationList));
    }

    @Override
    @Transactional
    public Header<ReservationResponse> update(Long id, Header<ReservationRequest> request) {
        // 손님이 입장을 했는지 판단하여 업데이트
        ReservationRequest reservationRequest = request.getData();

        Reservation reservation = baseRepository.findById(id).orElse(null);

        reservation.update(reservationRequest);

        return Header.OK(response(reservation));
    }

    @Override
    public Header delete(Long id) {
        return null;
    }
}
