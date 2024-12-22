package edu.rutmiit.demo.dto.passenger;

import jakarta.validation.constraints.NotBlank;

public record PassengerAuthForm(
        @NotBlank(message = "Логин обязательно")
        String login,
        @NotBlank(message = "Пароль обязательно")
        String password
) {
}
