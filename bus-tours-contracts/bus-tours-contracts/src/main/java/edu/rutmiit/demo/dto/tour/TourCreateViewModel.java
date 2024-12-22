package edu.rutmiit.demo.dto.tour;


import edu.rutmiit.demo.dto.base.BaseViewModel;
import edu.rutmiit.demo.dto.city.CityViewModel;

import java.util.List;

public record TourCreateViewModel(
    BaseViewModel base,
    List<CityViewModel> cities
) {
}
