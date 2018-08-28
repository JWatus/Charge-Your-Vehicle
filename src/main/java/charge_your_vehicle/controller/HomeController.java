package charge_your_vehicle.controller;

import charge_your_vehicle.service.providers.InformationModelsProvider;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

    private final InformationModelsProvider informationModelsProvider;

    public HomeController(InformationModelsProvider informationModelsProvider) {
        this.informationModelsProvider = informationModelsProvider;
    }

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public ModelAndView getHomePage() {
        return informationModelsProvider.getModelAndViewHomePage();
    }
}
