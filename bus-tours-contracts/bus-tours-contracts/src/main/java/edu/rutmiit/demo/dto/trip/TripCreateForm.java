package edu.rutmiit.demo.dto.trip;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public record TripCreateForm(
        @NotBlank(message = "ID тура обязательно")
        String tourId,

        @NotBlank(message = "ID автобуса обязательно")
        String busId,

        @NotBlank(message = "ID водителя обязательно")
        String driverId,

        @NotBlank(message = "Дата отправления обязательно")
        String startDate,

        @NotBlank(message = "Дата прибытия обязательно")
        String endDate,

        String tripStatus
) {
        @AssertTrue(message = "ID водителя должен быть указан только после ввода дат отправления и прибытия")
        public boolean isDriverIdValid() {
                return (startDate != null && endDate != null) || (driverId == null || driverId.isBlank());
        }
}
