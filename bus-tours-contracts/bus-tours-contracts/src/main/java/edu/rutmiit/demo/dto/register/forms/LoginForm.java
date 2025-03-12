package edu.rutmiit.demo.dto.register.forms;

import jakarta.validation.constraints.NotBlank;

public record LoginForm(
        @NotBlank(message = "Логин обязательно")
        String login,

        @NotBlank(message = "Пароль обязательно")
        String password
) {
}
