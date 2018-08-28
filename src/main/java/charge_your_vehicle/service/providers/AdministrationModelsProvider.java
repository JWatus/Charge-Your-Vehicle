package charge_your_vehicle.service.providers;

import charge_your_vehicle.model.dto.ChargingPointDto;
import charge_your_vehicle.model.dto.ParametersDto;
import charge_your_vehicle.repository.ChargingPointRepository;
import charge_your_vehicle.service.formaters.Formaters;
import charge_your_vehicle.service.properties.AppPropertiesBean;
import charge_your_vehicle.service.properties.Units;
import charge_your_vehicle.service.upload.FileUploadProcessorBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.List;

@Service
public class AdministrationModelsProvider {

    public static final Logger LOG = LoggerFactory.getLogger(AdministrationModelsProvider.class);

    private AppPropertiesBean appPropertiesBean;
    private ChargingPointRepository chargingPointRepository;
    private FileUploadProcessorBean fileUploadProcessor;

    public AdministrationModelsProvider(AppPropertiesBean appPropertiesBean,
                                        ChargingPointRepository chargingPointRepository,
                                        FileUploadProcessorBean fileUploadProcessor) {
        this.appPropertiesBean = appPropertiesBean;
        this.chargingPointRepository = chargingPointRepository;
        this.fileUploadProcessor = fileUploadProcessor;
    }

    /*    LOAD FROM FILE  */

    public ModelAndView getModelAndViewUploadJsonFile() throws IOException {
        ModelAndView modelAndView = new ModelAndView("body-templates/json-upload");
        modelAndView.addObject("title", "Administration");
        int recordsAdded = fileUploadProcessor.uploadJsonFile();
        if (recordsAdded != 0) {
            modelAndView.addObject("recordsAdded", recordsAdded);
        }
        return modelAndView;
    }

    /*    LOAD FROM API  */

    public ModelAndView getModelAndViewLoadDataFromApiOptionsPage() {
        ModelAndView modelAndView = new ModelAndView("body-templates/load-data-options");
        modelAndView.addObject("title", "Administration");
        return modelAndView;
    }

    public ModelAndView getModelAndViewLoadDataFromApi(int uploadedPoints) {
        ModelAndView modelAndView = new ModelAndView("body-templates/load-data-from-api");
        modelAndView.addObject("title", "Administration");
        addRecords(uploadedPoints, modelAndView);
        return modelAndView;
    }

    private void addRecords(int uploadedPoints, ModelAndView modelAndView) {
        int recordsAdded;
        try {
            recordsAdded = uploadedPoints;
            if (recordsAdded != 0) {
                modelAndView.addObject("recordsAdded", recordsAdded);
            }
        } catch (Exception e) {
            LOG.error("Failed to update charging points from api: {}", e);
        }
    }

    /*    ALL LOADED  */

    public ModelAndView getModelAndViewAllLoadedPointsPage() {
        ModelAndView modelAndView = new ModelAndView("body-templates/all-loaded-points");
        List<ChargingPointDto> chargingPointsDtoList = ChargingPointDto.convertFromChargingPointList(chargingPointRepository.findAll());
        modelAndView.addObject("title", "All loaded charging points");
        modelAndView.addObject("chargingPoints", chargingPointsDtoList);
        return modelAndView;
    }

    /*    PROPERTIES  */

    public ModelAndView getModelAndViewPropertiesFormPage() {
        ModelAndView modelAndView = new ModelAndView("body-templates/properties");
        modelAndView.addObject("title", "Properties");
        modelAndView.addObject("units", Formaters.getNames(Units.values()));
        modelAndView.addObject("current_unit", Formaters.naturalFormat(appPropertiesBean.getCurrentUnit().name()));
        modelAndView.addObject("apiKey", appPropertiesBean.getGoogleApiKey());
        modelAndView.addObject("parametersDto", new ParametersDto());
        return modelAndView;
    }

    public ModelAndView getModelAndViewPropertiesResultPage(@ModelAttribute ParametersDto parametersDto) {
        ModelAndView modelAndView = new ModelAndView("body-templates/properties");
        modelAndView.addObject("title", "Properties");
        setUnit(parametersDto);
        setGoogleApiKey(parametersDto);
        modelAndView.addObject("apiKey", appPropertiesBean.getGoogleApiKey());
        modelAndView.addObject("units", Formaters.getNames(Units.values()));
        modelAndView.addObject("current_unit", Formaters.naturalFormat(appPropertiesBean.getCurrentUnit().name()));
        return modelAndView;
    }

    private void setGoogleApiKey(@ModelAttribute ParametersDto parametersDto) {
        String apiKey = parametersDto.getApiKey();
        if (apiKey != null && !apiKey.isEmpty()) {
            appPropertiesBean.setGoogleApiKey(apiKey);
        }
    }

    private void setUnit(@ModelAttribute ParametersDto parametersDto) {
        String unit = parametersDto.getUnit();
        if (unit != null && !unit.isEmpty()) {
            appPropertiesBean.setUnits(Units.valueOf(unit.toUpperCase()));
        }
    }
}