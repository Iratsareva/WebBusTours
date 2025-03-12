package demo.dto.bus;

import java.io.Serializable;
import java.time.LocalDate;

public record BusUpdateDto(
        String id,
        String mark,
        String numberBus,
        Integer numberSeats,
        String classBus,
        LocalDate year
)implements Serializable { }
