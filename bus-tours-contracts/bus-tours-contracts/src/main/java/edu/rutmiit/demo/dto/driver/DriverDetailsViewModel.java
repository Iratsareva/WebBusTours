package edu.rutmiit.demo.dto.driver;

import edu.rutmiit.demo.dto.base.BaseViewModel;
import edu.rutmiit.demo.dto.bus.BusViewModel;

public record DriverDetailsViewModel(
        BaseViewModel base,
        DriverViewModel driver
) {
}
