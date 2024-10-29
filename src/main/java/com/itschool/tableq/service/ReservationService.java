package com.itschool.tableq.service;

import com.itschool.tableq.domain.Reservation;
import com.itschool.tableq.network.Header;
import com.itschool.tableq.network.response.ReservationResponse;
import com.itschool.tableq.network.request.ReservationRequest;
import com.itschool.tableq.service.base.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class ReservationService extends
        BaseService<ReservationRequest, ReservationResponse, Reservation> {

    @Override
    protected ReservationResponse response(Reservation reservation) {
        return new ReservationResponse(reservation);
    }

    public Integer count(){
        int number = 0;

        return number;
    }

    @Override
    public Header<ReservationResponse> create(Header<ReservationRequest> request) {
        ReservationRequest reservationRequest = request.getData();

        Reservation reservation = Reservation.builder()
                .reservationNumber(count())
                .reserveTime(LocalDateTime.now())
                .people(reservationRequest.getPeople())
                .restaurant(reservationRequest.getRestaurant())
                .user(reservationRequest.getUser())
                .build();

        baseRepository.save(reservation);
        return Header.OK(response(reservation));
    }

    @Override
    public Header<ReservationResponse> read(Long id) {
        return Header.OK(response(baseRepository.findById(id).orElse(null)));
    }

    @Override
    @Transactional
    public Header<ReservationResponse> update(Long id, Header<ReservationRequest> request) {
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
