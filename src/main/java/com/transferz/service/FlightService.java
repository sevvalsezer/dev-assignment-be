package com.transferz.service;

import com.transferz.configuration.properties.ApplicationProperties;
import com.transferz.dao.Flight;
import com.transferz.dao.Passenger;
import com.transferz.dto.request.AddPassengerRequest;
import com.transferz.dto.request.CreateFlightRequest;
import com.transferz.exception.BadRequestException;
import com.transferz.exception.ConflictException;
import com.transferz.exception.NotFoundException;
import com.transferz.repository.AirportRepository;
import com.transferz.repository.FlightRepository;
import com.transferz.repository.PassengerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FlightService {

    private final FlightRepository flightRepository;
    private final PassengerRepository passengerRepository;
    private final AirportRepository airportRepository;
    private final ApplicationProperties applicationProperties;

    public Flight create(CreateFlightRequest request) {
        var flight = Flight.builder()
                .code(request.getCode())
                .originAirportCode(applicationProperties.getOriginAirportCode())
                .passengerCount(0)
                .build();

        return flightRepository.save(flight);
    }

    @Transactional
    public void addPassenger(String flightCode, AddPassengerRequest request) {
        Optional<Flight> flightOptional = flightRepository.findById(flightCode);

        if (flightOptional.isEmpty()) {
            throw new NotFoundException("Flight");
        }

        Flight flight = flightOptional.get();

        if (flight.getDepartureDateTime() != null) {
            throw new BadRequestException("Flight.Departed");
        }

        var passenger = Passenger.builder()
                .name(request.getName())
                .flightCode(flightCode)
                .build();

        // A way to handle race condition around flight passenger count increment
        // Catch OptimisticLockingFailureException if the flight version has been changed during the request
        try {
            passengerRepository.save(passenger);

            flight.setPassengerCount(flight.getPassengerCount() + 1);
            if (flight.getPassengerCount() == applicationProperties.getPassengerLimitPerFlight()) {
                flight.setDepartureDateTime(LocalDateTime.now());

                String destinationCode = airportRepository.getRandomAirportCodeExceptByCode(
                        applicationProperties.getOriginAirportCode()
                );
                flight.setDestinationAirportCode(destinationCode);
            }

            flightRepository.save(flight);
        } catch (OptimisticLockingFailureException ex) {
            throw new ConflictException("Flight.RequestConflict");
        }
    }
}