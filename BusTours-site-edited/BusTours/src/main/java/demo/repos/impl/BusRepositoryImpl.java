package demo.repos.impl;

import demo.models.Bus;
import demo.repos.AbstractRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import jakarta.persistence.TypedQuery;
import demo.repos.BusRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BusRepositoryImpl extends AbstractRepository<Bus> implements BusRepository {
    @PersistenceContext
    private EntityManager entityManager;
    @Override
    public Bus findBusByTrip (String idTrip){
        TypedQuery<Bus> query = entityManager.createQuery(
                "select b from Bus b " +
                        "join b.trips t " +
                        "where t.id = :id", Bus.class);
        return query.setParameter("id", idTrip).getSingleResult();
    }
}
