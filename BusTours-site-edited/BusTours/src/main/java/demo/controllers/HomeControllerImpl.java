package demo.controllers;

import edu.rutmiit.demo.controllers.HomeController;
import edu.rutmiit.demo.dto.base.BaseViewModel;
import edu.rutmiit.demo.dto.home.HomeViewModel;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/")
public class HomeControllerImpl extends BaseControllerIpl implements HomeController {
    private static final Logger LOG = LogManager.getLogger(Controller.class);

    @Override
    @GetMapping("/")
    public String index(Principal principal, Model model) {
        var viewModel = new HomeViewModel(createBaseViewModel("Главная страница"));
        LOG.log(Level.INFO, "Show home for " +(principal != null ? principal.getName() : "anonymousUser"));
        model.addAttribute("model", viewModel);

        return "index";
    }

}
