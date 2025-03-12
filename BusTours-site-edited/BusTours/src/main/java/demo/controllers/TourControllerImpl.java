package demo.controllers;

import demo.dto.tour.TourCreateDTO;
import demo.dto.tour.TourDTO;
import demo.dto.tour.TourUpdateDTO;
import demo.exception.NotFoundException;
import demo.services.*;
import edu.rutmiit.demo.controllers.TourController;
import edu.rutmiit.demo.dto.city.CityViewModel;
import edu.rutmiit.demo.dto.tour.*;
import edu.rutmiit.demo.dto.tour.forms.TourCreateForm;
import edu.rutmiit.demo.dto.tour.forms.TourEditForm;
import edu.rutmiit.demo.dto.tour.forms.TourSearchForm;
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
import java.util.*;

@Controller
@RequestMapping("/tour")
public class TourControllerImpl extends BaseControllerIpl implements TourController {
    private static final Logger LOG = LogManager.getLogger(Controller.class);
    private TourService tourService;
    private TripService tripService;
    private PassengerService passengerService;
    private CityService cityService;

    @GetMapping()
    public String listTours(Principal principal,
                           Model model)throws NotFoundException {
        var toursPage = tourService.getTours();
        var toursViewModels = toursPage.stream()
                .map(tourDTO -> {
                        return new TourViewModelShort(tourDTO.id(), tourDTO.nameTour(), tourDTO.itinerary(), tourDTO.lengthTour(), tourDTO.price(), tripService.findTripByTourString(tourDTO.id()));}).toList();

        var viewModel = new TourListViewModelShort(
                createBaseViewModel("Список городов"),
                toursViewModels
        );

        LOG.log(Level.INFO, "Show all tours for "+  (principal != null ? principal.getName() : "anonymousUser"));
        model.addAttribute("model", viewModel);

        if (principal!=null && (passengerService.isAdminPassengerByLogin(principal.getName()) || passengerService.isModerPassengerByLogin(principal.getName()))) {
            return "tour-list-admin";
        } else {
            return "tour-list-user";
        }
    }

    @Override
    @GetMapping("/top")
    public String getTopTours (Principal principal, Model model)throws NotFoundException {
        var toursPage = tourService.findTourByTickets();
        var toursViewModels = toursPage.stream()
                .map(tourDTO -> {
                        return new TourViewModelShort(tourDTO.id(), tourDTO.nameTour(), tourDTO.itinerary(), tourDTO.lengthTour(), tourDTO.price(), tripService.findTripByTourString(tourDTO.id()));
                }).toList();

        var viewModel = new TourListViewModelShort(
                createBaseViewModel("Список туров"),
                toursViewModels
        );
        LOG.log(Level.INFO, "Show top tour for "+  (principal != null ? principal.getName() : "anonymousUser"));
        model.addAttribute("model", viewModel);
        return "tour-top-list";
    }



    @Override
    @GetMapping("/{id}")
    public String details(Principal principal,@RequestParam(value = "source", required = false) String source,String id, Model model)throws NotFoundException {
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
    public String createForm(Principal principal,Model model) throws NotFoundException {
        List<CityViewModel> cities = cityService.findAll().stream()
                .map(cityDTO -> new CityViewModel(cityDTO.id(), cityDTO.nameCity(), cityDTO.description())).toList();

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
                         Model model) throws NotFoundException {
        List<CityViewModel> cities = cityService.findAll().stream()
                .map(cityDTO -> new CityViewModel(cityDTO.id(), cityDTO.nameCity(), cityDTO.description())).toList();

        if (bindingResult.hasErrors()) {
            var viewModel = new TourCreateViewModel(
                    createBaseViewModel("Создание тура"),
                    cities
            );
            model.addAttribute("model", viewModel);
            model.addAttribute("form", form);
            return "tour-create";
        }

        TourDTO tour = tourService.createTour(new TourCreateDTO(form.nameTour(), form.description(), form.itinerary(), form.lengthTour(), form.price(), form.destination()));
        LOG.log(Level.INFO, "Create tour for "+  principal.getName());
        return "redirect:/tour/"+ tour.id();
    }


    @Override
    @GetMapping("/{id}/edit")
    public String editForm(Principal principal,String id, Model model)throws NotFoundException {
        var tour = tourService.getTourById(id);
        List<CityViewModel> cities = cityService.findAll().stream()
                .map(cityDTO -> new CityViewModel(cityDTO.id(), cityDTO.nameCity(), cityDTO.description())).toList();

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
                       BindingResult bindingResult, Model model) throws NotFoundException {
        List<CityViewModel> cities = cityService.findAll().stream()
                .map(cityDTO -> new CityViewModel(cityDTO.id(), cityDTO.nameCity(), cityDTO.description())).toList();


        if (bindingResult.hasErrors()) {
            var viewModel = new TourEditViewModel(
                    createBaseViewModel("Редактирование тура"),
                    cities
            );
            model.addAttribute("model", viewModel);
            model.addAttribute("form", form);
            return "tour-edit";
        }

        tourService.updateTour(new TourUpdateDTO(id, form.nameTour(), form.description(), form.itinerary(), form.lengthTour(), form.price(), form.destination()));
        LOG.log(Level.INFO, "Edit tour  for "+  principal.getName());
        return "redirect:/tour";
    }


    @Override
    @GetMapping("/find")
    public String findTour(Principal principal, @ModelAttribute("form") TourSearchForm form, Model model)throws NotFoundException {
        var destination =form.searchDestination();
        var startDate = form.searchStartDate();
        var lengthTour = form.searchLengthTour();
        List<TourDTO> toursPage;

        if( destination ==null && startDate ==null && lengthTour==null){
            toursPage = tourService.getTours();
        }else {
            toursPage = tourService.findTourByDestinationAndStartDateAndLengthTour(destination, startDate,lengthTour);
        }

        var toursViewModels = toursPage.stream()
                .map(tourDTO -> new TourViewModelShort(tourDTO.id(), tourDTO.nameTour(), tourDTO.itinerary(), tourDTO.lengthTour(), tourDTO.price(), tripService.findTripByTourString(tourDTO.id())
                )).toList();

        var viewModel = new TourListViewModelShort(
                createBaseViewModel("Список городов"),
                toursViewModels
        );

        LOG.log(Level.INFO, "Find tours  for "+  (principal != null ? principal.getName() : "anonymousUser"));
        model.addAttribute("model", viewModel);
        model.addAttribute("form", form);
        return "tour-find-list";
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
    public void setPassengerService(PassengerService passengerService) {
        this.passengerService = passengerService;
    }
    @Autowired
    public void setCityService(CityService cityService) {
        this.cityService = cityService;
    }

}
