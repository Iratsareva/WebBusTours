package edu.rutmiit.demo.dto.passenger;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public record PassengerCreateForm(
        @NotBlank(message = "Имя обязательно")
        String name,
        @NotBlank(message = "Фамилия обязательно")
        String surname,
        String patronymic,
        String telephone,
        @NotBlank(message = "Почта обязательно")
        String email,
        @NotBlank(message = "Логин обязательно")
        String login,
        @NotBlank(message = "Пароль обязательно")
        String password,

        @NotBlank(message = "Пароль обязательно")
        String confirmPassword
) {
}
