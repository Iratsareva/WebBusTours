package edu.rutmiit.demo.dto.ticket;

import jakarta.validation.constraints.NotBlank;

public record TicketCreateForm1(
        @NotBlank(message = "Название города обязательно")
        String passengerId,
        @NotBlank(message = "Название города обязательно")
        String tripId



) {
}
