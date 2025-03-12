package edu.rutmiit.demo.controllers;


import edu.rutmiit.demo.dto.driver.forms.DriverCreateForm;
import edu.rutmiit.demo.dto.driver.forms.DriverEditForm;
import jakarta.validation.Valid;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RequestMapping("/drivers")
public interface DriverController extends BaseController {
    @GetMapping
    String listDrivers(Principal principal,
                      Model model)throws Exception;

    @GetMapping("/{id}")
    String details(Principal principal,@PathVariable String id,
            Model model
    )throws Exception;


    @GetMapping("/create")
    String createForm(Principal principal,Model model)throws Exception;

    @PostMapping("/create")
    String create(Principal principal,
            @Valid @ModelAttribute("form") DriverCreateForm form,
            BindingResult bindingResult,
            Model model
    )throws Exception;

    @GetMapping("/{id}/edit")
    String editForm(Principal principal,
            @PathVariable String id,
            Model model
    )throws Exception;

    @PostMapping("/{id}/edit")
    String edit(Principal principal,
            @PathVariable String id,
            @Valid @ModelAttribute("form") DriverEditForm form,
            BindingResult bindingResult,
            Model model
    )throws Exception;

    @GetMapping("/{id}/delete")
    String delete(Principal principal,@PathVariable String id)throws Exception;
}
