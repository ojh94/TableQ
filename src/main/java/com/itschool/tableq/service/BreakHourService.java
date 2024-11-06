package com.itschool.tableq.service;

import com.itschool.tableq.domain.BreakHour;
import com.itschool.tableq.network.Header;
import com.itschool.tableq.network.request.BreakHourRequest;
import com.itschool.tableq.network.response.BreakHourResponse;
import com.itschool.tableq.service.base.BaseService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BreakHourService extends BaseService<BreakHourRequest, BreakHourResponse, BreakHour> {
    @Override
    public Header<List<BreakHourResponse>> getPaginatedList(Pageable pageable) {
        return null;
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
}
