package charge_your_vehicle.view.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HowToStartController {

    public static final Logger LOG = LoggerFactory.getLogger(HowToStartController.class);

    @RequestMapping(value = "/how-to-start", method = RequestMethod.GET)
    public ModelAndView getHowToStartPage() {
        ModelAndView modelAndView = new ModelAndView("body-templates/how-to-start");
        modelAndView.addObject("title", "How to start");
        return modelAndView;
    }
}
