package edu.rutmiit.demo.dto.trip.forms;

import edu.rutmiit.demo.dto.trip.TripStatus;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record TripCreateForm(
        @NotBlank(message = "ID тура обязательно")
        String tourId,

        @NotBlank(message = "ID автобуса обязательно")
        String busId,

        @NotBlank(message = "ID водителя обязательно")
        String driverId,

        @NotNull(message = "Дата отправления обязательно")
        LocalDate startDate,

        @NotNull(message = "Дата прибытия обязательно")
        LocalDate endDate,

        TripStatus tripStatus
) {
        @AssertTrue(message = "ID водителя должен быть указан только после ввода дат отправления и прибытия")
        public boolean isDriverIdValid() {
                return (startDate != null && endDate != null) || (driverId == null || driverId.isBlank());
        }
}
