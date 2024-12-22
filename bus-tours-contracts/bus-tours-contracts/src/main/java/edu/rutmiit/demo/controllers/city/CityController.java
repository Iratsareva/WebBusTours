package edu.rutmiit.demo.controllers.city;

import edu.rutmiit.demo.controllers.base.BaseController;
import edu.rutmiit.demo.dto.bus.BusCreateForm;
import edu.rutmiit.demo.dto.bus.BusEditForm;
import edu.rutmiit.demo.dto.city.CityCreateForm;
import edu.rutmiit.demo.dto.city.CityEditForm;
import edu.rutmiit.demo.dto.city.CityShowForm;
import edu.rutmiit.demo.dto.city.CityToursViewModel;
import edu.rutmiit.demo.dto.tour.TourShowForm;
import jakarta.validation.Valid;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RequestMapping("/cities")
public interface CityController extends BaseController {

    @GetMapping
    String listCities(Principal principal,
                      @ModelAttribute("form") CityShowForm form,
                      Model model);

    @GetMapping("/{id}")
    String details(Principal principal,
            @ModelAttribute("form") TourShowForm form,
            @PathVariable String id,
            Model model
    );


    @GetMapping("/create")
    String createForm(Principal principal,Model model);


    @PostMapping("/create")
    String create(Principal principal,
            @Valid @ModelAttribute("form") CityCreateForm form,
            BindingResult bindingResult,
            Model model
    );

    @GetMapping("/{id}/edit")
    String editForm(Principal principal,
            @PathVariable String id,
            Model model
    );

    @PostMapping("/{id}/edit")
    String edit(Principal principal,
            @PathVariable String id,
            @Valid @ModelAttribute("form") CityEditForm form,
            BindingResult bindingResult,
            Model model
    );

}
