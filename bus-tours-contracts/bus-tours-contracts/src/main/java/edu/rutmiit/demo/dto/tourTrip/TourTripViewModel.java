package edu.rutmiit.demo.dto.tourTrip;

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