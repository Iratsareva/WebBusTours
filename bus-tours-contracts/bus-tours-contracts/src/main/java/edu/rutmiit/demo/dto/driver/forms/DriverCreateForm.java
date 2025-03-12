package edu.rutmiit.demo.dto.driver.forms;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record DriverCreateForm(
        @NotBlank(message = "Имя обязательно")
        String name,

        @NotBlank(message = "Фамилия обязательно")
        String surname,

        String patronymic,

        @NotNull(message = "Дата рождения обязательно")
        LocalDate birthday,

        @NotBlank(message = "Паспорт обязательно")
        String passport,

        @NotBlank(message = "Водительские права обязательно")
        String driverLicense,

        @NotNull(message = "Дата начала водительских прав обязательно")
        LocalDate dateStartDriverLicense,

        @NotBlank(message = "Категория обязательно")
        String category
) {
}
