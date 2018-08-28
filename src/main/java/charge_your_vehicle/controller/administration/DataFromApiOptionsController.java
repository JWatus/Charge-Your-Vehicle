package charge_your_vehicle.controller.administration;

import charge_your_vehicle.service.providers.AdministrationModelsProvider;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class DataFromApiOptionsController {

    private final AdministrationModelsProvider administrationModelsProvider;

    public DataFromApiOptionsController(AdministrationModelsProvider administrationModelsProvider) {
        this.administrationModelsProvider = administrationModelsProvider;
    }

    @RequestMapping(value = "/administration/load-data-options", method = RequestMethod.GET)
    public ModelAndView loadDataFromApiOptionsPage() {
        return administrationModelsProvider.getModelAndViewLoadDataFromApiOptionsPage();
    }
}
