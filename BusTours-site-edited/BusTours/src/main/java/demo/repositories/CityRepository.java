package demo.repositories;

import demo.models.City;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CityRepository {
    City create(City city);
    City findById( String id);
    List<City> findAll();
    City update(City city);
    void delete(City city);
}
