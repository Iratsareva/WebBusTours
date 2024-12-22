package demo.services;

import demo.dto.PassengerDTO;
import demo.models.Passenger;
import demo.models.Role;
import org.springframework.data.domain.Page;
import org.springframework.security.core.GrantedAuthority;


import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public interface PassengerService {
    PassengerDTO getPassengerById(String id);
    List<PassengerDTO> findAll();
    PassengerDTO getPassengerByLogin(String login);
    Page<PassengerDTO> getPassengers(int page, int size);

    List<PassengerDTO> getPassengersByTrip(String id);

    Boolean isAdminPassengerByLogin(String login);
    Boolean isModerPassengerByLogin(String login);


    void updatePassenger(String name, String surname, String patronymic, LocalDate birthday, String identificationDocument, String telephone, String login);

    }
