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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.OptimisticLockingFailureException;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class FlightServiceTest {

    @InjectMocks
    FlightService flightService;

    @Mock
    FlightRepository flightRepository;

    @Mock
    PassengerRepository passengerRepository;

    @Mock
    AirportRepository airportRepository;

    @Mock
    ApplicationProperties applicationProperties;

    @Test
    public void should_create_flight() {
        // Given
        CreateFlightRequest request = new CreateFlightRequest();
        request.setCode("SB-9051");

        String originAirportCode = "OAC";

        Flight flight = Flight.builder()
            .code(request.getCode())
            .originAirportCode(originAirportCode)
            .passengerCount(0)
            .build();

        Flight expected = Flight.builder()
                .code(request.getCode())
                .originAirportCode(originAirportCode)
                .passengerCount(0)
                .version(0)
                .build();

        Mockito.when(applicationProperties.getOriginAirportCode()).thenReturn(originAirportCode);
        Mockito.when(flightRepository.save(flight)).thenReturn(expected);

        // When
        Flight actual = flightService.create(request);

        // Then
        Mockito.verify(flightRepository).save(flight);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void should_add_passenger_to_flight() {
        // Given
        String flightCode = "SB-9051";
        String originAirportCode = "OAC";
        AddPassengerRequest request = new AddPassengerRequest();
        request.setName("Passenger Name");

        Flight flight = Flight.builder()
                .code(flightCode)
                .originAirportCode(originAirportCode)
                .passengerCount(0)
                .build();

        Passenger passenger = Passenger.builder()
                .name(request.getName())
                .flightCode(flightCode)
                .build();

        flight.setPassengerCount(1);

        Mockito.when(flightRepository.findById(flightCode)).thenReturn(Optional.of(flight));
        Mockito.when(passengerRepository.save(passenger)).thenReturn(Mockito.mock(Passenger.class));
        Mockito.when(applicationProperties.getPassengerLimitPerFlight()).thenReturn(150);
        Mockito.when(flightRepository.save(flight)).thenReturn(Mockito.mock(Flight.class));


        // When
        flightService.addPassenger(flightCode, request);

        // Then
        Mockito.verify(passengerRepository).save(passenger);
        Mockito.verify(flightRepository).save(flight);
        Mockito.verify(airportRepository, Mockito.never()).getRandomAirportCodeExceptByCode(Mockito.any());
    }

    @Test
    public void should_throw_exception_if_flight_not_found() {
        // Given
        String flightCode = "SB-9051";
        AddPassengerRequest request = new AddPassengerRequest();

        Mockito.when(flightRepository.findById(flightCode)).thenReturn(Optional.empty());

        // When - Then
        Assertions.assertThrows(NotFoundException.class, () -> flightService.addPassenger(flightCode, request));
    }

    @Test
    public void should_throw_exception_if_flight_already_departed() {
        // Given
        String flightCode = "SB-9051";
        String originAirportCode = "OAC";
        AddPassengerRequest request = new AddPassengerRequest();
        request.setName("Passenger Name");

        Flight flight = Flight.builder()
                .code(flightCode)
                .originAirportCode(originAirportCode)
                .departureDateTime(LocalDateTime.now())
                .passengerCount(150)
                .build();

        Mockito.when(flightRepository.findById(flightCode)).thenReturn(Optional.of(flight));

        // When - Then
        Assertions.assertThrows(BadRequestException.class, () -> flightService.addPassenger(flightCode, request));
    }

    @Test
    public void should_depart_flight_if_paasenger_count_reached_limit() {
        // Given
        String flightCode = "SB-9051";
        String originAirportCode = "OAC";
        String destinationAirportCode = "DAC";
        AddPassengerRequest request = new AddPassengerRequest();
        request.setName("Passenger Name");

        Flight flight = Flight.builder()
                .code(flightCode)
                .originAirportCode(originAirportCode)
                .passengerCount(149)
                .build();

        Passenger passenger = Passenger.builder()
                .name(request.getName())
                .flightCode(flightCode)
                .build();

        String instantExpected = "2024-01-01T00:00:00Z";
        Clock clock = Clock.fixed(Instant.parse(instantExpected), ZoneId.of("UTC"));
        LocalDateTime localDateTime = LocalDateTime.now(clock);

        Flight modifiedFlight = Flight.builder()
                .code(flightCode)
                .originAirportCode(originAirportCode)
                .passengerCount(150)
                .departureDateTime(localDateTime)
                .destinationAirportCode(destinationAirportCode)
                .build();

        Mockito.when(flightRepository.findById(flightCode)).thenReturn(Optional.of(flight));
        Mockito.when(passengerRepository.save(passenger)).thenReturn(Mockito.mock(Passenger.class));
        Mockito.when(applicationProperties.getPassengerLimitPerFlight()).thenReturn(150);
        Mockito.when(applicationProperties.getOriginAirportCode()).thenReturn(originAirportCode);
        Mockito.when(airportRepository.getRandomAirportCodeExceptByCode(originAirportCode)).thenReturn(destinationAirportCode);
        Mockito.when(flightRepository.save(flight)).thenReturn(Mockito.mock(Flight.class));


        // When
        try (MockedStatic<LocalDateTime> localDateTimeMockedStatic = Mockito.mockStatic(LocalDateTime.class)) {
            localDateTimeMockedStatic.when(LocalDateTime::now).thenReturn(localDateTime);
            flightService.addPassenger(flightCode, request);
        }

        // Then
        Mockito.verify(passengerRepository).save(passenger);
        Mockito.verify(flightRepository).save(flight);
        Mockito.verify(airportRepository).getRandomAirportCodeExceptByCode(originAirportCode);
        Mockito.verify(flightRepository).save(modifiedFlight);
    }

    @Test
    public void should_throw_exception_if_flight_modified_by_parallel_request() {
        // Given
        String flightCode = "SB-9051";
        String originAirportCode = "OAC";
        String destinationAirportCode = "DAC";
        AddPassengerRequest request = new AddPassengerRequest();
        request.setName("Passenger Name");

        Flight flight = Flight.builder()
                .code(flightCode)
                .originAirportCode(originAirportCode)
                .passengerCount(149)
                .build();

        Passenger passenger = Passenger.builder()
                .name(request.getName())
                .flightCode(flightCode)
                .build();

        String instantExpected = "2024-01-01T00:00:00Z";
        Clock clock = Clock.fixed(Instant.parse(instantExpected), ZoneId.of("UTC"));
        LocalDateTime localDateTime = LocalDateTime.now(clock);

        Flight modifiedFlight = Flight.builder()
                .code(flightCode)
                .originAirportCode(originAirportCode)
                .passengerCount(150)
                .departureDateTime(localDateTime)
                .destinationAirportCode(destinationAirportCode)
                .build();

        Mockito.when(flightRepository.findById(flightCode)).thenReturn(Optional.of(flight));
        Mockito.when(passengerRepository.save(passenger)).thenReturn(Mockito.mock(Passenger.class));
        Mockito.when(applicationProperties.getPassengerLimitPerFlight()).thenReturn(150);
        Mockito.when(applicationProperties.getOriginAirportCode()).thenReturn(originAirportCode);
        Mockito.when(airportRepository.getRandomAirportCodeExceptByCode(originAirportCode)).thenReturn(destinationAirportCode);
        Mockito.when(flightRepository.save(modifiedFlight)).thenThrow(new OptimisticLockingFailureException(""));


        // When - Then
        Assertions.assertThrows(ConflictException.class, () -> {
            try (MockedStatic<LocalDateTime> localDateTimeMockedStatic = Mockito.mockStatic(LocalDateTime.class)) {
                localDateTimeMockedStatic.when(LocalDateTime::now).thenReturn(localDateTime);
                flightService.addPassenger(flightCode, request);
            }
        });
    }
}
