package demo.repositories.impl;

import demo.models.Bus;
import demo.repositories.AbstractRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import demo.repositories.BusRepository;
import org.springframework.stereotype.Repository;


@Repository
public class BusRepositoryImpl extends AbstractRepository<Bus> implements BusRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public BusRepositoryImpl() {
        super(Bus.class);
    }

    @Override
    public Bus findBusByTrip (String idTrip){
        TypedQuery<Bus> query = entityManager.createQuery(
                "select b from Bus b " +
                        "join b.trips t " +
                        "where t.id = :id", Bus.class);
        return query.setParameter("id", idTrip).getSingleResult();
    }
}
