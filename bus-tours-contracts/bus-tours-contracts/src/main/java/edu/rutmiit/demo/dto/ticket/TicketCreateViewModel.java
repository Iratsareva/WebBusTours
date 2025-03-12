package edu.rutmiit.demo.dto.ticket;

import edu.rutmiit.demo.dto.base.BaseViewModel;
import edu.rutmiit.demo.dto.tour.TourViewModel;
import edu.rutmiit.demo.dto.trip.TripViewModel;

import java.util.List;

public record TicketCreateViewModel(
    BaseViewModel base,
    TourViewModel  tour,
    List<TripViewModel> trips
    ) {
}
