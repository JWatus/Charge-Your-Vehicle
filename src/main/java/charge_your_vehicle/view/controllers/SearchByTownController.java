package charge_your_vehicle.view.controllers;

import charge_your_vehicle.dao.ChargingPointRepository;
import charge_your_vehicle.dao.TownStatisticsRepository;
import charge_your_vehicle.dto.ChargingPointDto;
import charge_your_vehicle.service.promoted.ChargingPointToDtoConverterBean;
import charge_your_vehicle.service.properties.AppPropertiesBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class SearchByTownController {

    private ChargingPointRepository chargingPointRepository;
    private ChargingPointToDtoConverterBean chargingPointToDtoConverterBean;
    private TownStatisticsRepository townStatisticsRepository;
    private AppPropertiesBean appPropertiesBean;

    public SearchByTownController(ChargingPointRepository chargingPointRepository,
                                  ChargingPointToDtoConverterBean chargingPointToDtoConverterBean,
                                  TownStatisticsRepository townStatisticsRepository,
                                  AppPropertiesBean appPropertiesBean) {
        this.chargingPointRepository = chargingPointRepository;
        this.chargingPointToDtoConverterBean = chargingPointToDtoConverterBean;
        this.townStatisticsRepository = townStatisticsRepository;
        this.appPropertiesBean = appPropertiesBean;
    }

    public static final Logger LOG = LoggerFactory.getLogger(SearchByTownController.class);

    @RequestMapping(value = "/search-by-town", method = RequestMethod.GET)
    public ModelAndView getFindAllInTownFormPage() {
        LOG.info("User searched charging station at town");
        ModelAndView modelAndView = new ModelAndView("body-templates/search-by-town");
        modelAndView.addObject("title", "Search by town");
        modelAndView.addObject("chargingPointDto", new ChargingPointDto());
        return modelAndView;
    }

    @RequestMapping(value = "/search-by-town", method = RequestMethod.POST)
    public ModelAndView getFindAllInTownResultPage(@ModelAttribute ChargingPointDto chargingPointDto) {

        String town = chargingPointDto.getTown();
        ModelAndView modelAndView = new ModelAndView("body-templates/search-by-town");

        if (town == null || town.isEmpty()) {
            modelAndView.addObject("error", "Fill the field with correct value");
            return modelAndView;
        } else {
            try {
                List<ChargingPointDto> chargingPointsDtoList = ChargingPointDto.convertFromChargingPointList(chargingPointRepository.findByTown(town));
                if (chargingPointsDtoList.size() > 0) {
//                    townStatisticsRepository.addToStatistics(town);
                    modelAndView = new ModelAndView("body-templates/results-town-and-country");
                    modelAndView.addObject("chargingPoints", chargingPointsDtoList);
                    modelAndView.addObject("chargingPointsSize", chargingPointsDtoList.size());
                    modelAndView.addObject("title", "Search by town");
                    modelAndView.addObject("google_api_key", appPropertiesBean.getGoogleApiKey());
                    return modelAndView;
                } else {
                    errorMessages(modelAndView);
                    return modelAndView;
                }
            } catch (Exception e) {
                errorMessages(modelAndView);
                LOG.error("Exception was catched.");
                return modelAndView;
            }
        }
    }

    private void errorMessages(ModelAndView modelAndView) {
        modelAndView.addObject("error", "No charging points found");
    }
}
