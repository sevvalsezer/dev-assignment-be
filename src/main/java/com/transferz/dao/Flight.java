package com.transferz.dao;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Version;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Flight {

    @Id
    private String code;

    private String originAirportCode;

    private String destinationAirportCode;

    private LocalDateTime departureDateTime;

    private LocalDateTime arrivalDateTime;

    private Integer passengerCount;

    @Version
    private Integer version;
}
