package demo.services;


import demo.dto.ticket.TicketCreateDTO;
import demo.dto.ticket.TicketDTO;
import demo.exception.NotFoundException;

import java.util.List;

public interface TicketService {
    TicketDTO getTicketById(String id) throws NotFoundException;
    List<TicketDTO> findAll() throws NotFoundException;
    List<TicketDTO> getTickets() throws NotFoundException;
    List<TicketDTO> getTicketsUser(String id) throws NotFoundException;
    TicketDTO createTicket(TicketCreateDTO ticketCreateDTO) throws NotFoundException;
    void deleteTicket(String id) throws NotFoundException;
    void confirmTicket(String id) throws NotFoundException;
}
