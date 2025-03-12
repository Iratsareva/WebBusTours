package demo.dto.bus;

import edu.rutmiit.demo.dto.bus.BusStatus;

import java.io.Serializable;
import java.time.LocalDate;

public record BusDto (
        String id,
        String mark,
        String numberBus,
        Integer numberSeats,
        String classBus,
        LocalDate year,
        BusStatus busStatus
)implements Serializable { }
