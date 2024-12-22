package demo.repos;


import demo.models.Driver;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DriverRepository {
    Driver findDriverByTrip(String tripId);
    Driver create(Driver driver);
    Driver findById(Class<Driver> driverClass, String id);
    List<Driver> getAll(Class<Driver> driverClass);
    Driver update(Driver driver);
    void delete(Driver driver);
}
