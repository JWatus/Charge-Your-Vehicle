package charge_your_vehicle.controller;

import charge_your_vehicle.model.dto.ParametersDto;
import charge_your_vehicle.service.providers.AdministrationModelsProvider;
import charge_your_vehicle.service.upload.ApiUploadProcessorBean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

@Controller
public class AdministrationController {

    private final ApiUploadProcessorBean apiUploadProcessorBean;
    private final AdministrationModelsProvider administrationModelsProvider;

    public AdministrationController(ApiUploadProcessorBean apiUploadProcessorBean, AdministrationModelsProvider administrationModelsProvider) {
        this.apiUploadProcessorBean = apiUploadProcessorBean;
        this.administrationModelsProvider = administrationModelsProvider;
    }

    /*    LOAD FROM FILE  */

    @RequestMapping(value = "/administration/json-upload", method = RequestMethod.GET)
    public ModelAndView uploadJSONFile() throws IOException {
        return administrationModelsProvider.getModelAndViewUploadJsonFile();
    }

    /*    LOAD FROM API  */

    @RequestMapping(value = "/administration/load-data-options", method = RequestMethod.GET)
    public ModelAndView loadDataFromApiOptionsPage() {
        return administrationModelsProvider.getModelAndViewLoadDataFromApiOptionsPage();
    }

    @RequestMapping(value = "/administration/load-data-from-api-world", method = RequestMethod.GET)
    public ModelAndView loadDataFromWorld() {
        return administrationModelsProvider.getModelAndViewLoadDataFromApi(apiUploadProcessorBean.uploadAllChargingPointsFromApi());
    }

    @RequestMapping(value = "/administration/load-data-from-api-poland", method = RequestMethod.GET)
    public ModelAndView loadDataFromPoland() {
        return administrationModelsProvider.getModelAndViewLoadDataFromApi(apiUploadProcessorBean.uploadAllChargingPointsInPolandFromApi());
    }

    @RequestMapping(value = "/administration/load-data-from-api-india", method = RequestMethod.GET)
    public ModelAndView loadDataFromIndia() {
        return administrationModelsProvider.getModelAndViewLoadDataFromApi(apiUploadProcessorBean.uploadAllChargingPointsInIndiaFromApi());
    }

    /*    ALL LOADED  */

    @RequestMapping(value = "/administration/all-loaded-points", method = RequestMethod.GET)
    public ModelAndView getAllLoadedPointsPage() {
        return administrationModelsProvider.getModelAndViewAllLoadedPointsPage();
    }

    /*    PROPERTIES  */

    @RequestMapping(value = "/administration/properties", method = RequestMethod.GET)
    public ModelAndView getPropertiesFormPage() {
        return administrationModelsProvider.getModelAndViewPropertiesFormPage();
    }

    @RequestMapping(value = "/administration/properties", method = RequestMethod.POST)
    public ModelAndView getPropertiesResultPage(@ModelAttribute ParametersDto parametersDto) {
        return administrationModelsProvider.getModelAndViewPropertiesResultPage(parametersDto);
    }
}
