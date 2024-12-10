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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OpeningHourService extends BaseService<OpeningHourRequest, OpeningHourResponse, OpeningHour> {

    private final RestaurantRepository restaurantRepository;

    // 생성자
    @Autowired
    public OpeningHourService(OpeningHourRepository baseRepository,
                              RestaurantRepository restaurantRepository) {
        super(baseRepository);
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    protected OpeningHourRepository getBaseRepository() {
        return (OpeningHourRepository) baseRepository;
    }

    @Override
    protected OpeningHourResponse response(OpeningHour openingHour) {
        return OpeningHourResponse.of(openingHour);
    }

    @Override
    protected OpeningHour convertBaseEntityFromRequest(OpeningHourRequest requestEntity) {

        return OpeningHour.builder()
                .openAt(requestEntity.getOpenAt())
                .closeAt(requestEntity.getCloseAt())
                .dayOfWeek(requestEntity.getDayOfWeek())
                .restaurant(restaurantRepository.findById(requestEntity.getRestaurant().getId())
                        .orElseThrow(() -> new EntityNotFoundException()))
                .build();
    }

    public Header<List<OpeningHourResponse>> readByRestaurantId(Long restaurantId, Pageable pageable) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new EntityNotFoundException("레스토랑을 찾을 수 없습니다."));

        return convertPageToList(getBaseRepository().findByRestaurant(restaurant, pageable));
    }

    @Transactional
    public List<OpeningHourResponse> upsertListByDayOfWeek(Restaurant restaurant, List<OpeningHourRequest> requestedEntities) {
        List<OpeningHour> upsertedEntityList = new ArrayList<>();

        for(OpeningHourRequest requestedEntity : requestedEntities){

            Optional<OpeningHour> needUpsertEntity = getBaseRepository().findByRestaurantAndDayOfWeek(restaurant, requestedEntity.getDayOfWeek());

            if (needUpsertEntity.isPresent()) {
                needUpsertEntity.get().update(requestedEntity);
                upsertedEntityList.add(needUpsertEntity.get());
            } else {
                OpeningHour createdEntity = baseRepository.save(convertBaseEntityFromRequest(requestedEntity));
                upsertedEntityList.add(createdEntity);
            }
        }

        return responseList(upsertedEntityList);
    }

    @Transactional
    public void deleteAllByRestaurant(Restaurant restaurant) {

        List<OpeningHour> openingHourList = getBaseRepository().findAllByRestaurant(restaurant);

        getBaseRepository().deleteAll(openingHourList);

        getBaseRepository().flush();
    }
}
