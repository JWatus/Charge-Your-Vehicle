package charge_your_vehicle.controller;

import charge_your_vehicle.model.dto.CoordinatesDto;
import charge_your_vehicle.service.providers.FindByCoordinatesModelProvider;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class FindByCoordinatesController {

    private final FindByCoordinatesModelProvider findByCoordinatesModelProvider;

    public FindByCoordinatesController(FindByCoordinatesModelProvider findByCoordinatesModelProvider) {
        this.findByCoordinatesModelProvider = findByCoordinatesModelProvider;
    }

    @RequestMapping(value = "/find-the-closest", method = RequestMethod.GET)
    public ModelAndView getFindTheClosestFormPage() {
        return findByCoordinatesModelProvider.getModelAndViewFindTheClosestFormPage();
    }

    @RequestMapping(value = "/find-the-closest", method = RequestMethod.POST)
    public ModelAndView getFindTheClosestResultPage(@ModelAttribute CoordinatesDto coordinatesDto) {
        return findByCoordinatesModelProvider.getModelAndViewFindTheClosestResultPage(coordinatesDto);
    }

    @RequestMapping(value = "/find-the-closest-in-radius", method = RequestMethod.GET)
    public ModelAndView getFindTheClosestInRadiusFormPage() {
        return findByCoordinatesModelProvider.getModelAndViewFindTheClosestInRadiusFormPage();
    }

    @RequestMapping(value = "/find-the-closest-in-radius", method = RequestMethod.POST)
    public ModelAndView getFindTheClosestInRadiusResultPage(@ModelAttribute CoordinatesDto coordinatesDto) {
        return findByCoordinatesModelProvider.getModelAndViewFindTheClosestInRadiusResultPage(coordinatesDto);
    }
}
