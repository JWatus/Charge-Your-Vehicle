package charge_your_vehicle.view.controllers;

import charge_your_vehicle.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

@Controller
public class HomeController {

    public static final Logger LOG = LoggerFactory.getLogger(HomeController.class);

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public ModelAndView getHomePage(HttpSession session) {

        ModelAndView modelAndView = new ModelAndView("body-templates/home");
        modelAndView.addObject("title", "Home");

        Object userObject = session.getAttribute("user");
        User user;
        if (userObject != null) {
            user = (User) userObject;
            modelAndView.addObject("userSessionName", user.getName());
            modelAndView.addObject("userAdmin", user.getRoleAdministration());
        }
        return modelAndView;
    }
}
