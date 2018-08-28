package charge_your_vehicle.controller.administration;

import charge_your_vehicle.model.dto.ParametersDto;
import charge_your_vehicle.service.providers.AdministrationModelsProvider;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PropertiesController {

    private final AdministrationModelsProvider administrationModelsProvider;

    public PropertiesController(AdministrationModelsProvider administrationModelsProvider) {
        this.administrationModelsProvider = administrationModelsProvider;
    }

    @RequestMapping(value = "/administration/properties", method = RequestMethod.GET)
    public ModelAndView getPropertiesFormPage() {
        return administrationModelsProvider.getModelAndViewPropertiesFormPage();
    }

    @RequestMapping(value = "/administration/properties", method = RequestMethod.POST)
    public ModelAndView getPropertiesResultPage(@ModelAttribute ParametersDto parametersDto) {
        return administrationModelsProvider.getModelAndViewPropertiesResultPage(parametersDto);
    }
}
