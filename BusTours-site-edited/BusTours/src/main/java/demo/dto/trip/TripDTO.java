package demo.dto.trip;

import edu.rutmiit.demo.dto.trip.TripStatus;

import java.io.Serializable;
import java.time.LocalDate;

public record TripDTO (
        String id,
        String tourId,
        String busId,
        String driverId,
        LocalDate startDate,
        LocalDate endDate,
        TripStatus tripStatus
)implements Serializable {
}
