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

import java.util.List;

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

    public void deleteAllByRestaurantId(Long id) {

        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException());

        List<OpeningHour> openingHourList = getBaseRepository().findAllByRestaurant(restaurant);

        getBaseRepository().deleteAll(openingHourList);
    }
}
