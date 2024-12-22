package edu.rutmiit.demo.dto.city;


public record CityToursViewModel(
        String id,
        String nameTour,
        String itinerary,
        Integer lengthTour,
        Integer price
) {}