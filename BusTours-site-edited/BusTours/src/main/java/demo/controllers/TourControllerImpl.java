package demo.controllers;

import demo.dto.CityDTO;
import demo.dto.PassengerDTO;
import demo.dto.TourDTO;
import demo.dto.TripDTO;
import demo.services.*;
import edu.rutmiit.demo.controllers.tour.TourController;
import edu.rutmiit.demo.dto.base.BaseViewModel;
import edu.rutmiit.demo.dto.city.CityViewModel;
import edu.rutmiit.demo.dto.tour.*;
import edu.rutmiit.demo.dto.trip.TripViewModel;
import edu.rutmiit.demo.controllers.tour.TourController;
import jakarta.validation.Valid;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/tour")
public class TourControllerImpl implements TourController {
    private static final Logger LOG = LogManager.getLogger(Controller.class);


    @Autowired
    private TourService tourService;

    @Autowired
    private TripService tripService;

    @Autowired
    private TicketService ticketService;

    @Autowired
    private PassengerService passengerService;

    @Autowired
    private CityService cityService;

    public TourControllerImpl(TourService tourService) {
        this.tourService = tourService;
    }



    @Override
    public BaseViewModel createBaseViewModel(String title) {
        return new BaseViewModel(title);
    }


    @GetMapping()
    public String listTours(Principal principal,
                            TourShowForm form, Model model) {
        var page = form.page() != null ? form.page() : 1;
        var size = form.size() != null ? form.size() : 3;

        var toursPage = tourService.getTours(page, size);

       var trips = toursPage.stream().map(tour -> tripService.findTripByTourString(tour.id())).toList();


        var toursViewModels = toursPage.stream()
                .map(t -> new TourViewModelShort(t.id(), t.nameTour(), t.itinerary(), t.lengthTour(), t.price(), tripService.findTripByTourString(t.id()))).toList();


        var viewModel = new TourListViewModelShort(
                createBaseViewModel("Список городов"),
                toursViewModels,
                toursPage.getTotalPages()
        );

        LOG.log(Level.INFO, "Show all tours for "+  (principal != null ? principal.getName() : "anonymousUser"));
        model.addAttribute("model", viewModel);
        model.addAttribute("form", form);


        if (principal!=null && (passengerService.isAdminPassengerByLogin(principal.getName()) || passengerService.isModerPassengerByLogin(principal.getName()))) {
            return "tour-list-admin";
        } else {
            return "tour-list-user";
        }
    }

    @Override
    @GetMapping("/top")
    public String getTopTours (Principal principal,TourShowForm form, Model model){
        var toursPage = tourService.findTourByTickets();
        var toursViewModels = toursPage.stream()
                .map(t -> new TourViewModelShort(t.id(), t.nameTour(), t.itinerary(), t.lengthTour(), t.price(), null
                )).toList();


        var viewModel = new TourListViewModelShort(
                createBaseViewModel("Список туров"),
                toursViewModels,
                toursPage.getTotalPages()
        );
        LOG.log(Level.INFO, "Show top tour for "+  (principal != null ? principal.getName() : "anonymousUser"));
        model.addAttribute("model", viewModel);
        model.addAttribute("form", form);
        return "tour-top-list";
    }



    @Override
    @GetMapping("/{id}")
    public String details(Principal principal,@RequestParam(value = "source", required = false) String source,String id, Model model) {
        var tour = tourService.getTourById(id);
        var viewModel = new TourListViewModel(
                createBaseViewModel("О городе"),
                new TourViewModel(tour.id(), tour.nameTour(), tour.description(), tour.itinerary(), tour.lengthTour(), tour.price(), tour.destination()),
                tripService.findTripByTourString(id)
        );


        LOG.log(Level.INFO, "Show tour "+id+" for "+  (principal != null ? principal.getName() : "anonymousUser"));
        model.addAttribute("model", viewModel);

        if ("admin".equals(source)) {
            return "tour-details-admin";
        } else {
            return "tour-details";
        }
    }




    @Override
    @GetMapping("/create")
    public String createForm(Principal principal,Model model) {
        List<CityViewModel> cities = cityService.findAll().stream()
                .map(c -> new CityViewModel(c.id(), c.nameCity(), c.description())).toList();

        var viewModel = new TourCreateViewModel(
                createBaseViewModel("Создание тура"),
                cities
        );
        LOG.log(Level.INFO, "Show create form tour  for "+  principal.getName());
        model.addAttribute("model", viewModel);
        model.addAttribute("form",new TourCreateForm("", "", "", 0, 0, ""));
        return "tour-create";
    }

    @Override
    @PostMapping("/create")
    public String create(Principal principal,TourCreateForm form,
                         BindingResult bindingResult,
                         Model model) {
        List<CityViewModel> cities = cityService.findAll().stream()
                .map(c -> new CityViewModel(c.id(), c.nameCity(), c.description())).toList();


        if (bindingResult.hasErrors()) {
            var viewModel = new TourCreateViewModel(
                    createBaseViewModel("Создание тура"),
                    cities
            );
            model.addAttribute("model", viewModel);
            model.addAttribute("form", form);
            return "tour-create";
        }

        TourDTO tour = tourService.createTour(form.nameTour(), form.description(), form.itinerary(), form.lengthTour(), form.price(), form.destination());
        LOG.log(Level.INFO, "Create tour for "+  principal.getName());
        return "redirect:/tour/"+ tour.id();
    }


    @Override
    @GetMapping("/{id}/edit")
    public String editForm(Principal principal,String id, Model model) {
        var tour = tourService.getTourById(id);
        List<CityViewModel> cities = cityService.findAll().stream()
                .map(c -> new CityViewModel(c.id(), c.nameCity(), c.description())).toList();

        var viewModel = new TourEditViewModel(
                createBaseViewModel("Редактирование тура"),
                cities
        );

        LOG.log(Level.INFO, "Show edit form tour  for "+  principal.getName());
        model.addAttribute("model", viewModel);
        model.addAttribute("form", new TourEditForm(tour.id(), tour.nameTour(), tour.description(), tour.itinerary(), tour.lengthTour(), tour.price(), tour.destination()));
        return "tour-edit";

    }

    @Override
    public String edit(Principal principal,@PathVariable String id,
                       @Valid @ModelAttribute("form") TourEditForm form,
                       BindingResult bindingResult, Model model) {
        List<CityViewModel> cities = cityService.findAll().stream()
                .map(c -> new CityViewModel(c.id(), c.nameCity(), c.description())).toList();


        if (bindingResult.hasErrors()) {
            var viewModel = new TourEditViewModel(
                    createBaseViewModel("Редактирование тура"),
                    cities
            );
            model.addAttribute("model", viewModel);
            model.addAttribute("form", form);
            return "tour-edit";
        }

        tourService.updateTour(id, form.nameTour(), form.description(), form.itinerary(), form.lengthTour(), form.price(), form.destination());
        LOG.log(Level.INFO, "Edit tour  for "+  principal.getName());
        return "redirect:/tour";
    }


    @Override
    @GetMapping("/find")
    public String findTour(Principal principal,@ModelAttribute("form") TourSearchForm form, Model model){
        var destination =form.searchDestination();
        var startDate = form.searchStartDate();
        var lengthTour = form.searchLengthTour();

        Page<TourDTO> toursPage;

        if( destination ==null && startDate ==null && lengthTour==null){
            toursPage = tourService.getTours(1, tourService.findAll().size());
        }else {
            toursPage = tourService.findTourByDestinationAndStartDateAndLengthTour(destination, startDate,lengthTour);
        }

        var toursViewModels = toursPage.stream()
                .map(t -> new TourViewModelShort(t.id(), t.nameTour(), t.itinerary(), t.lengthTour(), t.price(), null
                )).toList();

        var viewModel = new TourListViewModelShort(
                createBaseViewModel("Список городов"),
                toursViewModels,
                toursPage.getTotalPages()
                );

        LOG.log(Level.INFO, "Find tours  for "+  (principal != null ? principal.getName() : "anonymousUser"));
        model.addAttribute("model", viewModel);
        model.addAttribute("form", form);
        return "tour-find-list";
    }
}
