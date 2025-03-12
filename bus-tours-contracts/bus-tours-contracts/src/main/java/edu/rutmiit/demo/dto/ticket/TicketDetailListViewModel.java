package edu.rutmiit.demo.dto.ticket;

import java.time.LocalDate;

public record TicketDetailListViewModel  (
        String id,
        Integer price,
        TicketStatus ticketStatus,

        String passengerName,
        String passengerSurname,
        String passengerPatronymic,

        String tripId,
        LocalDate startDate,
        LocalDate endDate,

        String tourName
) {
}