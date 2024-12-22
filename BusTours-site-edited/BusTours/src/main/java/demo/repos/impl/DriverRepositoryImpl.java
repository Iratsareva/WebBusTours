package demo.repos.impl;

import jakarta.persistence.TypedQuery;
import demo.models.Driver;
import demo.repos.AbstractRepository;
import demo.repos.DriverRepository;
import org.springframework.stereotype.Repository;

@Repository
public class DriverRepositoryImpl extends AbstractRepository<Driver> implements DriverRepository {

    public Driver findDriverByTrip(String tripId) {
        TypedQuery<Driver> query = entityManager.createQuery(
                "select d from Driver d " +
                        "join d.trips tr " +
                        "where tr.id = :id", Driver.class);
        return query.setParameter("id", tripId).getSingleResult();
    }
}
