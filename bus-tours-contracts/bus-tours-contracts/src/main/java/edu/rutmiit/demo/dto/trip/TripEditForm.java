package edu.rutmiit.demo.dto.trip;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record TripEditForm(
        @NotBlank(message = "Идентификатор обязателен")
        String id,
        @NotBlank(message = "ID тура обязательно")
        String tourId,

        @NotBlank(message = "ID автобуса обязательно")
        String busId,

        @NotBlank(message = "ID водителя обязательно")
        String driverId,

        @NotNull(message = "Дата отправления обязательно")
        String startDate,

        @NotNull(message = "Дата прибытия обязательно")
        String endDate,

        @NotBlank(message = "Статус обязательно")
        TripStatus tripStatus
) {
}
