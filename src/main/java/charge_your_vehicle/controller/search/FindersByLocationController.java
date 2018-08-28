package charge_your_vehicle.controller.search;

import charge_your_vehicle.model.dto.ChargingPointDto;
import charge_your_vehicle.service.providers.FindersByLocationModelsProvider;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class FindersByLocationController {

    private final FindersByLocationModelsProvider findersByLocationModelsProvider;

    public FindersByLocationController(FindersByLocationModelsProvider findersByLocationModelsProvider) {
        this.findersByLocationModelsProvider = findersByLocationModelsProvider;
    }

    @RequestMapping(value = "/search-by-town", method = RequestMethod.GET)
    public ModelAndView getSearchByTownFormPage() {
        return findersByLocationModelsProvider.getModelAndViewSearchByLocationFormPage("town");
    }

    @RequestMapping(value = "/search-by-town", method = RequestMethod.POST)
    public ModelAndView getSearchByTownResultPage(@ModelAttribute ChargingPointDto chargingPointDto) {
        return findersByLocationModelsProvider.getModelAndViewSearchByTownResultPage(chargingPointDto);
    }

    @RequestMapping(value = "/search-by-country", method = RequestMethod.GET)
    public ModelAndView getSearchByCountryFormPage() {
        return findersByLocationModelsProvider.getModelAndViewSearchByLocationFormPage("country");
    }

    @RequestMapping(value = "/search-by-country", method = RequestMethod.POST)
    public ModelAndView getSearchByCountryResultPage(@ModelAttribute ChargingPointDto chargingPointDto) {
        return findersByLocationModelsProvider.getModelAndViewSearchByCountryResultPage(chargingPointDto);
    }

}
