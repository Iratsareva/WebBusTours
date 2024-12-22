package demo.repos;

import demo.models.Passenger;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PassengerRepository  {
    Passenger create(Passenger passenger);
    Passenger findById(Class<Passenger> passengerClass, String id);
    List<Passenger> getAll(Class<Passenger> passengerClass);
    Passenger update(Passenger passenger);
    void delete(Passenger passenger);


    Passenger findByLogin(String login);

    Passenger authorisation(String login, String password);

    List<Passenger> PassengersTrip(String idTrip);
    Passenger findByEmail(String email);
    }
