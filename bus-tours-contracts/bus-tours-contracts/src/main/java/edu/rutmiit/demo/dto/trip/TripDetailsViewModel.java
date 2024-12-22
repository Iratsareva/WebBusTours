package edu.rutmiit.demo.dto.trip;

import edu.rutmiit.demo.dto.base.BaseViewModel;
import edu.rutmiit.demo.dto.driver.DriverViewModel;

public record TripDetailsViewModel(
        BaseViewModel base,
        TripViewModel trip
) {
}
