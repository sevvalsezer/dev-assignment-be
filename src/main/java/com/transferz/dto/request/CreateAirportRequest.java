package com.transferz.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateAirportRequest {

    private String name;

    private String code;

    private String countryCode;
}