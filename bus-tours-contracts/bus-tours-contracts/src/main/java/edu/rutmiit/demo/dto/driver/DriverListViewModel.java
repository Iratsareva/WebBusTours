package edu.rutmiit.demo.dto.driver;

import edu.rutmiit.demo.dto.base.BaseViewModel;
import java.util.List;

public record DriverListViewModel(
        BaseViewModel base,
        List<DriverViewModel> drivers
) {}