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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
    protected BreakHour convertBaseEntityFromRequest(BreakHourRequest requestEntity) {
        return BreakHour.builder()
                .breakStart(requestEntity.getBreakStart())
                .breakEnd(requestEntity.getBreakEnd())
                .dayOfWeek(requestEntity.getDayOfWeek())
                .restaurant(restaurantRepository.findById(requestEntity.getRestaurant().getId())
                        .orElseThrow(() -> new EntityNotFoundException()))
                .build();
    }

    public Header<List<BreakHourResponse>> readByRestaurantId(Long restaurantId, Pageable pageable) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new EntityNotFoundException("레스토랑을 찾을 수 없습니다. ID: " + restaurantId));

        Page<BreakHour> entities = getBaseRepository().findByRestaurant(restaurant, pageable);

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

    @Transactional
    public List<BreakHourResponse> upsertListByDayOfWeek(Restaurant restaurant, List<BreakHourRequest> requestedEntities) {
        List<BreakHour> upsertedEntityList = new ArrayList<>();

        for(BreakHourRequest requestedEntity : requestedEntities){

            Optional<BreakHour> needUpsertEntity = getBaseRepository().findByRestaurantAndDayOfWeek(restaurant, requestedEntity.getDayOfWeek());

            if (needUpsertEntity.isPresent()) {
                needUpsertEntity.get().update(requestedEntity);
                upsertedEntityList.add(needUpsertEntity.get());
            } else {
                BreakHour createdEntity = baseRepository.save(convertBaseEntityFromRequest(requestedEntity));
                upsertedEntityList.add(createdEntity);
            }
        }

        return responseList(upsertedEntityList);
    }

    @Transactional
    public void deleteAllByRestaurant(Restaurant restaurant) {

        List<BreakHour> breakHourList = getBaseRepository().findAllByRestaurant(restaurant);

        getBaseRepository().deleteAll(breakHourList);

        getBaseRepository().flush();
    }
}
