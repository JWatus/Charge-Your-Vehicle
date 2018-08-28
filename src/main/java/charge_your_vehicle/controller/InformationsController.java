package charge_your_vehicle.controller;

import charge_your_vehicle.service.providers.InformationModelsProvider;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class InformationsController {

    private final InformationModelsProvider informationModelsProvider;

    public InformationsController(InformationModelsProvider informationModelsProvider) {
        this.informationModelsProvider = informationModelsProvider;
    }

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public ModelAndView getHomePage() {
        return informationModelsProvider.getModelAndViewHomePage();
    }

    @RequestMapping(value = "/how-to-start", method = RequestMethod.GET)
    public ModelAndView getHowToStartPage() {
        return informationModelsProvider.getModelAndViewHowToStartPage();
    }

    @RequestMapping(value = "/about", method = RequestMethod.GET)
    public ModelAndView getAboutPage() {
        return informationModelsProvider.getModelAndViewForAboutPage();
    }
}
