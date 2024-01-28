package com.transferz.mapper;

import com.transferz.dao.Airport;
import com.transferz.dto.response.AirportResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface AirportMapper {

    AirportResponse toAirportResponse(Airport airport);
}
