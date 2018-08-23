package charge_your_vehicle.view.controllers;

import charge_your_vehicle.service.upload.ApiUploadProcessorBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class DataFromApiUploadController {

    public static final Logger LOG = LoggerFactory.getLogger(DataFromApiUploadController.class);

    private ApiUploadProcessorBean apiUploadProcessorBean;

    public DataFromApiUploadController(ApiUploadProcessorBean apiUploadProcessorBean) {
        this.apiUploadProcessorBean = apiUploadProcessorBean;
    }

    @RequestMapping(value = "/administration/load-data-from-api-world", method = RequestMethod.GET)
    public ModelAndView loadDataFromWorld() {
        ModelAndView modelAndView = new ModelAndView("body-templates/load-data-from-api");
        modelAndView.addObject("title", "Administration");
        int recordsAdded;
        try {
            recordsAdded = apiUploadProcessorBean.uploadAllChargingPointsFromApi();
            if (recordsAdded != 0) {
                modelAndView.addObject("recordsAdded", recordsAdded);
            }
        } catch (Exception e) {
            LOG.error("Failed to update charging points from api: {}", e);
        }
        return modelAndView;
    }

    @RequestMapping(value = "/administration/load-data-from-api-poland", method = RequestMethod.GET)
    public ModelAndView loadDataFromPoland() {
        ModelAndView modelAndView = new ModelAndView("body-templates/load-data-from-api");
        modelAndView.addObject("title", "Administration");
        int recordsAdded;
        try {
            recordsAdded = apiUploadProcessorBean.uploadAllChargingPointsInPolandFromApi();
            if (recordsAdded != 0) {
                modelAndView.addObject("recordsAdded", recordsAdded);
            }
        } catch (Exception e) {
            LOG.error("Failed to update charging points from api: {}", e);
        }
        return modelAndView;
    }

    @RequestMapping(value = "/administration/load-data-from-api-india", method = RequestMethod.GET)
    public ModelAndView loadDataFromIndia() {
        ModelAndView modelAndView = new ModelAndView("body-templates/load-data-from-api");
        modelAndView.addObject("title", "Administration");
        int recordsAdded;
        try {
            recordsAdded = apiUploadProcessorBean.uploadAllChargingPointsInIndiaFromApi();
            if (recordsAdded != 0) {
                modelAndView.addObject("recordsAdded", recordsAdded);
            }
        } catch (Exception e) {
            LOG.error("Failed to update charging points from api: {}", e);
        }
        return modelAndView;
    }
}
