package demo.repos.impl;

import edu.rutmiit.demo.dto.ticket.TicketStatus;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import demo.models.Passenger;
import demo.repos.AbstractRepository;
import demo.repos.PassengerRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PassengerRepositoryImpl extends AbstractRepository<Passenger> implements PassengerRepository {
    @Override
    public Passenger authorisation(String login, String password) {
        TypedQuery<Passenger> query = entityManager.createQuery(
                "select p from Passenger p "+
                        "where p.login = :login " +
                        "and p.password = :password", Passenger.class);
        return query.setParameter("login", login).setParameter("password", password).getSingleResult();
    }


    @Override
    public Passenger findByLogin(String login) {
        TypedQuery<Passenger> query = entityManager.createQuery(
                "select p from Passenger p "+
                        "where p.login = :login", Passenger.class);
        return query.setParameter("login", login).getSingleResult();
    }

    @Override
    public Passenger findByEmail(String email) {
        TypedQuery<Passenger> query = entityManager.createQuery(
                "select p from Passenger p "+
                        "where p.email = :email", Passenger.class)
                .setParameter("email", email);

        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<Passenger> PassengersTrip(String idTrip){
        TypedQuery<Passenger> query = entityManager.createQuery(
                "select ti.passenger from Ticket ti " +
                        "join ti.trip t " +
                        "where t.id = :idTrip " +
                        "and ti.ticketStatus = :ticketStatus ", Passenger.class);

        return query.setParameter("idTrip", idTrip).setParameter("ticketStatus", TicketStatus.CONFIRMED).getResultList();

    }
}
