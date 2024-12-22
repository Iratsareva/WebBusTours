package demo.controllers;


import demo.services.CityService;
import demo.services.PassengerService;
import demo.services.TourService;
import edu.rutmiit.demo.controllers.city.CityController;
import edu.rutmiit.demo.dto.base.BaseViewModel;
import edu.rutmiit.demo.dto.city.*;
import edu.rutmiit.demo.dto.tour.TourShowForm;
import jakarta.validation.Valid;
import demo.dto.CityDTO;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/city")
public class CityControllerImpl implements CityController {
    private static final Logger LOG = LogManager.getLogger(Controller.class);

    @Autowired
    private CityService cityService;
    @Autowired
    private TourService tourService;
    @Autowired
    private PassengerService passengerService;

    public CityControllerImpl(CityService cityService) {
        this.cityService = cityService;
    }



    @GetMapping()
    public String listCities(Principal principal,
                             @ModelAttribute("form") CityShowForm form,
                             Model model) {
        var page = form.page() != null ? form.page() : 1;
        var size = form.size() != null ? form.size() : 3;

        var citiesPage = cityService.getCities(page, size);
        var citiesViewModels = citiesPage.stream()
                .map(c -> new CityNameViewModel(c.id(), c.nameCity(), c.description())).toList();

        var viewModel = new CityListViewModel(
                createBaseViewModel("Список городов"),
                citiesViewModels,
                citiesPage.getTotalPages()
        );
        LOG.log(Level.INFO, "Show all cities for "+  (principal != null ? principal.getName() : "anonymousUser"));
        model.addAttribute("model", viewModel);
        model.addAttribute("form", form);


        if (principal != null && (passengerService.isAdminPassengerByLogin(principal.getName()))) {
            return "city-list-admin";
        } else {
            return "city-list-user";
        }
    }






    @Override
    @GetMapping("/{id}")
    public String details(Principal principal,
                          TourShowForm form,
                          String id, Model model) {
        var page = form.page() != null ? form.page() : 1;
        var size = form.size() != null ? form.size() : 3;

        var city = cityService.getCityById(id);

        var toursPage = tourService.findTourByDestination(city.nameCity(), page, size);

        var toursViewModels = toursPage.stream()
                .map(t -> new CityToursViewModel(t.id(), t.nameTour(), t.itinerary(), t.lengthTour(), t.price()
                )).toList();

        var viewModel = new CityTourListViewModel(
                createBaseViewModel("О городе"),
                new CityViewModel(city.id(), city.nameCity(), city.description()),
                toursViewModels,
                toursPage.getTotalPages()
        );
        LOG.log(Level.INFO, "Show city "+id+" for "+  (principal != null ? principal.getName() : "anonymousUser"));
        model.addAttribute("model", viewModel);
        model.addAttribute("form", form);


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

        CityDTO city = cityService.createCity(form.nameCity(), form.description());
        LOG.log(Level.INFO, "Create city for "+  principal.getName());
        return "redirect:/city";
    }

    @Override
    @GetMapping("/{id}/edit")
    public String editForm(Principal principal,String id, Model model) {

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
                       BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            var viewModel = new CityEditViewModel(
                    createBaseViewModel("Редактирование города")
            );
            model.addAttribute("model", viewModel);
            model.addAttribute("form", form);
            return "city-edit";
        }

        cityService.updateCity(id, form.nameCity(), form.description());
        LOG.log(Level.INFO, "Edit city for "+  principal.getName());
        return "redirect:/city";
    }




    @Override
    public BaseViewModel createBaseViewModel(String title) {
        return new BaseViewModel(title);
    }



}
