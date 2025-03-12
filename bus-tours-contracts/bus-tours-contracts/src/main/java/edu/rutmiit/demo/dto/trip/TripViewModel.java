package edu.rutmiit.demo.dto.trip;


import java.time.LocalDate;

public record TripViewModel(
        String id,
        String tourId,
        String busId,
        String driverId,
        LocalDate startDate,
        LocalDate endDate,
        TripStatus tripStatus
) {
}