package demo.repos;

import demo.models.Tour;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TourRepository {


    Tour create(Tour tour);
    Tour findById(Class<Tour> tourClass, String id);
    List<Tour> getAll(Class<Tour> tourClass);
    List<Tour> findTourByDestination(String destination );
    Tour findTourByTrip (String idTrip);

    List<Tour> findTourByDestinationAndStartDateAndLengthTour(String destination,
                                                              LocalDate startDate,
                                                              Integer lengthTour
    );





}
