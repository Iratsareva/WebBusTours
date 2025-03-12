package edu.rutmiit.demo.dto.passenger;


public record PassengerTicketViewModel(
        String id,
        String name,
        String surname,
        String patronymic
) {}