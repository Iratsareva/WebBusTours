package edu.rutmiit.demo.dto.bus;

import java.time.LocalDate;

public record BusViewModel(
        String id,
        String mark,
        String numberBus,
        Integer numberSeats,
        String classBus,
        LocalDate year,
        BusStatus busStatus
) {
}
