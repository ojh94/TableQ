package com.itschool.tableq.service;

import com.itschool.tableq.domain.OpeningHour;
import com.itschool.tableq.domain.Restaurant;
import com.itschool.tableq.network.Header;
import com.itschool.tableq.network.request.OpeningHourRequest;
import com.itschool.tableq.network.response.OpeningHourResponse;
import com.itschool.tableq.repository.OpeningHourRepository;
import com.itschool.tableq.repository.RestaurantRepository;
import com.itschool.tableq.service.base.BaseService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class OpeningHourService extends BaseService<OpeningHourRequest, OpeningHourResponse, OpeningHour> {
    @Autowired
    RestaurantRepository restaurantRepository;

    @Override
    protected OpeningHourResponse response(OpeningHour openingHour) {
        return OpeningHourResponse.of(openingHour);
    }

    @Override
    public Header<OpeningHourResponse> create(Header<OpeningHourRequest> request) {
        OpeningHourRequest openingHourRequest = request.getData();

        OpeningHour openingHour = OpeningHour.builder()
                .openAt(openingHourRequest.getOpenAt())
                .closeAt(openingHourRequest.getCloseAt())
                .dayOfWeek(openingHourRequest.getDayOfWeek())
                .build();

        baseRepository.save(openingHour);
        return Header.OK(response(openingHour));
    }

    @Override
    public Header<OpeningHourResponse> read(Long id) {
        return Header.OK(response(baseRepository.findById(id).orElse(null)));
    }

    @Override
    @Transactional
    public Header<OpeningHourResponse> update(Long id, Header<OpeningHourRequest> request) {
        OpeningHourRequest openingHourRequest = request.getData();

        OpeningHour openingHour = baseRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("not found"));
        openingHour.update(openingHourRequest);
        return Header.OK(response(openingHour));
    }

    @Override
    public Header delete(Long id) {
        return baseRepository.findById(id)
                .map(openingHour -> {
                    baseRepository.delete(openingHour);
                    return Header.OK(response(openingHour));
                })
                .orElseThrow(() -> new IllegalArgumentException("not found"));
    }

    public Header<List<OpeningHourResponse>> readByRestaurantId(Long restaurantId, Pageable pageable) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new EntityNotFoundException("레스토랑을 찾을 수 없습니다."));

        return convertPageToList(((OpeningHourRepository)baseRepository).findByRestaurant(restaurant, pageable));
    }
}
