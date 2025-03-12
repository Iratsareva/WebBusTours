package demo.controllers;

import demo.dto.bus.BusDto;
import demo.dto.driver.DriverDTO;
import demo.dto.passenger.PassengerDTO;
import demo.dto.tour.TourDTO;
import demo.dto.trip.TripCreateDTO;
import demo.dto.trip.TripDTO;
import demo.dto.trip.TripUpdateDTO;
import demo.exception.NotFoundException;
import demo.services.*;
import edu.rutmiit.demo.controllers.TripController;
import edu.rutmiit.demo.dto.bus.BusViewModel;
import edu.rutmiit.demo.dto.driver.DriverViewModel;
import edu.rutmiit.demo.dto.passenger.PassengerListViewModel;
import edu.rutmiit.demo.dto.passenger.PassengerViewModel;
import edu.rutmiit.demo.dto.tour.TourViewModel;
import edu.rutmiit.demo.dto.tourTrip.TourTripListViewModel;
import edu.rutmiit.demo.dto.tourTrip.TourTripViewModel;
import edu.rutmiit.demo.dto.trip.*;
import edu.rutmiit.demo.dto.trip.forms.TripCreateForm;
import edu.rutmiit.demo.dto.trip.forms.TripEditForm;
import edu.rutmiit.demo.dto.trip.forms.TripShowForm;
import jakarta.validation.Valid;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Controller
@RequestMapping("/trip")
public class TripControllerImpl extends BaseControllerIpl implements TripController  {
    private static final Logger LOG = LogManager.getLogger(Controller.class);
    private TripService tripService;
    private TourService tourService;
    private DriverService driverService;
    private PassengerService passengerService;
    private BusService busService;

    @GetMapping()
    public String listTrips(Principal principal, Model model)throws NotFoundException {
        var tripPage = tripService.getTrips();
        DriverDTO driver =  null;
        BusDto bus = null;
        TourDTO tour = null;

        var tripViewModels = tripPage.stream()
        .map(tripDTO -> new TripViewModel(tripDTO.id(), tripDTO.tourId(), tripDTO.busId(), tripDTO.driverId(), tripDTO.startDate(), tripDTO.endDate(), tripDTO.tripStatus())
        ).toList();

       for(int i =0; i< tripViewModels.size(); i++){
           driver = driverService.getDriverById(tripViewModels.get(i).driverId());
           bus = busService.getBus(tripViewModels.get(i).busId());
           tour = tourService.getTourById(tripViewModels.get(i).tourId());
       }

        var viewModel = new TripListViewModel(
                createBaseViewModel("Список рейсов"),
                tripViewModels,
                new DriverViewModel(driver.id(), driver.name(), driver.surname(), driver.patronymic(), driver.birthday(), driver.passport(), driver.driverLicense(), driver.dateStartDriverLicense(), driver.category(), driver.driverStatus()),
                new BusViewModel(bus.id(), bus.mark(), bus.numberBus(), bus.numberSeats(), bus.classBus(), bus.year(), bus.busStatus()),
                new TourViewModel(tour.id(), tour.nameTour(), tour.description(), tour.itinerary(), tour.lengthTour(), tour.price(), tour.destination())
        );

        LOG.log(Level.INFO, "Show all trips for "+ principal.getName());
        model.addAttribute("model", viewModel);
        return "trip-list";
    }

    @Override
    @GetMapping("/{id}/passengers")
    public String tripPassengers(Principal principal, String id, TripShowForm form, Model model)throws NotFoundException {
        List<PassengerDTO> passengers = passengerService.getPassengersByTrip(id);
        var passengerViewModels = passengers.stream()
                .map(passengerDTO -> new PassengerViewModel(passengerDTO.id(), passengerDTO.name(), passengerDTO.surname(), passengerDTO.patronymic(), passengerDTO.birthday(), passengerDTO.identificationDocument(), passengerDTO.telephone(), passengerDTO.email(), passengerDTO.login(), passengerDTO.password())
                ).toList();
        var viewModel = new PassengerListViewModel(
                createBaseViewModel("Список пассажиров"),
                passengerViewModels
        );
        LOG.log(Level.INFO, "Show passengers trips for "+ principal.getName());
        model.addAttribute("model", viewModel);
        model.addAttribute("form", form);
        return "passenger-list";
    }

    @GetMapping("/{id}")
    public String details(Principal principal,String id, Model model)throws NotFoundException {
        var trip = tripService.getTripById(id);
        var viewModel = new TripDetailsViewModel(
                createBaseViewModel("Детали рейса"),
                new TripViewModel(trip.id(), trip.tourId(), trip.busId(), trip.driverId(), trip.startDate(), trip.endDate(), trip.tripStatus())
        );

        LOG.log(Level.INFO, "Show trip "+id+" for "+ principal.getName());
        model.addAttribute("model", viewModel);
        return "trip-details";
    }

    @Override
    @GetMapping("/create/{id}")
    public String createForm(Principal principal,@PathVariable String id,Model model)throws NotFoundException {
        TourDTO tour = tourService.getTourById(id);
        List<DriverViewModel> drivers = driverService.findAll().stream()
                .map(driverDTO -> new DriverViewModel(driverDTO.id(), driverDTO.name(), driverDTO.surname(), driverDTO.patronymic(), driverDTO.birthday(), driverDTO.passport(), driverDTO.driverLicense(), driverDTO.dateStartDriverLicense(), driverDTO.category(), driverDTO.driverStatus())).toList();
        List<BusViewModel> buses = busService.findAll().stream()
                .map(busDto -> new BusViewModel(busDto.id(), busDto.mark(), busDto.numberBus(), busDto.numberSeats(), busDto.classBus(), busDto.year(), busDto.busStatus())).toList();

        var viewModel = new TripCreateViewModel(
                createBaseViewModel("Создание рейса"),
                new TourViewModel(tour.id(), tour.nameTour(), tour.description(), tour.itinerary(), tour.lengthTour(), tour.price(), tour.destination()),
                drivers,
                buses
        );

        LOG.log(Level.INFO, "Show create form trip for "+ principal.getName());
        model.addAttribute("model", viewModel);
        model.addAttribute("form", new TripCreateForm(tour.id(), "", "", null, null, TripStatus.AWAITING));
        return "trip-create";
    }

    @Override
    @PostMapping("/create/{id}")
    public String create(Principal principal,
                         @PathVariable String id,
                         TripCreateForm form, BindingResult bindingResult, Model model) throws NotFoundException {
        TourDTO tour = tourService.getTourById(id);
        List<DriverViewModel> drivers = driverService.findAll().stream()
                .map(driverDTO -> new DriverViewModel(driverDTO.id(), driverDTO.name(), driverDTO.surname(), driverDTO.patronymic(), driverDTO.birthday(), driverDTO.passport(), driverDTO.driverLicense(), driverDTO.dateStartDriverLicense(), driverDTO.category(), driverDTO.driverStatus())).toList();
        List<BusViewModel> buses = busService.findAll().stream()
                .map(busDto -> new BusViewModel(busDto.id(), busDto.mark(), busDto.numberBus(), busDto.numberSeats(), busDto.classBus(), busDto.year(), busDto.busStatus())).toList();

        if (bindingResult.hasErrors()) {
            var viewModel = new TripCreateViewModel(
                    createBaseViewModel("Создание рейса"),
                    new TourViewModel(tour.id(), tour.nameTour(), tour.description(), tour.itinerary(), tour.lengthTour(), tour.price(), tour.destination()),
                    drivers,
                    buses
            );
            model.addAttribute("model", viewModel);
            model.addAttribute("form", form);
            return "trip-create";
        }

        TripDTO trip = tripService.createTrip(new TripCreateDTO(form.tourId(), form.busId(), form.driverId(), form.startDate(), form.endDate(), form.tripStatus()));
        LOG.log(Level.INFO, "Create trip for "+ principal.getName());
        return "redirect:/trip/"+trip.id();
    }

    @Override
    @GetMapping("/{id}/edit")
    public String editForm(Principal principal,String id, Model model)throws NotFoundException {
        var trip = tripService.getTripById(id);
        List<DriverViewModel> drivers = driverService.findFreeDriversByTrip(trip.startDate(), trip.endDate(), trip.tourId()).stream()
                .map(driverDTO -> new DriverViewModel(driverDTO.id(), driverDTO.name(), driverDTO.surname(), driverDTO.patronymic(), driverDTO.birthday(), driverDTO.passport(), driverDTO.driverLicense(), driverDTO.dateStartDriverLicense(), driverDTO.category(), driverDTO.driverStatus())).toList();
        List<BusViewModel> buses = busService.findAll().stream()
                .map(busDto -> new BusViewModel(busDto.id(), busDto.mark(), busDto.numberBus(), busDto.numberSeats(), busDto.classBus(), busDto.year(), busDto.busStatus())).toList();


        var viewModel = new TripEditViewModel(
                createBaseViewModel("Редактирование рейса"),
                drivers,
                buses);
        LOG.log(Level.INFO, "Show edit form trip for "+ principal.getName());
        model.addAttribute("model", viewModel);
        model.addAttribute("form",new TripEditForm(trip.id(), trip.tourId(), trip.busId(), trip.driverId(), trip.startDate().toString(), trip.endDate().toString(), trip.tripStatus()));
        return "trip-edit";
    }

    @Override
    @PostMapping("/{id}/edit")
    public String edit(Principal principal,@PathVariable String id,
                       @Valid @ModelAttribute("form") TripEditForm form,
                       BindingResult bindingResult, Model model)throws NotFoundException {
        List<DriverViewModel> drivers = driverService.findFreeDriversByTrip(LocalDate.parse(form.startDate()), LocalDate.parse(form.endDate()),id).stream()
                .map(driverDTO -> new DriverViewModel(driverDTO.id(), driverDTO.name(), driverDTO.surname(), driverDTO.patronymic(), driverDTO.birthday(), driverDTO.passport(), driverDTO.driverLicense(), driverDTO.dateStartDriverLicense(), driverDTO.category(), driverDTO.driverStatus())).toList();
        List<BusViewModel> buses = busService.findAll().stream()
                .map(busDto -> new BusViewModel(busDto.id(), busDto.mark(), busDto.numberBus(), busDto.numberSeats(), busDto.classBus(), busDto.year(), busDto.busStatus())).toList();

        if (bindingResult.hasErrors()) {
            var viewModel = new TripEditViewModel(
                    createBaseViewModel("Редактирование рейса"),
                    drivers,
                    buses
            );
            model.addAttribute("model", viewModel);
            model.addAttribute("form", form);
            return "trip-edit";
        }

        tripService.updateTrip(new TripUpdateDTO(id, form.tourId(), form.busId(), form.driverId(), LocalDate.parse(form.startDate()), LocalDate.parse(form.endDate()), form.tripStatus()));
        LOG.log(Level.INFO, "Edit trip for "+ principal.getName());
        return "redirect:/trip";
    }




    @GetMapping("/soon")
    public String getSoonTours (Principal principal, Model model)throws NotFoundException {
        var toursPage = tripService.findTourByDateTrip();
        var tourTripViewModels = toursPage.stream()
                .map(tourTripDTO -> new TourTripViewModel(tourTripDTO.id(), tourTripDTO.nameTour(), tourTripDTO.destination(), tourTripDTO.itinerary(), tourTripDTO.price(), tourTripDTO.startDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")), tourTripDTO.endDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))
                ).toList();

        var viewModel = new TourTripListViewModel(
                createBaseViewModel("Список туров"),
                tourTripViewModels
        );

        LOG.log(Level.INFO, "Show soon trip for "+ (principal != null ? principal.getName() : "anonymousUser"));
        model.addAttribute("model", viewModel);
        return "tour-soon-list";
    }

    @Autowired
    public void setTripService(TripService tripService) {
        this.tripService = tripService;
    }
    @Autowired
    public void setTourService(TourService tourService) {
        this.tourService = tourService;
    }
    @Autowired
    public void setDriverService(DriverService driverService) {
        this.driverService = driverService;
    }
    @Autowired
    public void setPassengerService(PassengerService passengerService) {
        this.passengerService = passengerService;
    }
    @Autowired
    public void setBusService(BusService busService) {
        this.busService = busService;
    }
}
