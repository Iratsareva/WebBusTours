package edu.rutmiit.demo.controllers.tour;

import edu.rutmiit.demo.controllers.base.BaseController;
import edu.rutmiit.demo.dto.bus.BusCreateForm;
import edu.rutmiit.demo.dto.bus.BusEditForm;
import edu.rutmiit.demo.dto.city.CityShowForm;
import edu.rutmiit.demo.dto.tour.*;
import jakarta.validation.Valid;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RequestMapping("/tours")
public interface TourController extends BaseController {

    @GetMapping
    String listTours(Principal principal,
                     @ModelAttribute("form") TourShowForm form,
                     Model model);

    @GetMapping("/{id}")
    String details(Principal principal,
            @RequestParam(value = "source", required = false) String source,
            @PathVariable String id,
            Model model
    );


    @GetMapping("/create")
    String createForm(Principal principal,Model model);

    @PostMapping("/create")
    String create(Principal principal,
            @Valid @ModelAttribute("form") TourCreateForm form,
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
            @Valid @ModelAttribute("form") TourEditForm form,
            BindingResult bindingResult,
            Model model
    );

    @GetMapping("/find")
    String findTour(Principal principal,@ModelAttribute("form") TourSearchForm form, Model model);

    @GetMapping("/top")
    String getTopTours (Principal principal,TourShowForm form, Model model);
}
