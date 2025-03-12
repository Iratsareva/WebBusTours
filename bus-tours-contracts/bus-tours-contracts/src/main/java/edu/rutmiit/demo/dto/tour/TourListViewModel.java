package edu.rutmiit.demo.dto.tour;

import edu.rutmiit.demo.dto.base.BaseViewModel;
public record TourListViewModel(
        BaseViewModel base,
        TourViewModel tour,
        String trips
) {
}
