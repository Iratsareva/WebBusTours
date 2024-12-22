package demo.repos;

import demo.models.City;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CityRepository {
    City create(City city);
    City findById(Class<City> cityClass, String id);
    List<City> getAll(Class<City> cityClass);
    City update(City city);
    void delete(City city);
}
