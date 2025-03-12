package edu.rutmiit.demo.dto.bus;

import edu.rutmiit.demo.dto.base.BaseViewModel;

public record BusDetailsViewModel(
        BaseViewModel base,
        BusViewModel bus
) {
}
