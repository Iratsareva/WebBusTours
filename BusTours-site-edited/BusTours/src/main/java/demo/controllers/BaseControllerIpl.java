package demo.controllers;

import edu.rutmiit.demo.controllers.BaseController;
import edu.rutmiit.demo.dto.base.BaseViewModel;
import org.springframework.stereotype.Controller;

@Controller
public abstract class BaseControllerIpl implements BaseController {

    public BaseViewModel createBaseViewModel(String title) {
        return new BaseViewModel(title);
    }

}
