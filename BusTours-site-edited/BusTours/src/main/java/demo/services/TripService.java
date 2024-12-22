package demo.services;

import edu.rutmiit.demo.dto.trip.TripStatus;
import demo.dto.TourTripDTO;
import demo.dto.TripDTO;
import org.springframework.data.domain.Page;


import java.time.LocalDate;
import java.util.List;

public interface TripService {
    TripDTO createTrip (
    String tourId,
    String busId,
    String driverId,
    LocalDate startDate,
    LocalDate endDate,
    TripStatus tripStatus
    );
    TripDTO getTripById (String id);
    List<TripDTO> findAll();

    void updateTrip(String id,
                       String tourId,
                       String busId,
                       String driverId,
                       LocalDate startDate,
                       LocalDate endDate,
                       TripStatus tripStatus);

    Page<TourTripDTO> findTourByDateTrip();

    Page<TripDTO> getTrips(int page, int size);

    List<TripDTO> findTripByTour(String id);

    String findTripByTourString(String id);
}
