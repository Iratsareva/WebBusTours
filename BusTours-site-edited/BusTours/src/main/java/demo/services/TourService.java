package demo.services;

import demo.dto.tour.TourCreateDTO;
import demo.dto.tour.TourDTO;
import demo.dto.tour.TourUpdateDTO;
import demo.exception.NotFoundException;

import java.time.LocalDate;
import java.util.List;

public interface TourService {
    TourDTO createTour (TourCreateDTO tourCreateDTO) throws NotFoundException;
    TourDTO getTourById (String id) throws NotFoundException;
    List<TourDTO> findAll() throws NotFoundException;
    List<TourDTO> findTourByDestination(String destination);
    List<TourDTO> findTourByDestinationAndStartDateAndLengthTour (String destination, LocalDate startDate, Integer lengthTour);
    List<TourDTO> findTourByTickets ();
    void updateTour(TourUpdateDTO tourUpdateDTO) throws NotFoundException;
    List<TourDTO> getTours();
}
