package com.transferz.service;

import com.transferz.dao.Airport;
import com.transferz.dto.request.CreateAirportRequest;
import com.transferz.dto.request.FindAirportRequest;
import com.transferz.repository.AirportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AirportService {

    private final AirportRepository airportRepository;

    public Airport create(CreateAirportRequest request) {
        var airport = Airport.builder()
                .name(request.getName())
                .code(request.getCode())
                .countryCode(request.getCountryCode())
                .build();

        return airportRepository.save(airport);
    }

    public Page<Airport> findAll(FindAirportRequest request, Pageable pageable) {
        return airportRepository.findAll(request, pageable);
    }
}
