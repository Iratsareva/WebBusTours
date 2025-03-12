package demo.dto.driver;

import edu.rutmiit.demo.dto.driver.DriverStatus;

import java.io.Serializable;
import java.time.LocalDate;

public record DriverCreateDTO(
        String name,
        String surname,
        String patronymic,
        LocalDate birthday,
        String passport,
        String driverLicense,
        LocalDate dateStartDriverLicense,
        String category
) implements Serializable {
}
