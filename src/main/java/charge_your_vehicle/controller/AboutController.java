package charge_your_vehicle.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AboutController {

    public static final Logger LOG = LoggerFactory.getLogger(AboutController.class);

    @RequestMapping(value = "/about", method = RequestMethod.GET)
    public ModelAndView getAboutPage() {
        return getModelAndViewForAboutPage();
    }

    private ModelAndView getModelAndViewForAboutPage() {
        ModelAndView modelAndView = new ModelAndView("body-templates/about");
        modelAndView.addObject("title", "About");
        return modelAndView;
    }
}
