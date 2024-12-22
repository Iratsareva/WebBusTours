package demo.repos;

import demo.models.Trip;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TripRepository {

    Trip create(Trip trip);
    Trip findById (Class<Trip> tripClass, String id);
    List<Trip> getAll(Class<Trip> tripClass);
    List<Trip> findTripByTour (String idTour);

}
