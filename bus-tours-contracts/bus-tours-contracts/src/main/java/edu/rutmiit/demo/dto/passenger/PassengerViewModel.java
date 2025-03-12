package edu.rutmiit.demo.dto.passenger;

import java.time.LocalDate;

public record PassengerViewModel(
        String id,
        String name,
        String surname,
        String patronymic,
        LocalDate birthday,
        String identificationDocument,
        String telephone,
        String email,
        String login,
        String password
) {}