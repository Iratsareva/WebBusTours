package edu.rutmiit.demo.dto.passenger.forms;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public record PassengerEditForm(
        @NotBlank(message = "Идентификатор обязателен")
        String id,
        @NotBlank(message = "Имя обязателен")
        String name,
        @NotBlank(message = "Фамилия обязателен")
        String surname,
        String patronymic,
        @NotNull(message = "Дата рождения обязателен")
        String birthday,
        @NotBlank(message = "Паспорт обязателен")
        String identificationDocument,
        @NotBlank(message = "Телефон обязателен")
        String telephone


) {}