package demo.repos;

import demo.models.Passenger;
import demo.models.Ticket;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository {
    Ticket create(Ticket ticket);
    Ticket findById(Class<Ticket> ticketClass, String id);
    List<Ticket> getAll(Class<Ticket> ticketClass);

    List<Ticket> TicketsTrip(String idTrip);

    List<Ticket> PassengerTickets(String idPassenger);
}
