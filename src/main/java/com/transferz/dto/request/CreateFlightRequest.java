package com.transferz.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateFlightRequest {

    @NotBlank(message = "error.CreateFlightRequest.code.NotBlank")
    @Size(max = 20, message = "error.CreateFlightRequest.code.Size")
    private String code;
}
