package demo.services;

import demo.dto.TourDTO;
import demo.dto.TripDTO;
import org.springframework.data.domain.Page;


import java.time.LocalDate;
import java.util.List;

public interface TourService {
    TourDTO createTour (
    String nameTour,
    String description,
    String itinerary,
    Integer lengthTour,
    Integer price,
    String destination
    );
    TourDTO getTourById (String id);
    List<TourDTO> findAll();


    Page<TourDTO> findTourByDestination(String destination, int page, int size);

    Page<TourDTO> findTourByDestinationAndStartDateAndLengthTour (String destination,
                                                                  LocalDate startDate,
                                                                  Integer lengthTour);
    Page<TourDTO> findTourByTickets ();

    void updateTour(String id,
                           String nameTour,
                           String description,
                           String itinerary,
                           Integer lengthTour,
                           Integer price,
                           String destination);

    Page<TourDTO> getTours(int page, int size);
}
