package edu.rutmiit.demo.dto.bus;
import java.util.List;

import edu.rutmiit.demo.dto.base.BaseViewModel;

public record BusListViewModel(
        BaseViewModel base,
        List<BusViewModel> buses
) {
}
