package demo.repositories;

import demo.models.Trip;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TripRepository {
    Trip create(Trip trip);
    Trip findById (String id);
    List<Trip> findAll();
    List<Trip> findTripByTour (String idTour);
}
