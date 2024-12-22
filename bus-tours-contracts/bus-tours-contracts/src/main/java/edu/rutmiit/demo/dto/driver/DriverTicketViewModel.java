package edu.rutmiit.demo.dto.driver;

import java.time.LocalDate;

public record DriverTicketViewModel(
        String id,
        String name,
        String surname,
        String patronymic
) {}