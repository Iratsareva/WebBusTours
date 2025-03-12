package edu.rutmiit.demo.dto.city;
import edu.rutmiit.demo.dto.base.BaseViewModel;

import java.util.List;

public record CityListViewModel(
        BaseViewModel base,
        List<CityNameViewModel> cityName) {
}
