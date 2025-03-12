package edu.rutmiit.demo.dto.tour;


public record TourViewModelShort(
        String id,
        String nameTour,
        String itinerary,
        Integer lengthTour,
        Integer price,
        String trips
) {}