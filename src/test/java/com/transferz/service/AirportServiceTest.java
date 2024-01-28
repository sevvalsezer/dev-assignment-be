package com.transferz.service;

import com.transferz.dao.Airport;
import com.transferz.dto.request.CreateAirportRequest;
import com.transferz.repository.AirportRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class AirportServiceTest {

    @InjectMocks
    AirportService airportService;

    @Mock
    AirportRepository airportRepository;

    @Test
    public void should_create_airport() {
        // Given
        CreateAirportRequest request = new CreateAirportRequest();

        request.setName("Amsterdam Airport Schiphol");
        request.setCode("AMS");
        request.setCountryCode("NL");

        Airport airport = Airport.builder()
                .name(request.getName())
                .code(request.getCode())
                .countryCode(request.getCountryCode())
                .build();

        Airport expected = Airport.builder()
                .name(request.getName())
                .code(request.getCode())
                .countryCode(request.getCountryCode())
                .build();

        Mockito.when(airportRepository.save(airport)).thenReturn(expected);

        // When
        Airport actual = airportService.create(request);

        // Then
        Mockito.verify(airportRepository).save(airport);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void should_get_all_airports() {
        // Given
        Pageable pageable = PageRequest.of(0, 50);

        Airport airport1 = Airport.builder()
                .name("Amsterdam Airport Schiphol")
                .code("AMS")
                .countryCode("NL")
                .build();

        Airport airport2 = Airport.builder()
                .name("Venice Marco Polo Airport")
                .code("VCE")
                .countryCode("IT")
                .build();

        List<Airport> airportList = new ArrayList<>();
        airportList.add(airport1);
        airportList.add(airport2);

        Page<Airport> expected = new PageImpl<>(airportList, pageable, 2);

        Mockito.when(airportRepository.findAll(pageable)).thenReturn(expected);

        // When
        Page<Airport> actual = airportService.getAll(pageable);

        // Then
        Mockito.verify(airportRepository).findAll(pageable);
        Assertions.assertEquals(expected, actual);
    }
}
