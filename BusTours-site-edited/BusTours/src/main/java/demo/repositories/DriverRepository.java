package demo.repositories;


import demo.models.Driver;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DriverRepository {
    Driver findDriverByTrip(String tripId);
    Driver create(Driver driver);
    Driver findById(String id);
    List<Driver> findAll();
    Driver update(Driver driver);
    void delete(Driver driver);
}
