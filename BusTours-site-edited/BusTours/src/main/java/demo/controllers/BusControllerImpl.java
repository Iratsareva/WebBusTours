package demo.controllers;

import demo.dto.BusDto;
import demo.services.BusService;
import edu.rutmiit.demo.controllers.bus.BusController;
import edu.rutmiit.demo.dto.base.BaseViewModel;
import edu.rutmiit.demo.dto.bus.*;
import jakarta.validation.Valid;
import org.apache.juli.logging.Log;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;


@Controller
@RequestMapping("/bus")
public class BusControllerImpl implements BusController {
    private static final Logger LOG = LogManager.getLogger(Controller.class);
    private BusService busService;
    public BusControllerImpl(BusService busService) {
        this.busService = busService;
    }

    @GetMapping()
    public String listBuses(
            Principal principal,
            @ModelAttribute("form") BusSearchForm form,
            Model model) {
        var page = form.page() != null ? form.page() : 1;
        var size = form.size() != null ? form.size() : 3;

        form = new BusSearchForm(page, size);

        var busesPage = busService.getBus( page, size);
        var busViewModels = busesPage.stream()
                .map(b -> new BusViewModel(b.id(), b.mark(), b.numberBus(), b.numberSeats(), b.classBus(), b.year(), b.busStatus()))
                .toList();

        var viewModel = new BusListViewModel(
                createBaseViewModel("Список автобусов"),
                busViewModels,
                busesPage.getTotalPages()
        );

        LOG.log(Level.INFO, "Show all buses from "+ principal.getName());
        model.addAttribute("model", viewModel);
        model.addAttribute("form", form);
        return "bus-list";
    }



    @Override
    @GetMapping("/{id}")
    public String details(Principal principal,String id, Model model) {
        var bus = busService.getBus(id);

        var viewModel = new BusDetailsViewModel(
                createBaseViewModel("Детали автобуса"),
                new BusViewModel(bus.id(), bus.mark(), bus.numberBus(), bus.numberSeats(), bus.classBus(), bus.year(), bus.busStatus())
        );
        LOG.log(Level.INFO, "Show bus "+id+" from "+ principal.getName());
        model.addAttribute("model", viewModel);
        return "bus-details";
    }



    @Override
    @GetMapping("/create")
    public String createForm(Principal principal,Model model) {
        var viewModel = new BusCreateViewModel(
                createBaseViewModel("Создание автобуса")
        );

        LOG.log(Level.INFO, "Show create form bus from "+ principal.getName());
        model.addAttribute("model", viewModel);
        model.addAttribute("form", new BusCreateForm("", "", 1, "", null));
        return "bus-create";
    }

    @Override
    @PostMapping("/create")
    public String create(Principal principal,@Valid @ModelAttribute("form") BusCreateForm form,
                         BindingResult bindingResult,
                         Model model) {

        if (bindingResult.hasErrors()) {
            var viewModel = new BusCreateViewModel(
                    createBaseViewModel("Создание автобуса")
            );

            model.addAttribute("model", viewModel);
            model.addAttribute("form", form);
            return "bus-create";
        }


        BusDto bus = busService.createBus(form.mark(), form.numberBus(), form.numberSeats(), form.classBus(), form.year());
        LOG.log(Level.INFO, "Create bus from "+ principal.getName());
        return "redirect:/bus/" + bus.id();
    }

    @Override
    @GetMapping("/{id}/edit")
    public String editForm(Principal principal,String id, Model model) {

        var bus = busService.getBus(id);

        var viewModel = new BusEditViewModel(
                createBaseViewModel("Редактирование автобуса")
        );
        LOG.log(Level.INFO, "Show edit form bus from "+ principal.getName());
        model.addAttribute("model", viewModel);
        model.addAttribute("form", new BusEditForm(bus.id(), bus.mark(), bus.numberBus(), bus.numberSeats(), bus.classBus(), bus.year()));
        return "bus-edit";
    }

    @Override
    @PostMapping("/{id}/edit")
    public String edit(Principal principal,@PathVariable String id,
                       @Valid @ModelAttribute("form") BusEditForm form,
                       BindingResult bindingResult, Model model) {


        if (bindingResult.hasErrors()) {
            var viewModel = new BusEditViewModel(
                    createBaseViewModel("Редактирование автобуса")
            );


            model.addAttribute("model", viewModel);
            model.addAttribute("form", form);
            return "bus-edit";
        }

        busService.updateBus(id, form.mark(), form.numberBus(), form.numberSeats(), form.classBus(), form.year());
        LOG.log(Level.INFO, "Edit bus from "+ principal.getName());
        return "redirect:/bus/" + id;
    }

    @Override
    @GetMapping("/{id}/delete")
    public String delete(Principal principal,String id) {
        busService.deleteBus(id);
        LOG.log(Level.INFO, "Delete bus from "+ principal.getName());
        return "redirect:/bus";
    }

    @Override
    public BaseViewModel createBaseViewModel(String title) {
        return new BaseViewModel(title);
    }
}