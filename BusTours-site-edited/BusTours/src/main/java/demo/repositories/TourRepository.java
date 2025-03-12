package demo.repositories;

import demo.models.Tour;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TourRepository {
    Tour create(Tour tour);
    Tour findById(String id);
    List<Tour> findAll();
    List<Tour> findTourByDestination(String destination );
    Tour findTourByTrip (String idTrip);
    List<Tour> findTourByDestinationAndStartDateAndLengthTour(String destination, LocalDate startDate, Integer lengthTour);
}
