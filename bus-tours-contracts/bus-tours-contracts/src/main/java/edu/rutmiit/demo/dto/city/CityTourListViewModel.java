package edu.rutmiit.demo.dto.city;


import edu.rutmiit.demo.dto.base.BaseViewModel;

import java.util.List;

public record CityTourListViewModel(
      BaseViewModel base,
      CityViewModel city,
      List<CityToursViewModel> cityTours
) {
}
