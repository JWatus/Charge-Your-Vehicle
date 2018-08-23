package charge_your_vehicle.controller;

import charge_your_vehicle.model.dto.ParametersDto;
import charge_your_vehicle.service.properties.AppPropertiesBean;
import charge_your_vehicle.service.properties.Units;
import charge_your_vehicle.service.formaters.Formaters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PropertiesController {

    public static final Logger LOG = LoggerFactory.getLogger(PropertiesController.class);

    private AppPropertiesBean appPropertiesBean;

    public PropertiesController(AppPropertiesBean appPropertiesBean) {
        this.appPropertiesBean = appPropertiesBean;
    }

    @RequestMapping(value = "/administration/properties", method = RequestMethod.GET)
    public ModelAndView getPropertiesFormPage() {
        ModelAndView modelAndView = new ModelAndView("body-templates/properties");
        modelAndView.addObject("title", "Properties");
        modelAndView.addObject("units", Formaters.getNames(Units.values()));
        modelAndView.addObject("current_unit", Formaters.naturalFormat(appPropertiesBean.getCurrentUnit().name()));
        modelAndView.addObject("apiKey", appPropertiesBean.getGoogleApiKey());
        modelAndView.addObject("parametersDto", new ParametersDto());
        return modelAndView;
    }

    @RequestMapping(value = "/administration/properties", method = RequestMethod.POST)
    public ModelAndView getPropertiesResultPage(@ModelAttribute ParametersDto parametersDto) {

        ModelAndView modelAndView = new ModelAndView("body-templates/properties");
        modelAndView.addObject("title", "Properties");

        String unit = parametersDto.getUnit();
        if (unit != null && !unit.isEmpty()) {
            appPropertiesBean.setUnits(Units.valueOf(unit.toUpperCase()));
        }

        String apiKey = parametersDto.getApiKey();
        if (apiKey != null && !apiKey.isEmpty()) {
            appPropertiesBean.setGoogleApiKey(apiKey);
        }

        modelAndView.addObject("apiKey", appPropertiesBean.getGoogleApiKey());
        modelAndView.addObject("units", Formaters.getNames(Units.values()));
        modelAndView.addObject("current_unit", Formaters.naturalFormat(appPropertiesBean.getCurrentUnit().name()));
        return modelAndView;
    }
}
