package demo.controllers;

import demo.dto.passenger.PassengerDTO;
import demo.dto.passenger.PassengerUpdateDTO;
import demo.dto.ticket.TicketCreateDTO;
import demo.dto.ticket.TicketDTO;
import demo.dto.tour.TourDTO;
import demo.exception.NotFoundException;
import demo.services.*;
import edu.rutmiit.demo.controllers.TicketController;
import edu.rutmiit.demo.dto.bus.BusTicketViewModel;
import edu.rutmiit.demo.dto.driver.DriverTicketViewModel;
import edu.rutmiit.demo.dto.passenger.PassengerTicketViewModel;
import edu.rutmiit.demo.dto.ticket.*;
import edu.rutmiit.demo.dto.ticket.forms.TicketCreateForm;
import edu.rutmiit.demo.dto.tour.TourViewModel;
import edu.rutmiit.demo.dto.tour.TourViewModelShort;
import edu.rutmiit.demo.dto.trip.TripViewModel;
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
import java.util.List;

@Controller
@RequestMapping("/ticket")
public class TicketControllerImpl extends BaseControllerIpl implements TicketController {
    private static final Logger LOG = LogManager.getLogger(Controller.class);
    private TicketService ticketService;
    private PassengerService passengerService;
    private TourService tourService;
    private TripService tripService;
    private DriverService driverService;
    private BusService busService;


    @GetMapping()
    public String listTickets(Principal principal, Model model)throws NotFoundException {
        String username = principal.getName();
        PassengerDTO passenger = passengerService.getPassengerByLogin(username);

        List<TicketDTO> ticketPage;
        if (passengerService.isAdminPassengerByLogin(username) || passengerService.isModerPassengerByLogin(username)) {
            ticketPage = ticketService.getTickets();
        } else {
            ticketPage = ticketService.getTicketsUser(passenger.id());
        }

        var ticketViewModels = ticketPage.stream()
                .map(ticketDTO -> {
                            try {
                                return new TicketDetailListViewModel(ticketDTO.id(), ticketDTO.price(), ticketDTO.ticketStatus(),
                                        passengerService.getPassengerById(ticketDTO.passengerId()).name(),
                                        passengerService.getPassengerById(ticketDTO.passengerId()).surname(),
                                        passengerService.getPassengerById(ticketDTO.passengerId()).patronymic(),
                                        ticketDTO.tripId(),
                                        tripService.getTripById(ticketDTO.tripId()).startDate(),
                                        tripService.getTripById(ticketDTO.tripId()).endDate(),
                                        tourService.getTourById(tripService.getTripById(ticketDTO.tripId()).tourId()).nameTour());
                            } catch (NotFoundException e) {
                                throw new RuntimeException(e);
                            }
                        }
                ).toList();

        var viewModel = new TicketListViewModel(
                createBaseViewModel("Список билетов"),
                ticketViewModels
        );

        LOG.log(Level.INFO, "Show all tickets for "+ principal.getName());
        model.addAttribute("model", viewModel);

        if (passengerService.isAdminPassengerByLogin(username)|| passengerService.isModerPassengerByLogin(username)) {
            return "ticket-list-admin";
        } else {
            return "ticket-list-user";
        }

    }

    @Override
    @GetMapping("/{id}")
    public String details(Principal principal,String id, Model model)throws NotFoundException {
        var ticket = ticketService.getTicketById(id);
        var passenger = passengerService.getPassengerById(ticket.passengerId());
        var trip = tripService.getTripById(ticket.tripId());
        var driver = driverService.getDriverById(trip.driverId());
        var bus = busService.getBus(trip.driverId());
        var tour = tourService.getTourById(trip.tourId());

        var viewModel = new TicketDetailsViewModel(
                createBaseViewModel("Детали билета"),
                new TicketViewModel(ticket.id(), ticket.passengerId(), ticket.tripId(), ticket.price(), ticket.ticketStatus()),
                new PassengerTicketViewModel(passenger.id(), passenger.name(), passenger.surname(), passenger.patronymic()),
                new TripViewModel(trip.id(), trip.tourId(), trip.busId(), trip.driverId(), trip.startDate(), trip.endDate(), trip.tripStatus()),
                new DriverTicketViewModel(driver.id(), driver.name(), driver.surname(), driver.patronymic()),
                new BusTicketViewModel(bus.id(), bus.mark(), bus.numberBus()),
                new TourViewModelShort(tour.id(), tour.nameTour(), tour.itinerary(), tour.lengthTour(), tour.price(), null)
        );
        LOG.log(Level.INFO, "Show ticket "+id+" for "+ principal.getName());
        model.addAttribute("model", viewModel);
        return "ticket-details";
    }



    @Override
    @GetMapping("/create/{id}")
    public String createForm(Principal principal,@PathVariable String id, Model model)throws NotFoundException {
        TourDTO tour = tourService.getTourById(id);
        PassengerDTO passenger = passengerService.getPassengerByLogin(principal.getName());
        List<TripViewModel> trips = tripService.findTripByTour(tour.id()).stream()
                .map(tripDTO -> new TripViewModel(tripDTO.id(), tripDTO.tourId(), tripDTO.busId(), tripDTO.driverId(), tripDTO.startDate(), tripDTO.endDate(), tripDTO.tripStatus())).toList();

        var viewModel = new TicketCreateViewModel(
                createBaseViewModel("Создание билета"),
                new TourViewModel(tour.id(), tour.nameTour(), tour.description(), tour.itinerary(), tour.lengthTour(), tour.price(), tour.destination()),
                trips
        );

        LOG.log(Level.INFO, "Show create form ticket for "+ principal.getName());
        model.addAttribute("model", viewModel);
        model.addAttribute("form", new TicketCreateForm(
                "", passenger.id(),"",tour.price(), passenger.surname(), passenger.name(), passenger.patronymic(),passenger.birthday() != null ?passenger.birthday().toString():"", passenger.identificationDocument(), passenger.telephone(), passenger.email()));
        return "ticket-create";
    }

    @Override
    @PostMapping("/create/{id}")
    public String create(Principal principal,
            @PathVariable String id,
            TicketCreateForm form, BindingResult bindingResult, Model model)throws NotFoundException {
        TourDTO tour = tourService.getTourById(id);
        PassengerDTO passenger = passengerService.getPassengerByLogin(principal.getName());
        List<TripViewModel> trips = tripService.findTripByTour(tour.id()).stream()
                .map(tripDTO -> new TripViewModel(tripDTO.id(), tripDTO.tourId(), tripDTO.busId(), tripDTO.driverId(), tripDTO.startDate(), tripDTO.endDate(), tripDTO.tripStatus())).toList();

        if (bindingResult.hasErrors()) {
            var viewModel = new TicketCreateViewModel(
                    createBaseViewModel("Создание билета"),
                    new TourViewModel(tour.id(), tour.nameTour(), tour.description(), tour.itinerary(), tour.lengthTour(), tour.price(), tour.destination()),
                    trips
            );

            model.addAttribute("model", viewModel);
            model.addAttribute("form", form);
            return "ticket-create";
        }

        TicketDTO ticket = ticketService.createTicket(new TicketCreateDTO(passenger.id(), form.tripId()));
        LOG.log(Level.INFO, " Create ticket for "+ principal.getName());
        if (ticket == null) {
            model.addAttribute("errorMessage", "Вы уже оформили билет на данный рейс");
            var viewModel = new TicketCreateViewModel(
                    createBaseViewModel("Создание билета"),
                    new TourViewModel(tour.id(), tour.nameTour(), tour.description(), tour.itinerary(), tour.lengthTour(), tour.price(), tour.destination()),
                    trips
                    );

            LOG.log(Level.INFO, "post Create ticket for "+ principal.getName());
            model.addAttribute("model", viewModel);
            model.addAttribute("form", form);
            return "ticket-create";
        }else {
            passengerService.updatePassenger(new PassengerUpdateDTO(form.name(), form.surname(), form.patronymic(), LocalDate.parse(form.birthday()), form.identificationDocument(), form.telephone(), principal.getName()));
            return "redirect:/ticket/" + ticket.id();
        }
    }

    @Override
    @GetMapping("/{id}/delete")
    public String delete(Principal principal,String id)throws NotFoundException {
        ticketService.deleteTicket(id);
        LOG.log(Level.INFO, "Delete ticket for "+ principal.getName());
        return "redirect:/ticket";
    }

    @Override
    @GetMapping("/{id}/confirm")
    public String confirm(Principal principal,String id)throws NotFoundException {
        ticketService.confirmTicket(id);
        LOG.log(Level.INFO, "Confirm ticket for "+ principal.getName());
        return "redirect:/ticket";
    }

    @Autowired
    public void setTicketService(TicketService ticketService) {
        this.ticketService = ticketService;
    }
    @Autowired
    public void setPassengerService(PassengerService passengerService) {
        this.passengerService = passengerService;
    }
    @Autowired
    public void setTourService(TourService tourService) {
        this.tourService = tourService;
    }
    @Autowired
    public void setTripService(TripService tripService) {
        this.tripService = tripService;
    }
    @Autowired
    public void setDriverService(DriverService driverService) {
        this.driverService = driverService;
    }
    @Autowired
    public void setBusService(BusService busService) {
        this.busService = busService;
    }
}
