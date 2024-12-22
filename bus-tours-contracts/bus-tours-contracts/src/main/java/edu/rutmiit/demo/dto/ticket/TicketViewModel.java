package edu.rutmiit.demo.dto.ticket;


public record TicketViewModel(
        String id,
        String passengerId,
        String tripId,
        Integer price,
        TicketStatus ticketStatus
) {
}