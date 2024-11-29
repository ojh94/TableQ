package com.itschool.tableq.service;

import com.itschool.tableq.domain.BreakHour;
import com.itschool.tableq.domain.Restaurant;
import com.itschool.tableq.network.Header;
import com.itschool.tableq.network.Pagination;
import com.itschool.tableq.network.request.BreakHourRequest;
import com.itschool.tableq.network.response.BreakHourResponse;
import com.itschool.tableq.repository.BreakHourRepository;
import com.itschool.tableq.repository.RestaurantRepository;
import com.itschool.tableq.service.base.BaseService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BreakHourService extends BaseService<BreakHourRequest, BreakHourResponse, BreakHour> {

    private final RestaurantRepository restaurantRepository;

    // 생성자
    @Autowired
    public BreakHourService(BreakHourRepository baseRepository,
                            RestaurantRepository restaurantRepository) {
        super(baseRepository);
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    protected BreakHourRepository getBaseRepository() {
        return (BreakHourRepository) baseRepository;
    }

    @Override
    protected BreakHourResponse response(BreakHour breakHour) {
        return BreakHourResponse.of(breakHour);
    }

    @Override
    public Header<BreakHourResponse> create(Header<BreakHourRequest> request) {
        BreakHourRequest breakHourRequest = request.getData();

        BreakHour breakHour = BreakHour.builder()
                .breakStart(breakHourRequest.getBreakStart())
                .breakEnd(breakHourRequest.getBreakEnd())
                .dayOfWeek(breakHourRequest.getDayOfWeek())
                .build();

        getBaseRepository().save(breakHour);
        return Header.OK(response(breakHour));
    }

    @Override
    public Header<BreakHourResponse> read(Long id) {
        return Header.OK(response(getBaseRepository().findById(id).orElseThrow(()-> new EntityNotFoundException())));
    }

    @Override
    @Transactional
    public Header<BreakHourResponse> update(Long id, Header<BreakHourRequest> request) {
        BreakHourRequest breakHourRequest = request.getData();
        BreakHour breakHour = getBaseRepository().findById(id).orElseThrow(() -> new EntityNotFoundException());
        breakHour.update(breakHourRequest);
        return Header.OK(response(breakHour));
    }

    @Override
    public Header delete(Long id) {
        return getBaseRepository().findById(id)
                .map(breakHour -> {
                    getBaseRepository().delete(breakHour);
                    return Header.OK(response(breakHour));
                })
                .orElseThrow(() -> new EntityNotFoundException());
    }

    public Header<List<BreakHourResponse>> readByRestaurantId(Long restaurantId, Pageable pageable) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new EntityNotFoundException("레스토랑을 찾을 수 없습니다. ID: " + restaurantId));

        Page<BreakHour> entities = ((BreakHourRepository)baseRepository).findByRestaurant(restaurant, pageable);

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
