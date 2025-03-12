package edu.rutmiit.demo.dto.driver.forms;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DriverEditForm(
        @NotBlank(message = "Идентификатор обязателен")
        String id,

        @NotBlank(message = "Имя обязательно")
        String name,

        @NotBlank(message = "Фамилия обязательно")
        String surname,

        @NotBlank(message = "Отчество обязательно")
        String patronymic,

        @NotNull(message = "Дата рождения обязательно")
        String birthday,

        @NotBlank(message = "Паспорт обязательно")
        String passport,

        @NotBlank(message = "Водительские права обязательно")
        String driverLicense,

        @NotNull(message = "Дата начала водительских прав обязательно")
        String dateStartDriverLicense,

        @NotBlank(message = "Категория обязательно")
        String category
) {
}
