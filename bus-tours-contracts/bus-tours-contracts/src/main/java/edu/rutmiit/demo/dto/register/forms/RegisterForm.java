package edu.rutmiit.demo.dto.register.forms;

import jakarta.validation.constraints.NotBlank;

public record RegisterForm(
        @NotBlank(message = "Имя обязательно")
        String name,

        @NotBlank(message = "Фамилия обязательно")
        String surname,

        String patronymic,

        @NotBlank(message = "Почта обязательно")
        String email,

        @NotBlank(message = "Логин обязательно")
        String login,

        @NotBlank(message = "Пароль обязательно")
        String password,

        @NotBlank(message = "Повторите пароль обязательно")
        String confirmPassword
) {
}
