package demo.repositories.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import demo.models.Trip;
import demo.repositories.AbstractRepository;
import demo.repositories.TripRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class TripRepositoryImpl extends AbstractRepository<Trip> implements TripRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public TripRepositoryImpl() {
        super(Trip.class);
    }

    @Override
    public List<Trip> findTripByTour (String idTour){
        TypedQuery<Trip> query = entityManager.createQuery(
                "select tr from Trip tr " +
                        "join tr.tour tour " +
                        "where tour.id = :id" , Trip.class);
        return query.setParameter("id", idTour).getResultList();
    }
}
