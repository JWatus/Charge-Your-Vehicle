package charge_your_vehicle.service.providers;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

@Service
public class InformationModelsProvider {

    public ModelAndView getModelAndViewForAboutPage() {
        ModelAndView modelAndView = new ModelAndView("body-templates/about");
        modelAndView.addObject("title", "About");
        return modelAndView;
    }

    public ModelAndView getModelAndViewHomePage() {
        ModelAndView modelAndView = new ModelAndView("body-templates/home");
        modelAndView.addObject("title", "Home");
        return modelAndView;
    }

    public ModelAndView getModelAndViewHowToStartPage() {
        ModelAndView modelAndView = new ModelAndView("body-templates/how-to-start");
        modelAndView.addObject("title", "How to start");
        return modelAndView;
    }
}