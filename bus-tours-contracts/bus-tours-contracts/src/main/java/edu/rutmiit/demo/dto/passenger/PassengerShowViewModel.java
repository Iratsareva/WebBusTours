package edu.rutmiit.demo.dto.passenger;

import edu.rutmiit.demo.dto.base.BaseViewModel;

public record PassengerShowViewModel(
        BaseViewModel base,
        PassengerViewModel passenger
) {
}
