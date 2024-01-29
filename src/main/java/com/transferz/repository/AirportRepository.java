package com.transferz.repository;

import com.transferz.dao.Airport;
import com.transferz.dto.request.FindAirportRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AirportRepository extends JpaRepository<Airport, String> {

    // If the parameter is null, then skip the criteria for it
    // Otherwise, include in the criteria
    @Query(
        """
            FROM Airport as a
            WHERE (:#{#request.code} IS NULL OR a.code = :#{#request.code}) AND 
                  (:#{#request.name} IS NULL OR a.name = :#{#request.name}) AND 
                  (:#{#request.countryCode} IS NULL OR a.countryCode = :#{#request.countryCode})               
        """
    )
    Page<Airport> findAll(FindAirportRequest request, Pageable pageable);
}
