package edu.rutmiit.demo.controllers;

import edu.rutmiit.demo.dto.tour.forms.TourCreateForm;
import edu.rutmiit.demo.dto.tour.forms.TourEditForm;
import edu.rutmiit.demo.dto.tour.forms.TourSearchForm;
import jakarta.validation.Valid;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RequestMapping("/tours")
public interface TourController extends BaseController {
    @GetMapping
    String listTours(Principal principal,
                     Model model)throws Exception;

    @GetMapping("/{id}")
    String details(Principal principal,
            @RequestParam(value = "source", required = false) String source,
            @PathVariable String id,
            Model model
    )throws Exception;

    @GetMapping("/create")
    String createForm(Principal principal,Model model)throws Exception;

    @PostMapping("/create")
    String create(Principal principal,
            @Valid @ModelAttribute("form") TourCreateForm form,
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
            @Valid @ModelAttribute("form") TourEditForm form,
            BindingResult bindingResult,
            Model model
    )throws Exception;

    @GetMapping("/find")
    String findTour(Principal principal, @ModelAttribute("form") TourSearchForm form, Model model)throws Exception;

    @GetMapping("/top")
    String getTopTours (Principal principal, Model model)throws Exception;
}
