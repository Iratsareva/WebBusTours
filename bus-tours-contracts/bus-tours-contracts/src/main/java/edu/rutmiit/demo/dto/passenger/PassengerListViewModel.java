package edu.rutmiit.demo.dto.passenger;

import edu.rutmiit.demo.dto.base.BaseViewModel;
import java.util.List;

public record PassengerListViewModel(
        BaseViewModel base,
        List<PassengerViewModel> passengers
) {}