package edu.rutmiit.demo.dto.ticket;

import edu.rutmiit.demo.dto.base.BaseViewModel;
import edu.rutmiit.demo.dto.bus.BusTicketViewModel;
import edu.rutmiit.demo.dto.driver.DriverTicketViewModel;
import edu.rutmiit.demo.dto.passenger.PassengerTicketViewModel;
import edu.rutmiit.demo.dto.tour.TourViewModelShort;
import edu.rutmiit.demo.dto.trip.TripViewModel;

public record TicketDetailsViewModel(
        BaseViewModel base,
        TicketViewModel ticket,
        PassengerTicketViewModel passenger,
        TripViewModel trip,
        DriverTicketViewModel driver,
        BusTicketViewModel bus,
        TourViewModelShort tour
) {
}
