package edu.rutmiit.demo.dto.tourTrip;

import edu.rutmiit.demo.dto.base.BaseViewModel;
import java.util.List;

public record TourTripListViewModel(
        BaseViewModel base,
        List<TourTripViewModel> tourTrip
) {}