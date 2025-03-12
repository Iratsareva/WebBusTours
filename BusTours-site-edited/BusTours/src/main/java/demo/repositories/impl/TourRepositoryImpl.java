package demo.repositories.impl;

import demo.models.Tour;
import demo.repositories.AbstractRepository;
import demo.repositories.TourRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class TourRepositoryImpl extends AbstractRepository<Tour> implements TourRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public TourRepositoryImpl() {
        super(Tour.class);
    }

    @Override
    public List<Tour> findTourByDestination(String name_city){
        TypedQuery<Tour> query = entityManager.createQuery(
                "select t from Tour t " +
                        "join t.destination d "+
                        "where d.nameCity = :name_city", Tour.class);
        return query.setParameter("name_city", name_city).getResultList();
    }

    @Override
    public Tour findTourByTrip (String idTrip){
        TypedQuery<Tour> query = entityManager.createQuery(
                "select t from Tour t " +
                        "join t.trips tr " +
                        "where tr.id = :id", Tour.class);
        return query.setParameter("id", idTrip).getSingleResult();
    }

    @Override
    public List<Tour> findTourByDestinationAndStartDateAndLengthTour(String destination, LocalDate startDate, Integer lengthTour){
        StringBuilder queryString = new StringBuilder("select t from Tour t " +
                "join t.destination d " +
                "join t.trips tr where 1=1");
        if (destination != null && !destination.isEmpty()) {
            queryString.append(" and LOWER(d.nameCity) = LOWER(:destination)");
        }
        if (startDate != null) {
            queryString.append(" and tr.startDate = :startDate");
        }
        if (lengthTour != null) {
            queryString.append(" and t.lengthTour >= :lengthTour");
        }

        TypedQuery<Tour> query = entityManager.createQuery(queryString.toString(), Tour.class);

        if (destination != null && !destination.isEmpty()) {
            query.setParameter("destination", destination);
        }
        if (startDate != null) {query.setParameter("startDate", startDate);
        }
        if (lengthTour != null) {
            query.setParameter("lengthTour", lengthTour);
        }

        return query.getResultList();
    }
}
