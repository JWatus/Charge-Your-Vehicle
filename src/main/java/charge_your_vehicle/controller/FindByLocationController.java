package charge_your_vehicle.controller;

import charge_your_vehicle.model.dto.ChargingPointDto;
import charge_your_vehicle.service.providers.FindByLocationModelsProvider;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class FindByLocationController {

    private final FindByLocationModelsProvider findByLocationModelsProvider;

    public FindByLocationController(FindByLocationModelsProvider findByLocationModelsProvider) {
        this.findByLocationModelsProvider = findByLocationModelsProvider;
    }

    @RequestMapping(value = "/search-by-town", method = RequestMethod.GET)
    public ModelAndView getSearchByTownFormPage() {
        return findByLocationModelsProvider.getModelAndViewSearchByLocationFormPage("town");
    }

    @RequestMapping(value = "/search-by-town", method = RequestMethod.POST)
    public ModelAndView getSearchByTownResultPage(@ModelAttribute ChargingPointDto chargingPointDto) {
        return findByLocationModelsProvider.getModelAndViewSearchByTownResultPage(chargingPointDto);
    }

    @RequestMapping(value = "/search-by-country", method = RequestMethod.GET)
    public ModelAndView getSearchByCountryFormPage() {
        return findByLocationModelsProvider.getModelAndViewSearchByLocationFormPage("country");
    }

    @RequestMapping(value = "/search-by-country", method = RequestMethod.POST)
    public ModelAndView getSearchByCountryResultPage(@ModelAttribute ChargingPointDto chargingPointDto) {
        return findByLocationModelsProvider.getModelAndViewSearchByCountryResultPage(chargingPointDto);
    }
}
