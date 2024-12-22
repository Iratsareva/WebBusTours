package edu.rutmiit.demo.controllers.driver;


import edu.rutmiit.demo.controllers.base.BaseController;
import edu.rutmiit.demo.dto.bus.BusCreateForm;
import edu.rutmiit.demo.dto.bus.BusEditForm;
import edu.rutmiit.demo.dto.city.CityShowForm;
import edu.rutmiit.demo.dto.driver.DriverCreateForm;
import edu.rutmiit.demo.dto.driver.DriverEditForm;
import edu.rutmiit.demo.dto.driver.DriverShowForm;
import edu.rutmiit.demo.dto.tour.TourShowForm;
import jakarta.validation.Valid;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RequestMapping("/drivers")
public interface DriverController extends BaseController {
    @GetMapping
    String listDrivers(Principal principal, @ModelAttribute("form") DriverShowForm form,
                      Model model);

    @GetMapping("/{id}")
    String details(Principal principal,@PathVariable String id,
            Model model
    );


    @GetMapping("/create")
    String createForm(Principal principal,Model model);

    @PostMapping("/create")
    String create(Principal principal,
            @Valid @ModelAttribute("form") DriverCreateForm form,
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
            @Valid @ModelAttribute("form") DriverEditForm form,
            BindingResult bindingResult,
            Model model
    );


    @GetMapping("/{id}/delete")
    String delete(Principal principal,@PathVariable String id);
}
