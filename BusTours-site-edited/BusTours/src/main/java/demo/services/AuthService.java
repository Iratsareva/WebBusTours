package demo.services;

import demo.dto.PassengerRegistrationDTO;
import demo.models.Passenger;

public interface AuthService {
    String register (PassengerRegistrationDTO passengerRegistrationDTO);

    Passenger getPassenger(String login);
}
