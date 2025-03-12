package demo.repositories.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import demo.models.Driver;
import demo.repositories.AbstractRepository;
import demo.repositories.DriverRepository;
import org.springframework.stereotype.Repository;

@Repository
public class DriverRepositoryImpl extends AbstractRepository<Driver> implements DriverRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public DriverRepositoryImpl() {
        super(Driver.class);
    }

    public Driver findDriverByTrip(String tripId) {
        TypedQuery<Driver> query = entityManager.createQuery(
                "select d from Driver d " +
                        "join d.trips tr " +
                        "where tr.id = :id", Driver.class);
        return query.setParameter("id", tripId).getSingleResult();
    }
}
