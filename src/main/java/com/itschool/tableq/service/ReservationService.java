package com.itschool.tableq.service;

import com.itschool.tableq.domain.Reservations;
import com.itschool.tableq.network.request.AddReservationRequest;
import com.itschool.tableq.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    public int count(){
        int number = 0 ;
        return number;
    }
    public Reservations save(AddReservationRequest request){
        return reservationRepository.save(Reservations.builder()
                .customerContactNumber(request.getCustomerContactNumber())
                .reservationNumber(request.getReservation_number())
                .reservationNumber(count())
                .reserveTime(LocalDateTime.now())
                .people(request.getPeople())
                .store(request.getStore())
                .build()
        );
    }
}
