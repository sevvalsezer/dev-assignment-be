package com.transferz.controller;

import com.transferz.dao.Flight;
import com.transferz.dto.request.AddPassengerRequest;
import com.transferz.dto.request.CreateFlightRequest;
import com.transferz.service.FlightService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/flights")
public class FlightController {

    private final FlightService flightService;

    @PostMapping
    public Flight create(@RequestBody @Valid CreateFlightRequest request) {
        return flightService.create(request);
    }

    @PostMapping("/{flightCode}/add-passenger")
    public void addPassenger(
            @PathVariable String flightCode,
            @RequestBody @Valid AddPassengerRequest request
    ) {
        flightService.addPassenger(flightCode, request);
    }
}
