package edu.rutmiit.demo.dto.ticket.forms;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TicketCreateForm(
        String id,
        String passengerId,
        @NotBlank(message = "Выберете  рейс")
        String tripId,

        @Min(value = 1, message = "Цена билета обязательно")
        Integer price,

        @NotBlank(message = "Фамилия обязателен")
        String surname,

        @NotBlank(message = "Имя обязательно")
        String name,
        String patronymic,

        @NotNull(message = "Дата рождения обязательно")
        String birthday,

        @NotBlank(message = "Паспорт обязательно")
        String identificationDocument,

        @NotBlank(message = "Телефон обязательно")
        String telephone,

        @NotBlank(message = "Почта обязательно")
        String email
) {
}
