package edu.rutmiit.demo.dto.tour;

import edu.rutmiit.demo.dto.base.BaseViewModel;
import edu.rutmiit.demo.dto.trip.TripViewModel;

import java.util.List;

public record TourRegTicketViewModel (
        BaseViewModel base,
        TourViewModel tour,
        List<TripViewModel>trips
){
}
