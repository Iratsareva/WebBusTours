package edu.rutmiit.demo.dto.tourTrip;


import edu.rutmiit.demo.dto.trip.TripStatus;

import java.time.LocalDate;

public record TourTripViewModel(
        String id,
        String nameTour,
        String destination,
        String itinerary,
        int price,
        String startDate,
        String endDate
) {
}