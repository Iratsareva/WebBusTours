package demo.dto.ticket;

import java.io.Serializable;

public record TicketCreateDTO(
        String passengerId,
        String tripId
) implements Serializable {
}
