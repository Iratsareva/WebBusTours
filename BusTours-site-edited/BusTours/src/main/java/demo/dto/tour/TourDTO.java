package demo.dto.tour;

import java.io.Serializable;

public record TourDTO(
        String id,
        String nameTour,
        String description,
        String itinerary,
        Integer lengthTour,
        Integer price,
        String destination
) implements Serializable {
}
