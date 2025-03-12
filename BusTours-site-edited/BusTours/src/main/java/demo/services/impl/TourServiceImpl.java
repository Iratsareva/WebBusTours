package demo.services.impl;

import demo.dto.tour.TourCreateDTO;
import demo.dto.tour.TourDTO;
import demo.dto.tour.TourUpdateDTO;
import demo.exception.NotFoundException;
import demo.models.*;
import demo.repositories.*;
import demo.services.TourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.*;

@Service
public class TourServiceImpl implements TourService {
    private TourRepository tourRepository;
    private TripRepository tripRepository;
    private TicketRepository ticketRepository;
    private CityRepository cityRepository;
    @Override
    public TourDTO createTour(TourCreateDTO tourCreateDTO)throws NotFoundException {
        City city = cityRepository.findById(tourCreateDTO.destination());
        if(city == null){
            throw new NotFoundException("Город с id " + tourCreateDTO.destination() + " не найден");
        }
        Tour tour =  tourRepository.create(new Tour(tourCreateDTO.nameTour(), tourCreateDTO.description(), tourCreateDTO.itinerary(), tourCreateDTO.lengthTour(), tourCreateDTO.price(),city));
        return new TourDTO(tour.getId(), tour.getNameTour(), tour.getDescription(), tour.getItinerary(), tour.getLengthTour(), tour.getPrice(), tour.getDestination().getId());
    }

    @Override
   public void updateTour(TourUpdateDTO tourUpdateDTO) throws NotFoundException {
        City city = cityRepository.findById(tourUpdateDTO.destination());
        if(city == null){
            throw new NotFoundException("Город с id " + tourUpdateDTO.destination() + " не найден");
        }

        var tour = tourRepository.findById(tourUpdateDTO.id());
        if(tour == null){
            throw new NotFoundException("Тур с id " + tourUpdateDTO.id() + " не найден");
        }
        tour.setNameTour(tourUpdateDTO.nameTour());
        tour.setDescription(tourUpdateDTO.description());
        tour.setItinerary(tourUpdateDTO.itinerary());
        tour.setLengthTour(tourUpdateDTO.lengthTour());
        tour.setPrice(tourUpdateDTO.price());
        tour.setDestination(city);

        tourRepository.create(tour);
   }



    @Override
    public TourDTO getTourById(String id)throws NotFoundException {
        Tour tour = tourRepository.findById(id);
        if(tour == null){
            throw new NotFoundException("Тур с id " + id + " не найден");
        }
        return new TourDTO(id, tour.getNameTour(), tour.getDescription(), tour.getItinerary(), tour.getLengthTour(), tour.getPrice(), tour.getDestination().getNameCity());
    }

    @Override
    public List<TourDTO> findAll() throws NotFoundException {
        List<Tour> tours = tourRepository.findAll();
        if(tours == null || tours.isEmpty()){
            throw new NotFoundException("Туры не найдены");
        }
        return tours.stream().map(tour ->
                new TourDTO(tour.getId(), tour.getNameTour(), tour.getDescription(), tour.getItinerary(), tour.getLengthTour(), tour.getPrice(), tour.getDestination().getNameCity())
        ).toList();
    }

    //Нельзя добавить Exception, пользователю выводится что список пуст
    @Override
    public List<TourDTO> findTourByDestinationAndStartDateAndLengthTour(String destination, LocalDate startDate, Integer lengthTour) {
        List<Tour> listQuery = tourRepository.findTourByDestinationAndStartDateAndLengthTour(destination, startDate, lengthTour );
        return listQuery.stream().map(tour -> new TourDTO(tour.getId(), tour.getNameTour(), tour.getDescription(), tour.getItinerary(), tour.getLengthTour(), tour.getPrice(), tour.getDestination().getNameCity())).toList();
    }

    @Override
    public List<TourDTO> findTourByDestination(String destination) {
        List<Tour> listQuery = tourRepository.findTourByDestination(destination);
        return listQuery.stream().map(tour -> new TourDTO(tour.getId(), tour.getNameTour(), tour.getDescription(), tour.getItinerary(), tour.getLengthTour(), tour.getPrice(), tour.getDestination().getNameCity())).toList();
    }

    @Override
    public List<TourDTO> getTours() {
        List<Tour> listQuery = tourRepository.findAll();
        return listQuery.stream().map(tour -> new TourDTO(tour.getId(), tour.getNameTour(), tour.getDescription(), tour.getItinerary(), tour.getLengthTour(), tour.getPrice(), tour.getDestination().getNameCity())).toList();
    }

    @Override
    public List<TourDTO> findTourByTickets (){
        List<Tour> tourList = tourRepository.findAll();
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

        return sortedTourList.subList(0,5).stream().map(tour -> new TourDTO(tour.getId(), tour.getNameTour(), tour.getDescription(), tour.getItinerary(), tour.getLengthTour(), tour.getPrice(), tour.getDestination().getNameCity())).toList();
    }
//
    @Autowired
    public void setTourRepository(TourRepository tourRepository) {
        this.tourRepository = tourRepository;
    }
    @Autowired
    public void setTripRepository(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }
    @Autowired
    public void setTicketRepository(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }
    @Autowired
    public void setCityRepository(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

}
