package demo.services;

import demo.dto.trip.TripCreateDTO;
import demo.dto.trip.TripUpdateDTO;
import demo.exception.NotFoundException;
import demo.dto.trip.TourTripDTO;
import demo.dto.trip.TripDTO;

import java.util.List;

public interface TripService {
    TripDTO createTrip (TripCreateDTO tripCreateDTO) throws NotFoundException;
    TripDTO getTripById (String id) throws NotFoundException;
    List<TripDTO> findAll() throws NotFoundException;
    void updateTrip(TripUpdateDTO tripUpdateDTO) throws NotFoundException;
    List<TourTripDTO> findTourByDateTrip() throws NotFoundException;
    List<TripDTO> getTrips() throws NotFoundException;
    List<TripDTO> findTripByTour(String id) throws NotFoundException;
    String findTripByTourString(String id);
}
