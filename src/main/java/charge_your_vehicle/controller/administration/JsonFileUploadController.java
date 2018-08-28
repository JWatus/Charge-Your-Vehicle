package charge_your_vehicle.controller.administration;

import charge_your_vehicle.service.providers.AdministrationModelsProvider;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

@Controller
public class JsonFileUploadController {

    private final AdministrationModelsProvider administrationModelsProvider;

    public JsonFileUploadController(AdministrationModelsProvider administrationModelsProvider) {
        this.administrationModelsProvider = administrationModelsProvider;
    }

    @RequestMapping(value = "/administration/json-upload", method = RequestMethod.GET)
    public ModelAndView uploadJSONFile() throws IOException {
        return administrationModelsProvider.getModelAndViewUploadJsonFile();
    }
}
