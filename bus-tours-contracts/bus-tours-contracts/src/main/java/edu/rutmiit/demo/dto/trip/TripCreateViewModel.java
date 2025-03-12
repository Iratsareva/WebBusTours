package edu.rutmiit.demo.dto.trip;

import edu.rutmiit.demo.dto.base.BaseViewModel;
import edu.rutmiit.demo.dto.bus.BusViewModel;
import edu.rutmiit.demo.dto.driver.DriverViewModel;
import edu.rutmiit.demo.dto.tour.TourViewModel;

import java.util.List;

public record TripCreateViewModel(
    BaseViewModel base,
    TourViewModel tour,
    List<DriverViewModel> drivers,
    List<BusViewModel> buses
    ) {
}
