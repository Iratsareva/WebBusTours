package demo.services.impl;

import demo.dto.PassengerDTO;
import demo.models.Role;
import demo.models.Ticket;
import demo.models.UserRoles;
import demo.repos.PassengerRepository;
import demo.repos.TicketRepository;
import demo.services.PassengerService;
import demo.models.Passenger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.domain.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service

public class PassengerServiceImpl implements PassengerService {

    @Autowired
    private PassengerRepository passengerRepository;

    @Autowired
    private TicketRepository ticketRepository;
    private ModelMapper modelMapper = new ModelMapper();


    @Override
    public PassengerDTO getPassengerById(String id) {
        Passenger p = passengerRepository.findById(Passenger.class, id);
        return new PassengerDTO(p.getId(), p.getName(), p.getSurname(), p.getPatronymic(), p.getBirthday(), p.getIdentificationDocument(), p.getTelephone(), p.getEmail(), p.getLogin(), p.getPassword());
    }

    @Override
    public PassengerDTO getPassengerByLogin(String login) {
        Passenger p = passengerRepository.findByLogin(login);
        return new PassengerDTO(p.getId(), p.getName(), p.getSurname(), p.getPatronymic(), p.getBirthday(), p.getIdentificationDocument(), p.getTelephone(), p.getEmail(), p.getLogin(), p.getPassword());
    }

    @Override
    @Transactional
    public List<PassengerDTO> findAll() {
        return passengerRepository.getAll(Passenger.class).stream().map(p ->
                new PassengerDTO(p.getId(), p.getName(), p.getSurname(), p.getPatronymic(), p.getBirthday(), p.getIdentificationDocument(), p.getTelephone(), p.getEmail(), p.getLogin(), p.getPassword())
        ).toList();
    }


    @Override
    public Page<PassengerDTO> getPassengers(int page, int size) {

        List<Passenger> listQuery = passengerRepository.getAll(Passenger.class).stream()
                .map(passenger -> passengerRepository.update(passenger)).toList();
        Pageable pageable = PageRequest.of(page - 1, size);

        int totalElements = listQuery.size();
        int start = (int) Math.min(pageable.getOffset(), totalElements);
        int end = Math.min(start + pageable.getPageSize(), totalElements);
        List<Passenger> passengerOnPage = listQuery.subList(start, end);

        Page<Passenger> page1 = new PageImpl<>(passengerOnPage, pageable, totalElements);

        return page1.map(p ->
                new PassengerDTO(p.getId(), p.getName(), p.getSurname(), p.getPatronymic(), p.getBirthday(), p.getIdentificationDocument(), p.getTelephone(), p.getEmail(), p.getLogin(), p.getPassword()));

    }

    @Override
    public List<PassengerDTO> getPassengersByTrip(String id) {
        List<Passenger> passengers = passengerRepository.PassengersTrip(id);
        return passengers.stream().map(p ->

                new PassengerDTO(p.getId(), p.getName(), p.getSurname(), p.getPatronymic(), p.getBirthday(), p.getIdentificationDocument(), p.getTelephone(), p.getEmail(), p.getLogin(), p.getPassword())).toList();
    }

    @Override
    public Boolean isAdminPassengerByLogin(String login) {
        Passenger p = passengerRepository.findByLogin(login);
        for(int i =0; i< p.getRoles().size();i++) {
            if (p.getRoles().get(i).getName().equals(new Role(UserRoles.ADMIN).getName())){
                return true;
            }
        }
        return false;
    }

    @Override
    public Boolean isModerPassengerByLogin(String login) {
        Passenger p = passengerRepository.findByLogin(login);
        for(int i =0; i< p.getRoles().size();i++) {
            if (p.getRoles().get(i).getName().equals(new Role(UserRoles.MODERATOR).getName())){
                return true;
            }
        }
        return false;
    }

    @Override
    public void updatePassenger(String name, String surname, String patronymic, LocalDate birthday,String identificationDocument,  String telephone, String login){
        var p = passengerRepository.findByLogin(login);
        p.setName(name);
        p.setSurname(surname);
        p.setPatronymic(patronymic);
        p.setBirthday(birthday);
        p.setIdentificationDocument(identificationDocument);
        p.setTelephone(telephone);
        passengerRepository.create(p);
    }


}
