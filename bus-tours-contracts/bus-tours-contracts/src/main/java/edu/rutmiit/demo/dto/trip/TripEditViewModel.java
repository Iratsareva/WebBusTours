package edu.rutmiit.demo.dto.trip;

import edu.rutmiit.demo.dto.base.BaseViewModel;
import edu.rutmiit.demo.dto.bus.BusViewModel;
import edu.rutmiit.demo.dto.driver.DriverViewModel;

import java.util.List;

public record TripEditViewModel(
    BaseViewModel base,
    List<DriverViewModel> drivers,
    List<BusViewModel> buses
    ) {
}
