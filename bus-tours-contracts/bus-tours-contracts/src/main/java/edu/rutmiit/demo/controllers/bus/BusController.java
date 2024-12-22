package edu.rutmiit.demo.controllers.bus;

import edu.rutmiit.demo.controllers.base.BaseController;
import edu.rutmiit.demo.dto.bus.BusCreateForm;
import edu.rutmiit.demo.dto.bus.BusEditForm;
import edu.rutmiit.demo.dto.bus.BusSearchForm;
import jakarta.validation.Valid;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RequestMapping("/bus")
public interface BusController extends BaseController {
    @GetMapping
    String listBuses(
            Principal principal,
            @ModelAttribute("form") BusSearchForm form,
            Model model
    );

    @GetMapping("/{id}")
    String details(
            Principal principal,
            @PathVariable String id,
            Model model
    );

    @GetMapping("/create")
    String createForm(Principal principal,Model model);

    @PostMapping("/create")
    String create(Principal principal,
            @Valid @ModelAttribute("form") BusCreateForm form,
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
            @Valid @ModelAttribute("form") BusEditForm form,
            BindingResult bindingResult,
            Model model
    );


    @GetMapping("/{id}/delete")
    String delete(Principal principal,@PathVariable String id);
}