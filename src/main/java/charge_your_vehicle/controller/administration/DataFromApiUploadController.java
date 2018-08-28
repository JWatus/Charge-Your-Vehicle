package charge_your_vehicle.controller.administration;

import charge_your_vehicle.service.providers.AdministrationModelsProvider;
import charge_your_vehicle.service.upload.ApiUploadProcessorBean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class DataFromApiUploadController {

    private final ApiUploadProcessorBean apiUploadProcessorBean;
    private final AdministrationModelsProvider administrationModelsProvider;

    public DataFromApiUploadController(ApiUploadProcessorBean apiUploadProcessorBean, AdministrationModelsProvider administrationModelsProvider) {
        this.apiUploadProcessorBean = apiUploadProcessorBean;
        this.administrationModelsProvider = administrationModelsProvider;
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
}
