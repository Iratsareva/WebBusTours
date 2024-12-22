package edu.rutmiit.demo.dto.bus;

import java.time.LocalDate;

public record BusTicketViewModel(
        String id,
        String mark,
        String numberBus
) {
}
