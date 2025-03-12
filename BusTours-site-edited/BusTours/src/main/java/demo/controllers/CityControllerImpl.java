package demo.controllers;


import demo.dto.city.CityCreateDTO;
import demo.dto.city.CityUpdateDTO;
import demo.exception.NotFoundException;
import demo.services.CityService;
import demo.services.PassengerService;
import demo.services.TourService;
import edu.rutmiit.demo.controllers.CityController;
import edu.rutmiit.demo.dto.city.*;
import edu.rutmiit.demo.dto.city.forms.CityCreateForm;
import edu.rutmiit.demo.dto.city.forms.CityEditForm;
import jakarta.validation.Valid;
import demo.dto.city.CityDTO;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/city")
public class CityControllerImpl extends BaseControllerIpl implements CityController {
    private static final Logger LOG = LogManager.getLogger(Controller.class);
    private CityService cityService;
    private TourService tourService;
    private PassengerService passengerService;

    @GetMapping()
    public String listCities(Principal principal,
                             Model model)throws NotFoundException {
        var citiesPage = cityService.getCities();
        var citiesViewModels = citiesPage.stream()
                .map(cityDTO -> new CityNameViewModel(cityDTO.id(), cityDTO.nameCity(), cityDTO.description())).toList();

        var viewModel = new CityListViewModel(
                createBaseViewModel("Список городов"),
                citiesViewModels
        );
        LOG.log(Level.INFO, "Show all cities for "+  (principal != null ? principal.getName() : "anonymousUser"));
        model.addAttribute("model", viewModel);


        if (principal != null && (passengerService.isAdminPassengerByLogin(principal.getName()))) {
            return "city-list-admin";
        } else {
            return "city-list-user";
        }
    }

    @Override
    @GetMapping("/{id}")
    public String details(Principal principal,
                          String id, Model model)throws NotFoundException {

        var city = cityService.getCityById(id);
        var toursPage = tourService.findTourByDestination(city.nameCity());
        var toursViewModels = toursPage.stream()
                .map(tourDTO -> new CityToursViewModel(tourDTO.id(), tourDTO.nameTour(), tourDTO.itinerary(), tourDTO.lengthTour(), tourDTO.price()
                )).toList();

        var viewModel = new CityTourListViewModel(
                createBaseViewModel("О городе"),
                new CityViewModel(city.id(), city.nameCity(), city.description()),
                toursViewModels
        );
        LOG.log(Level.INFO, "Show city "+id+" for "+  (principal != null ? principal.getName() : "anonymousUser"));
        model.addAttribute("model", viewModel);


        if (principal!=null && (passengerService.isAdminPassengerByLogin(principal.getName()) || passengerService.isModerPassengerByLogin(principal.getName()))) {
            return "city-details-admin";
        } else {
            return "city-details";
        }
    }




    @Override
    @GetMapping("/create")
    public String createForm(Principal principal,Model model) {
        var viewModel = new CityCreateViewModel(
                createBaseViewModel("Создание города")
        );

        LOG.log(Level.INFO, "Show create form city for "+  principal.getName());
        model.addAttribute("model", viewModel);
        model.addAttribute("form", new CityCreateForm("", ""));

        return "city-create";
    }




    @Override
    @PostMapping("/create")
    public String create(Principal principal,CityCreateForm form,
                         BindingResult bindingResult,
                         Model model) {

        if (bindingResult.hasErrors()) {
            var viewModel = new CityCreateViewModel(
                    createBaseViewModel("Создание города")
            );

            model.addAttribute("model", viewModel);
            model.addAttribute("form", form);
            return "city-create";
        }

        CityDTO city = cityService.createCity(new CityCreateDTO(form.nameCity(), form.description()));
        LOG.log(Level.INFO, "Create city for "+  principal.getName());
        return "redirect:/city";
    }

    @Override
    @GetMapping("/{id}/edit")
    public String editForm(Principal principal,String id, Model model)throws NotFoundException {

        var city = cityService.getCityById(id);
        var viewModel = new CityEditViewModel(
                createBaseViewModel("Редактирование города")
        );
        LOG.log(Level.INFO, "get Edit city for "+  principal.getName());
        model.addAttribute("model", viewModel);
        model.addAttribute("form",new CityEditForm(city.id(), city.nameCity(), city.description()));



        return "city-edit";
    }

    @Override
    @PostMapping("/{id}/edit")
    public String edit(Principal principal,@PathVariable String id,
                       @Valid @ModelAttribute("form") CityEditForm form,
                       BindingResult bindingResult, Model model)throws NotFoundException {

        if (bindingResult.hasErrors()) {
            var viewModel = new CityEditViewModel(
                    createBaseViewModel("Редактирование города")
            );
            model.addAttribute("model", viewModel);
            model.addAttribute("form", form);
            return "city-edit";
        }

        cityService.updateCity(new CityUpdateDTO(id, form.nameCity(), form.description()));
        LOG.log(Level.INFO, "Edit city for "+  principal.getName());
        return "redirect:/city";
    }

    @Autowired
    public void setCityService(CityService cityService) {
        this.cityService = cityService;
    }
    @Autowired
    public void setTourService(TourService tourService) {
        this.tourService = tourService;
    }
    @Autowired
    public void setPassengerService(PassengerService passengerService) {
        this.passengerService = passengerService;
    }
}
