package demo.dto.ticket;

import edu.rutmiit.demo.dto.ticket.TicketStatus;

import java.io.Serializable;

public record TicketDTO(
        String id,
        String passengerId,
        String tripId,
        int price,
        TicketStatus ticketStatus
) implements Serializable {
}
