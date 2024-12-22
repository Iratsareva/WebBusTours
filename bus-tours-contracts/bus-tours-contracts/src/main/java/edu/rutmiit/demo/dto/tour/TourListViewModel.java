package edu.rutmiit.demo.dto.tour;

import edu.rutmiit.demo.dto.base.BaseViewModel;
import edu.rutmiit.demo.dto.city.CityViewModel;

public record TourListViewModel(
        BaseViewModel base,
        TourViewModel tour,
        String trips
) {


}
