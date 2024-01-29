package com.transferz.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddPassengerRequest {

    @NotBlank(message = "error.AddPassengerRequest.name.NotBlank")
    @Size(max = 1024, message = "error.AddPassengerRequest.name.Size")
    private String name;
}
