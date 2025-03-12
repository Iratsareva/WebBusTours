package demo.dto.bus;

import edu.rutmiit.demo.dto.bus.BusStatus;

import java.io.Serializable;
import java.time.LocalDate;

public record BusCreateDto(
        String mark,
        String numberBus,
        Integer numberSeats,
        String classBus,
        LocalDate year
)implements Serializable { }
