package demo.repositories;

import demo.models.Ticket;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TicketRepository {
    Ticket create(Ticket ticket);
    Ticket findById(String id);
    List<Ticket> findAll();
    List<Ticket> TicketsTrip(String idTrip);
    List<Ticket> PassengerTickets(String idPassenger);
}