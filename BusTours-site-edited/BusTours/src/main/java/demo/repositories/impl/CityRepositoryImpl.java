package demo.repositories.impl;


import demo.models.City;
import demo.repositories.CityRepository;
import demo.repositories.AbstractRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class CityRepositoryImpl extends AbstractRepository<City> implements CityRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public CityRepositoryImpl() {
        super(City.class);
    }
}
