package edu.rutmiit.demo.dto.driver;

import edu.rutmiit.demo.dto.base.BaseViewModel;

public record DriverDetailsViewModel(
        BaseViewModel base,
        DriverViewModel driver
) {
}
