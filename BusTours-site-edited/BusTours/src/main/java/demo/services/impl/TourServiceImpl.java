package demo.services.impl;

import demo.exception.PasNotFoundException;
import demo.dto.TourDTO;
import demo.dto.TripDTO;
import demo.models.*;
import demo.repos.*;
import demo.services.TourService;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service
public class TourServiceImpl implements TourService {
    @Autowired
    private TourRepository tourRepository;
    @Autowired
    private TripRepository tripRepository;
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private BusRepository busRepository;

    @Autowired
    private CityRepository cityRepository;



    private ModelMapper modelMapper = new ModelMapper();



    @Override
    public TourDTO createTour( String nameTour,
                            String description,
                            String itinerary,
                            Integer lengthTour,
                            Integer price,
                            String destination) {

        Tour tour =  tourRepository.create(new Tour(nameTour, description, itinerary, lengthTour, price,
                cityRepository.findById(City.class, destination)));
        return new TourDTO(tour.getId(), tour.getNameTour(), tour.getDescription(), tour.getItinerary(), tour.getLengthTour(), tour.getPrice(), tour.getDestination().getId());

    }

    @Override
   public void updateTour(String id,
                          String nameTour,
                          String description,
                          String itinerary,
                          Integer lengthTour,
                          Integer price,
                          String destination) {
        var tour = tourRepository.findById(Tour.class, id);

        tour.setNameTour(nameTour);
        tour.setDescription(description);
        tour.setItinerary(itinerary);
        tour.setLengthTour(lengthTour);
        tour.setPrice(price);
        tour.setDestination(cityRepository.findById(City.class, destination));

        tourRepository.create(tour);

   }



    @Override
    public TourDTO getTourById(String id) {
        Tour t = tourRepository.findById(Tour.class, id);
        return new TourDTO(id, t.getNameTour(), t.getDescription(), t.getItinerary(), t.getLengthTour(), t.getPrice(), t.getDestination().getNameCity());
    }

    @Override
    @Transactional
    public List<TourDTO> findAll() {
        return tourRepository.getAll(Tour.class).stream().map(t ->
                new TourDTO(t.getId(), t.getNameTour(), t.getDescription(), t.getItinerary(), t.getLengthTour(), t.getPrice(), t.getDestination().getNameCity())
        ).toList();
    }




    @Override
    @Transactional
    public Page<TourDTO> findTourByDestinationAndStartDateAndLengthTour(String destination, LocalDate startDate, Integer lengthTour) {
        List<Tour> listQuery = tourRepository.findTourByDestinationAndStartDateAndLengthTour(destination, startDate, lengthTour );
        if(listQuery.isEmpty()){
            return new PageImpl<>(Collections.emptyList(), PageRequest.of(0, 1), 0); // Пустая страница
            }


        List<Tour> tourOnPage = listQuery.subList(0,listQuery.size() );
        Pageable pageable = PageRequest.of(0, listQuery.size());
        int totalElements = listQuery.size();
        Page<Tour> page1 = new PageImpl<>(tourOnPage, pageable, totalElements);
        return page1.map(tour -> new TourDTO(tour.getId(), tour.getNameTour(), tour.getDescription(), tour.getItinerary(), tour.getLengthTour(), tour.getPrice(), tour.getDestination().getNameCity()));
    }









    @Override
    public Page<TourDTO> findTourByDestination(String destination, int page, int size) {
        List<Tour> listQuery = tourRepository.findTourByDestination(destination);
        Pageable pageable = PageRequest.of(page - 1, size);


        int totalElements = listQuery.size();
        int start = (int) Math.min(pageable.getOffset(), totalElements);
        int end = Math.min(start + pageable.getPageSize(), totalElements);
        List<Tour> tourOnPage = listQuery.subList(start, end);

        Page<Tour> page1 = new PageImpl<>(tourOnPage, pageable, totalElements);

        return page1.map(tour -> new TourDTO(tour.getId(), tour.getNameTour(), tour.getDescription(), tour.getItinerary(), tour.getLengthTour(), tour.getPrice(), tour.getDestination().getNameCity()));

    }



    @Override
    public Page<TourDTO> getTours(int page, int size) {
        List<Tour> listQuery = tourRepository.getAll(Tour.class);
        Pageable pageable = PageRequest.of(page - 1, size);


        int totalElements = listQuery.size();
        int start = (int) Math.min(pageable.getOffset(), totalElements);
        int end = Math.min(start + pageable.getPageSize(), totalElements);
        List<Tour> tourOnPage = listQuery.subList(start, end);

        Page<Tour> page1 = new PageImpl<>(tourOnPage, pageable, totalElements);

        return page1.map(tour -> new TourDTO(tour.getId(), tour.getNameTour(), tour.getDescription(), tour.getItinerary(), tour.getLengthTour(), tour.getPrice(), tour.getDestination().getNameCity()));

    }




    @Override
    @Transactional
    public Page<TourDTO> findTourByTickets (){
        List<Tour> tourList = tourRepository.getAll(Tour.class);
        Map<Tour, Integer> tourMap = new HashMap<>();

        List<Tour> sortedTourList = new ArrayList<>();

        for( Tour tour :tourList){
            int numberTickets=0;
            List<Trip> tripList =tripRepository.findTripByTour(tour.getId());

            for( Trip trip: tripList){
                numberTickets += ticketRepository.TicketsTrip(trip.getId()).size();
            }
            tourMap.put(tour, numberTickets);
        }
        sortedTourList.addAll(tourMap.keySet());
        Collections.sort(sortedTourList, new Comparator<Tour>() {
            @Override
            public int compare(Tour t1, Tour t2) {
                return tourMap.get(t2).compareTo(tourMap.get(t1));
            }
        });





        List<Tour> listQuery = sortedTourList.subList(0, 5);
        Pageable pageable = PageRequest.of(0, 5, Sort.by("name"));
        int totalElements = listQuery.size();
        Page<Tour> page1 = new PageImpl<>(listQuery, pageable, totalElements);
        return page1.map(tour -> new TourDTO(tour.getId(), tour.getNameTour(), tour.getDescription(), tour.getItinerary(), tour.getLengthTour(), tour.getPrice(), tour.getDestination().getNameCity()));
    }








}
