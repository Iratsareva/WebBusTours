package edu.rutmiit.demo.dto.passenger;

import edu.rutmiit.demo.dto.base.BaseViewModel;
import edu.rutmiit.demo.dto.driver.DriverViewModel;

import java.util.List;

public record PassengerListViewModel(
        BaseViewModel base,
        List<PassengerViewModel> passengers,
        Integer totalPages
) {}