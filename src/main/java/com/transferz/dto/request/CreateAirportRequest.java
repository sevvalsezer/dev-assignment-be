package com.transferz.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateAirportRequest {

    @NotBlank(message = "error.CreateAirportRequest.name.NotBlank")
    @Size(max = 255, message = "error.CreateAirportRequest.name.Size")
    private String name;

    @NotBlank(message = "error.CreateAirportRequest.code.NotBlank")
    @Size(min = 3, max = 4, message = "error.CreateAirportRequest.code.Size")
    @Pattern(regexp = "^[a-zA-Z]*$", message = "error.CreateAirportRequest.code.LettersOnly")
    private String code;

    @NotBlank(message = "error.CreateAirportRequest.countryCode.NotBlank")
    @Size(min = 2, max = 2, message = "error.CreateAirportRequest.countryCode.Size")
    @Pattern(regexp = "^[a-zA-Z]*$", message = "error.CreateAirportRequest.countryCode.LettersOnly")
    private String countryCode;
}