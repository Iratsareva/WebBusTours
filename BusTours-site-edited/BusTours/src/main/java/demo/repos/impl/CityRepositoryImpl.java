package demo.repos.impl;


import demo.models.City;
import demo.repos.CityRepository;
import demo.repos.AbstractRepository;
import org.springframework.stereotype.Repository;

@Repository
public class CityRepositoryImpl extends AbstractRepository<City> implements CityRepository {
}
