package edu.rutmiit.demo.dto.ticket;

import edu.rutmiit.demo.dto.base.BaseViewModel;
import edu.rutmiit.demo.dto.bus.BusTicketViewModel;
import edu.rutmiit.demo.dto.bus.BusViewModel;
import edu.rutmiit.demo.dto.driver.DriverTicketViewModel;
import edu.rutmiit.demo.dto.driver.DriverViewModel;
import edu.rutmiit.demo.dto.passenger.PassengerTicketViewModel;
import edu.rutmiit.demo.dto.passenger.PassengerViewModel;
import edu.rutmiit.demo.dto.tour.TourViewModel;
import edu.rutmiit.demo.dto.tour.TourViewModelShort;
import edu.rutmiit.demo.dto.trip.TripViewModel;

import java.sql.Driver;

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
