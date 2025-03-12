package demo.dto.trip;

import java.io.Serializable;
import java.time.LocalDate;

public record TourTripDTO(
        String id,
        String nameTour,
        String destination,
        String itinerary,
        int price,
        LocalDate startDate,
        LocalDate endDate
) implements Serializable {
}
