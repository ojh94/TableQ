package com.itschool.tableq.service;

import com.itschool.tableq.domain.*;
import com.itschool.tableq.network.Header;
import com.itschool.tableq.network.Pagination;
import com.itschool.tableq.network.request.BreakHourRequest;
import com.itschool.tableq.network.response.BreakHourResponse;
import com.itschool.tableq.network.response.MenuItemResponse;
import com.itschool.tableq.network.response.ReviewResponse;
import com.itschool.tableq.network.response.UserResponse;
import com.itschool.tableq.repository.BreakHoursRepository;
import com.itschool.tableq.repository.MenuItemRepository;
import com.itschool.tableq.repository.RestaurantRepository;
import com.itschool.tableq.repository.ReviewRepository;
import com.itschool.tableq.service.base.BaseService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BreakHourService extends BaseService<BreakHourRequest, BreakHourResponse, BreakHour> {
    @Autowired
    RestaurantRepository restaurantRepository;

    @Override
    public Header<List<BreakHourResponse>> getPaginatedList(Pageable pageable) {
        Page<BreakHour> entities =  baseRepository.findAll(pageable);

        List<BreakHourResponse> breakHourResponsesList = entities.stream()
                .map(entity -> response(entity))
                .collect(Collectors.toList());

        Pagination pagination = Pagination.builder()
                .totalPages(entities.getTotalPages())
                .totalElements(entities.getTotalElements())
                .currentPage(entities.getNumber())
                .currentElements(entities.getNumberOfElements())
                .build();

        return Header.OK(breakHourResponsesList, pagination);
    }

    @Override
    protected BreakHourResponse response(BreakHour breakHour) {
        return new BreakHourResponse(breakHour);
    }

    @Override
    public Header<BreakHourResponse> create(Header<BreakHourRequest> request) {
        BreakHourRequest breakHourRequest = request.getData();

        BreakHour breakHour = BreakHour.builder()
                .breakStart(breakHourRequest.getBreakStart())
                .breakEnd(breakHourRequest.getBreakEnd())
                .dayOfWeek(breakHourRequest.getDayOfWeek())
                .build();

        baseRepository.save(breakHour);
        return Header.OK(response(breakHour));
    }

    @Override
    public Header<BreakHourResponse> read(Long id) {
        return Header.OK(response(baseRepository.findById(id).orElse(null)));
    }

    @Override
    @Transactional
    public Header<BreakHourResponse> update(Long id, Header<BreakHourRequest> request) {
        BreakHourRequest breakHourRequest = request.getData();
        BreakHour breakHour = baseRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("not found"));
        breakHour.update(breakHourRequest);
        return Header.OK(response(breakHour));
    }

    @Override
    public Header delete(Long id) {
        return baseRepository.findById(id)
                .map(breakHour -> {
                    baseRepository.delete(breakHour);
                    return Header.OK(response(breakHour));
                })
                .orElseThrow(() -> new IllegalArgumentException("not found"));
    }

    public Header<List<BreakHourResponse>> readByRestaurantId(Long restaurantId, Pageable pageable) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new EntityNotFoundException("레스토랑을 찾을 수 없습니다. ID: " + restaurantId));

        Page<BreakHour> entities = ((BreakHoursRepository)baseRepository).findByRestaurant(restaurant, pageable);

        List<BreakHourResponse> BreakHourList = entities.stream()
                .map(entity -> response(entity))
                .collect(Collectors.toList());

        Pagination pagination = Pagination.builder()
                .totalPages(entities.getTotalPages())
                .totalElements(entities.getTotalElements())
                .currentPage(entities.getNumber())
                .currentElements(entities.getNumberOfElements())
                .build();

        return Header.OK(BreakHourList, pagination);
    }
}
