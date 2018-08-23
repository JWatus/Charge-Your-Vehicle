package charge_your_vehicle.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

    public static final Logger LOG = LoggerFactory.getLogger(HomeController.class);

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public ModelAndView getHomePage() {
        ModelAndView modelAndView = new ModelAndView("body-templates/home");
        modelAndView.addObject("title", "Home");
        return modelAndView;
    }
}
