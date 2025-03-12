package edu.rutmiit.demo.dto.tour;


public record TourViewModel(
        String id,
        String nameTour,
        String description,
        String itinerary,
        Integer lengthTour,
        Integer price,
        String destination
) {}