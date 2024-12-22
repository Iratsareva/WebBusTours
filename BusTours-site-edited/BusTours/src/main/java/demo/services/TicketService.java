package demo.services;


import demo.dto.TicketDTO;
import edu.rutmiit.demo.dto.ticket.TicketStatus;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TicketService {
    TicketDTO getTicketById(String id);
    List<TicketDTO> findAll();
    Page<TicketDTO> getTickets(int page, int size);
    Page<TicketDTO> getTicketsUser(int page, int size, String id);
    TicketDTO createTicket(String passengerId, String tripId);
    void deleteTicket(String id);
    void confirmTicket(String id);
}
