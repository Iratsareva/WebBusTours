package demo.services;

import demo.dto.passenger.PassengerRegistrationDTO;
import demo.exception.ClientException;
import demo.models.Passenger;

public interface AuthService {
    void register (PassengerRegistrationDTO passengerRegistrationDTO) throws ClientException;
    Passenger getPassenger(String login);
}
