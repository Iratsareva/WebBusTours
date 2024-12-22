package demo.repos.impl;

import demo.models.Passenger;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import demo.models.Ticket;
import demo.repos.AbstractRepository;
import demo.repos.TicketRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TicketRepositoryImpl extends AbstractRepository<Ticket> implements TicketRepository {

    @PersistenceContext
    private EntityManager entityManager;



    @Override
    public List<Ticket> TicketsTrip(String idTrip){
        TypedQuery<Ticket> query = entityManager.createQuery(
                "select ti from Ticket ti " +
                        "join ti.trip tr " +
                        "where tr.id = :id ", Ticket.class);
        return query.setParameter("id", idTrip).getResultList();
    }

    @Override
    public List<Ticket> PassengerTickets(String idPassenger) {

        TypedQuery<Ticket> query = entityManager.createQuery(
                "select ti from Ticket ti " +
                        "join ti.passenger p " +
                        "where p.id = :id ", Ticket.class);

        return query.setParameter("id", idPassenger).getResultList();
    }


}
