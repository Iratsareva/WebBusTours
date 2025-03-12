package edu.rutmiit.demo.dto.tour;

import edu.rutmiit.demo.dto.base.BaseViewModel;
import java.util.List;
public record TourListViewModelShort(
        BaseViewModel base,
        List<TourViewModelShort> toursShort
) {
}
