package edu.rutmiit.demo.dto.trip;

import edu.rutmiit.demo.dto.base.BaseViewModel;
public record TripDetailsViewModel(
        BaseViewModel base,
        TripViewModel trip
) {
}
