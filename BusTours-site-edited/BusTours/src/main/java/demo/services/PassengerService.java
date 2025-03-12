package demo.services;

import demo.dto.passenger.PassengerDTO;
import demo.dto.passenger.PassengerUpdateDTO;
import demo.exception.NotFoundException;

import java.util.List;

public interface PassengerService {
    PassengerDTO getPassengerById(String id) throws NotFoundException;
    List<PassengerDTO> findAll() throws NotFoundException;
    PassengerDTO getPassengerByLogin(String login) throws NotFoundException;
    List<PassengerDTO> getPassengers() throws NotFoundException;
    List<PassengerDTO> getPassengersByTrip(String id) throws NotFoundException;
    Boolean isAdminPassengerByLogin(String login) throws NotFoundException;
    Boolean isModerPassengerByLogin(String login) throws NotFoundException;
    void updatePassenger(PassengerUpdateDTO passengerUpdateDTO) throws NotFoundException;
}