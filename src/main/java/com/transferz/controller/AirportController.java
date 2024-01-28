package com.transferz.controller;

import com.transferz.dao.Airport;
import com.transferz.dto.request.CreateAirportRequest;
import com.transferz.dto.response.AirportResponse;
import com.transferz.dto.response.PagedResponse;
import com.transferz.mapper.AirportMapper;
import com.transferz.mapper.PageMapper;
import com.transferz.service.AirportService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/airports")
public class AirportController {

    private final AirportService airportService;
    private final AirportMapper airportMapper;
    private final PageMapper pageMapper;

    @PostMapping
    public AirportResponse create(@RequestBody CreateAirportRequest request) {
        Airport airport = airportService.create(request);

        return airportMapper.toAirportResponse(airport);
    }

    @GetMapping
    private PagedResponse<AirportResponse> getAll(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        Page<Airport> airportPage = airportService.getAll(PageRequest.of(page, size));

        Page<AirportResponse> airportResponsePage = airportPage.map(airportMapper::toAirportResponse);
        return pageMapper.toPagedResponse(airportResponsePage);
    }
}