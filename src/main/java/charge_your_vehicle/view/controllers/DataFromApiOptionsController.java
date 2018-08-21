package charge_your_vehicle.view.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class DataFromApiOptionsController {

    @RequestMapping(value = "/administration/load-data-options", method = RequestMethod.GET)
    public ModelAndView loadDataFromApiOptions() {
        ModelAndView modelAndView = new ModelAndView("body-templates/load-data-options");
        modelAndView.addObject("title", "Administration");
        return modelAndView;
    }
}
