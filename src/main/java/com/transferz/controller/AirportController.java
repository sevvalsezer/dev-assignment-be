package com.transferz.controller;

import com.transferz.dao.Airport;
import com.transferz.dto.request.CreateAirportRequest;
import com.transferz.dto.request.FindAirportRequest;
import com.transferz.dto.response.AirportResponse;
import com.transferz.dto.response.PagedResponse;
import com.transferz.mapper.AirportMapper;
import com.transferz.mapper.PageMapper;
import com.transferz.service.AirportService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/airports")
public class AirportController {

    private final AirportService airportService;
    private final AirportMapper airportMapper;
    private final PageMapper pageMapper;

    @PostMapping
    public AirportResponse create(@RequestBody @Valid CreateAirportRequest request) {
        Airport airport = airportService.create(request);

        return airportMapper.toAirportResponse(airport);
    }

    @GetMapping
    private PagedResponse<AirportResponse> findAll(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String countryCode
    ) {
        FindAirportRequest findAirportRequest = FindAirportRequest.builder()
                .code(code)
                .name(name)
                .countryCode(countryCode)
                .build();
        PageRequest pageRequest = PageRequest.of(page, size);

        Page<Airport> airportPage = airportService.findAll(findAirportRequest, pageRequest);

        Page<AirportResponse> airportResponsePage = airportPage.map(airportMapper::toAirportResponse);
        return pageMapper.toPagedResponse(airportResponsePage);
    }
}