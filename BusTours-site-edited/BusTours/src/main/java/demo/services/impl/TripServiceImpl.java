package demo.services.impl;

import demo.dto.trip.TripCreateDTO;
import demo.dto.trip.TripDTO;
import demo.dto.trip.TripUpdateDTO;
import demo.exception.NotFoundException;
import demo.models.*;
import demo.repositories.DriverRepository;
import demo.repositories.TourRepository;
import demo.repositories.TripRepository;
import demo.services.TripService;
import edu.rutmiit.demo.dto.trip.TripStatus;
import demo.dto.trip.TourTripDTO;
import demo.repositories.BusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class TripServiceImpl implements TripService {
    private TripRepository tripRepository;
    private TourRepository tourRepository;
    private BusRepository busRepository;
    private DriverRepository driverRepository;

    @Override
    public TripDTO createTrip(TripCreateDTO tripCreateDTO) throws NotFoundException {
        if (tripCreateDTO.tourId()== null) {
            throw new NotFoundException("Такого тура не существует");
        }

        TripStatus tripStatus = tripCreateDTO.tripStatus();
        if (tripCreateDTO.startDate().isAfter(LocalDate.now())) {
            tripStatus = (TripStatus.AWAITING);
        } else if (tripCreateDTO.endDate().isBefore(LocalDate.now())) {
            tripStatus = (TripStatus.COMPLETED);
        }else tripStatus = (TripStatus.ON_WAY);

        Tour tour = tourRepository.findTourByTrip(tripCreateDTO.tourId());
        if (tour == null) {
            throw new NotFoundException("Тур с ID " + tripCreateDTO.tourId() + " не найден");
        }
        Bus bus = busRepository.findById(tripCreateDTO.busId());
        if (bus == null) {
            throw new NotFoundException("Автобус с ID " + tripCreateDTO.busId() + " не найден");
        }
        Driver driver = driverRepository.findById(tripCreateDTO.driverId());
        if (driver == null) {
            throw new NotFoundException("Водитель с ID " + tripCreateDTO.driverId() + " не найден");
        }

        Trip trip = tripRepository.create(new Trip(
                tour,
                bus,
                driver,
                tripCreateDTO.startDate(),
                tripCreateDTO.endDate(),
                tripStatus));
        return new TripDTO(trip.getId(), trip.getTour().getId(), trip.getBus().getId(), trip.getDriver().getId(),trip.getStartDate(), trip.getEndDate(), trip.getTripStatus());
    }

    @Override
    public void updateTrip(TripUpdateDTO tripUpdateDTO)throws NotFoundException {

        Tour tour = tourRepository.findTourByTrip(tripUpdateDTO.tourId());
        if (tour == null) {
            throw new NotFoundException("Тур с ID " + tripUpdateDTO.tourId() + " не найден");
        }
        Bus bus = busRepository.findById(tripUpdateDTO.busId());
        if (bus == null) {
            throw new NotFoundException("Автобус с ID " + tripUpdateDTO.busId() + " не найден");
        }
        Driver driver = driverRepository.findById(tripUpdateDTO.driverId());
        if (driver == null) {
            throw new NotFoundException("Водитель с ID " + tripUpdateDTO.driverId() + " не найден");
        }
        var trip = tripRepository.findById(tripUpdateDTO.id());
        if (trip == null) {
            throw new NotFoundException("Рейс с ID " + tripUpdateDTO.id() + " не найден");
        }

        trip.setTour(tour);
        trip.setBus(bus);
        trip.setDriver(driver);
        trip.setStartDate(tripUpdateDTO.startDate());
        trip.setEndDate(tripUpdateDTO.endDate());
        trip.setTripStatus(tripUpdateDTO.tripStatus());

        tripRepository.create(trip);
    }

    @Override
    public TripDTO getTripById(String id) throws NotFoundException {
        Trip trip = tripRepository.findById(id);
        if (trip == null) {
            throw new NotFoundException("Рейс с ID " + id + " не найден");
        }
        return new TripDTO(trip.getId(), trip.getTour().getId(), trip.getBus().getId(), trip.getDriver().getId(), trip.getStartDate(), trip.getEndDate(), trip.getTripStatus());
    }

    @Override
    public List<TripDTO> findAll()throws NotFoundException {
        List<Trip> trips = tripRepository.findAll();
        if(trips == null || trips.isEmpty()){
            throw new NotFoundException("Рейсы не найдены");
        }

        for (Trip trip : trips) {
            if (trip.getStartDate().isAfter(LocalDate.now())) {
                trip.setTripStatus(TripStatus.AWAITING);
            } else if (trip.getEndDate().isBefore(LocalDate.now())) {
                trip.setTripStatus(TripStatus.COMPLETED);
            } else trip.setTripStatus(TripStatus.ON_WAY);
        }

        return trips.stream().map(trip ->
                new TripDTO(trip.getId(), trip.getTour().getId(), trip.getBus().getId(), trip.getDriver().getId(), trip.getStartDate(), trip.getEndDate(), trip.getTripStatus())).toList();
    }

    @Override
    public List<TourTripDTO> findTourByDateTrip ()throws NotFoundException {
        List<Trip> trips = tripRepository.findAll();
        if(trips == null || trips.isEmpty()){
            throw new NotFoundException("Рейсы не найдены");
        }
        List<TourTripDTO> result = new ArrayList<>();
        trips.sort(Comparator.comparing(Trip::getStartDate));

        for(Trip trip : trips){
            Tour tour = tourRepository.findTourByTrip(trip.getId());
            if (tour == null) {
                throw new NotFoundException("Тур с ID " + trip.getId() + " не найден");
            }
            TourTripDTO tourTripDTO = new TourTripDTO(tour.getId(), tour.getNameTour(), tour.getDestination().getNameCity(), tour.getItinerary(), tour.getPrice(), trip.getStartDate(), trip.getEndDate());
            result.add(tourTripDTO);
        }

        List<TourTripDTO> listQuery = result.subList(0, 5);
        return listQuery;
    }

    @Override
    public List<TripDTO> getTrips()throws NotFoundException {
        List<Trip> trips = tripRepository.findAll();
        if(trips == null || trips.isEmpty()){
            throw new NotFoundException("Рейсы не найдены");
        }

        for (Trip trip : trips) {
            if (trip.getStartDate().isAfter(LocalDate.now())) {
                trip.setTripStatus(TripStatus.AWAITING);
            } else if (trip.getEndDate().isBefore(LocalDate.now())) {
                trip.setTripStatus(TripStatus.COMPLETED);
            } else trip.setTripStatus(TripStatus.ON_WAY);
        }
        Collections.sort(trips, new Comparator<Trip>() {
            @Override
            public int compare(Trip t1, Trip t2) {
                return t2.getTripStatus().compareTo(t1.getTripStatus());
            }
        });

        return trips.stream().map(trip ->
                new TripDTO(trip.getId(), trip.getTour().getId(), trip.getBus().getId(), trip.getDriver().getId(), trip.getStartDate(), trip.getEndDate(), trip.getTripStatus())).toList();
    }

    @Override
    public List<TripDTO> findTripByTour(String id) {
        List<Trip> trips = tripRepository.findTripByTour(id);
        List<Trip> tripsFilter = trips.stream()
                //проверка, что билет можно купить за день до отправления
                .filter(trip -> trip.getStartDate().minusDays(1).isAfter(LocalDate.now()))
                //проверка, что в автобусе еще есть места
                .filter(trip -> trip.getTickets().size()<trip.getBus().getNumberSeats()).toList();

        return trips.stream().map(trip ->
                new TripDTO(trip.getId(), trip.getTour().getId(), trip.getBus().getId(), trip.getDriver().getId(), trip.getStartDate(), trip.getEndDate(), trip.getTripStatus())
        ).toList();
    }

    @Override
    public String findTripByTourString(String id){
        List<TripDTO> trips = findTripByTour(id).stream()
                .filter(trip -> trip.startDate().isAfter(LocalDate.now())).toList();

        StringBuilder strBuilder = new StringBuilder();
     trips.forEach(trip -> strBuilder.append(trip.startDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))
             .append(" - ").append(trip.endDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))
             .append("; "));
     if (strBuilder.length() == 0){
         return null;
     }else {
         return strBuilder.toString();
     }
    }

    @Autowired
    public void setTripRepository(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }
    @Autowired
    public void setTourRepository(TourRepository tourRepository) {
        this.tourRepository = tourRepository;
    }
    @Autowired
    public void setBusRepository(BusRepository busRepository) {
        this.busRepository = busRepository;
    }
    @Autowired
    public void setDriverRepository(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }
}
