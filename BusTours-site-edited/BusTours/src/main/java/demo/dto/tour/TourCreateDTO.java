package demo.dto.tour;

import java.io.Serializable;

public record TourCreateDTO(
        String nameTour,
        String description,
        String itinerary,
        Integer lengthTour,
        Integer price,
        String destination
) implements Serializable {
}
