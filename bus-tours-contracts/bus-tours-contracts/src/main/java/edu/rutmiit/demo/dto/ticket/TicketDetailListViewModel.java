package edu.rutmiit.demo.dto.ticket;


import edu.rutmiit.demo.dto.passenger.PassengerTicketViewModel;
import edu.rutmiit.demo.dto.tour.TourViewModelShort;
import edu.rutmiit.demo.dto.trip.TripViewModel;

import java.time.LocalDate;

public record TicketDetailListViewModel(
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