package demo.services.impl;

import demo.dto.passenger.PassengerDTO;
import demo.dto.passenger.PassengerUpdateDTO;
import demo.exception.NotFoundException;
import demo.models.Role;
import demo.models.UserRoles;
import demo.repositories.PassengerRepository;
import demo.services.PassengerService;
import demo.models.Passenger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PassengerServiceImpl implements PassengerService {
    private PassengerRepository passengerRepository;

    @Override
    public PassengerDTO getPassengerById(String id)throws NotFoundException {
        Passenger passenger = passengerRepository.findById(id);
        if (passenger == null) {
            throw new NotFoundException("Пассажир с ID " + id + " не найден");
        }
        return new PassengerDTO(passenger.getId(), passenger.getName(), passenger.getSurname(), passenger.getPatronymic(), passenger.getBirthday(), passenger.getIdentificationDocument(), passenger.getTelephone(), passenger.getEmail(), passenger.getLogin(), passenger.getPassword());
    }

    @Override
    public PassengerDTO getPassengerByLogin(String login)throws NotFoundException {
        Passenger passenger = passengerRepository.findByLogin(login);
        if (passenger == null) {
            throw new NotFoundException("Пассажир с логином " + login + " не найден");
        }
        return new PassengerDTO(passenger.getId(), passenger.getName(), passenger.getSurname(), passenger.getPatronymic(), passenger.getBirthday(), passenger.getIdentificationDocument(), passenger.getTelephone(), passenger.getEmail(), passenger.getLogin(), passenger.getPassword());
    }

    @Override
    public List<PassengerDTO> findAll()throws NotFoundException {
        List<Passenger> passengers = passengerRepository.findAll();
        if(passengers == null || passengers.isEmpty()){
            throw new NotFoundException("Пассажиры не найдены");
        }
        return passengers.stream().map(passenger ->
                new PassengerDTO(passenger.getId(), passenger.getName(), passenger.getSurname(), passenger.getPatronymic(), passenger.getBirthday(), passenger.getIdentificationDocument(), passenger.getTelephone(), passenger.getEmail(), passenger.getLogin(), passenger.getPassword())).toList();
    }

    @Override
    public List<PassengerDTO> getPassengers() throws NotFoundException {
        List<Passenger> passengers = passengerRepository.findAll();
        if(passengers == null || passengers.isEmpty()){
            throw new NotFoundException("Пассажиры не найдены");
        }
        List<Passenger> passengersUpdate = passengers.stream().map(passenger -> passengerRepository.update(passenger)).toList();
        return passengersUpdate.stream().map(passenger ->
                new PassengerDTO(passenger.getId(), passenger.getName(), passenger.getSurname(), passenger.getPatronymic(), passenger.getBirthday(), passenger.getIdentificationDocument(), passenger.getTelephone(), passenger.getEmail(), passenger.getLogin(), passenger.getPassword())).toList();
    }

    @Override
    public List<PassengerDTO> getPassengersByTrip(String id)throws NotFoundException {
        List<Passenger> passengers = passengerRepository.PassengersTrip(id);
        if(passengers == null || passengers.isEmpty()){
            throw new NotFoundException("Пассажиры не найдены");
        }
        return passengers.stream().map(passenger ->
                new PassengerDTO(passenger.getId(), passenger.getName(), passenger.getSurname(), passenger.getPatronymic(), passenger.getBirthday(), passenger.getIdentificationDocument(), passenger.getTelephone(), passenger.getEmail(), passenger.getLogin(), passenger.getPassword())).toList();
    }

    @Override
    public Boolean isAdminPassengerByLogin(String login) throws NotFoundException {
        Passenger passenger = passengerRepository.findByLogin(login);
        if (passenger == null) {
            throw new NotFoundException("Пассажир с логином " + login + " не найден");
        }
        for(int i = 0; i< passenger.getRoles().size(); i++) {
            if (passenger.getRoles().get(i).getName().equals(new Role(UserRoles.ADMIN).getName())){
                return true;
            }
        }
        return false;
    }

    @Override
    public Boolean isModerPassengerByLogin(String login) throws NotFoundException {
        Passenger passenger = passengerRepository.findByLogin(login);
        if (passenger == null) {
            throw new NotFoundException("Пассажир с логином " + login + " не найден");
        }
        for(int i = 0; i< passenger.getRoles().size(); i++) {
            if (passenger.getRoles().get(i).getName().equals(new Role(UserRoles.MODERATOR).getName())){
                return true;
            }
        }
        return false;
    }

    @Override
    public void updatePassenger(PassengerUpdateDTO passengerUpdateDTO) throws NotFoundException {
        var passenger = passengerRepository.findByLogin(passengerUpdateDTO.login());
        if (passenger == null) {
            throw new NotFoundException("Пассажир с логином " + passengerUpdateDTO.login() + " не найден");
        }
        passenger.setName(passengerUpdateDTO.name());
        passenger.setSurname(passengerUpdateDTO.surname());
        passenger.setPatronymic(passengerUpdateDTO.patronymic());
        passenger.setBirthday(passengerUpdateDTO.birthday());
        passenger.setIdentificationDocument(passengerUpdateDTO.identificationDocument());
        passenger.setTelephone(passengerUpdateDTO.telephone());
        passengerRepository.create(passenger);
    }

    @Autowired
    public void setPassengerRepository(PassengerRepository passengerRepository) {
        this.passengerRepository = passengerRepository;
    }
}
