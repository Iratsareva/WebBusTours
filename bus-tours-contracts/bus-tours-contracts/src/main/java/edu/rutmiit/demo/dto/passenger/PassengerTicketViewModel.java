package edu.rutmiit.demo.dto.passenger;

import java.time.LocalDate;


public record PassengerTicketViewModel(
        String id,
        String name,
        String surname,
        String patronymic

) {}