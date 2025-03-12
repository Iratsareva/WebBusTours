package edu.rutmiit.demo.dto.driver;

import java.time.LocalDate;

public record DriverViewModel(
        String id,
        String name,
        String surname,
        String patronymic,
        LocalDate birthday,
        String passport,
        String driverLicense,
        LocalDate dateStartDriverLicense,
        String category,
        DriverStatus driverStatus
) {}