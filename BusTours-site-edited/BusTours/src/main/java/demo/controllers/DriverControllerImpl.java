package demo.controllers;

import demo.models.Driver;
import edu.rutmiit.demo.controllers.driver.DriverController;
import edu.rutmiit.demo.dto.base.BaseViewModel;
import edu.rutmiit.demo.dto.driver.*;
import jakarta.validation.Valid;
import demo.dto.DriverDTO;
import demo.services.DriverService;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;

@Controller
@RequestMapping("/driver")
public class DriverControllerImpl implements DriverController {
    private static final Logger LOG = LogManager.getLogger(Controller.class);

    @Autowired
    private DriverService driverService;

    public DriverControllerImpl(DriverService driverService) {
        this.driverService = driverService;
    }
    @Override
    public BaseViewModel createBaseViewModel(String title) {
        return new BaseViewModel(title);
    }

    @GetMapping()
    public String listDrivers(Principal principal, DriverShowForm form, Model model) {

        var page = form.page() != null ? form.page() : 1;
        var size = form.size() != null ? form.size() : 3;

        var driversPage = driverService.getDrivers(page, size);
        var driversViewModels = driversPage.stream()
                .map(d -> new DriverViewModel(d.id(), d.name(), d.surname(), d.patronymic(), d.birthday(),d.passport(), d.driverLicense(), d.dateStartDriverLicense(), d.category(), d.driverStatus())).toList();

        var viewModel = new DriverListViewModel(
                createBaseViewModel("Список водителей"),
                driversViewModels,
                driversPage.getTotalPages()
        );
        LOG.log(Level.INFO, "Show all drivers for "+ principal.getName());
        model.addAttribute("model", viewModel);
        model.addAttribute("form", form);

        return "driver-list";
    }



    @Override
    @GetMapping("/{id}")
    public String details(Principal principal,String id, Model model) {
        var driver = driverService.getDriverById(id);

        var viewModel = new DriverDetailsViewModel(
                createBaseViewModel("Детали водителя"),
                new DriverViewModel(driver.id(), driver.name(),driver.surname(), driver.patronymic(),driver.birthday(), driver.passport(), driver.driverLicense(), driver.dateStartDriverLicense(), driver.category(), driver.driverStatus())
        );

        LOG.log(Level.INFO, "Show driver "+id+" for "+ principal.getName());
        model.addAttribute("model", viewModel);
        return "driver-details";
    }


    @Override
    @GetMapping("/create")
    public String createForm(Principal principal,Model model) {
        var viewModel = new DriverCreateViewModel(
                createBaseViewModel("Создание книги")
        );

        LOG.log(Level.INFO, "Show create form driver for "+ principal.getName());
        model.addAttribute("model", viewModel);
        model.addAttribute("form",new DriverCreateForm("", "", "", null, "", "",null, "" ));
        return "driver-create";
    }

    @Override
    @PostMapping("/create")
    public String create(Principal principal,DriverCreateForm form,
                         BindingResult bindingResult,
                         Model model) {
        if (bindingResult.hasErrors()) {
            var viewModel = new DriverCreateViewModel(
                    createBaseViewModel("Создание водителя")
            );
            model.addAttribute("model", viewModel);
            model.addAttribute("form", form);
            return "driver-create";
        }

        DriverDTO driver =  driverService.createDriver(form.name(), form.surname(), form.patronymic(), form.birthday(), form.passport(), form.driverLicense(), form.dateStartDriverLicense(), form.category());
        LOG.log(Level.INFO, "Create driver for "+ principal.getName());
        return "redirect:/driver/"+driver.id();
    }



    @Override
    @GetMapping("/{id}/edit")
    public String editForm(Principal principal,String id, Model model) {

        var driver = driverService.getDriverById(id);
        var viewModel = new DriverEditViewModel(
                createBaseViewModel("Редактирование книги")
        );

        LOG.log(Level.INFO, "Show edit form driver for "+ principal.getName());
        model.addAttribute("model", viewModel);
        model.addAttribute("form",new DriverEditForm(driver.id(), driver.name(), driver.surname(), driver.patronymic(), driver.birthday().toString(), driver.passport(), driver.driverLicense(), driver.dateStartDriverLicense().toString(), driver.category()));
        return "driver-edit";
    }

    @Override
    @PostMapping("/{id}/edit")
    public String edit(Principal principal,@PathVariable String id,
                       @Valid @ModelAttribute("form") DriverEditForm form,
                       BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            var viewModel = new DriverEditViewModel(
                    createBaseViewModel("Редактирование водителя")
            );
            model.addAttribute("model", viewModel);
            model.addAttribute("form", form);
            return "driver-edit";
        }

        driverService.updateDriver(id, form.name(), form.surname(), form.patronymic(), LocalDate.parse(form.birthday()), form.passport(), form.driverLicense(), LocalDate.parse(form.dateStartDriverLicense()), form.category());
        LOG.log(Level.INFO, "Edit driver for "+ principal.getName());
        return "redirect:/driver";
    }

    @Override
    @GetMapping("/{id}/delete")
    public String delete(Principal principal,String id) {
        driverService.deleteDriver(id);
        LOG.log(Level.INFO, "Delete driver for "+ principal.getName());
        return "redirect:/driver";
    }

}
