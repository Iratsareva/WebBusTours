package edu.rutmiit.demo.dto.bus.forms;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record BusEditForm(
        @NotBlank(message = "Идентификатор обязателен")
        String id,

        @NotBlank(message = "Марка обязательно")
        String mark,

        @NotBlank(message = "Номер автобуса обязательно")
        String numberBus,

        @NotNull(message = "Количество мест обязательно")
        Integer numberSeats,

        @NotBlank(message = "Класс обязательно")
        String classBus,

        @NotNull( message = "Год должен быть больше 2000")
        LocalDate year
) {
}