package charge_your_vehicle.view.controllers;

import charge_your_vehicle.dao.ChargingPointDao;
import charge_your_vehicle.dao.TownStatisticsDao;
import charge_your_vehicle.dto.ChargingPointDto;
import charge_your_vehicle.service.promoted.ChargingPointToDtoConverterBean;
import charge_your_vehicle.service.properties.AppPropertiesBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class SearchByTownController {

    private ChargingPointDao chargingPointDao;
    private ChargingPointToDtoConverterBean chargingPointToDtoConverterBean;
    private TownStatisticsDao townStatisticsDao;
    private AppPropertiesBean appPropertiesBean;

    public SearchByTownController(ChargingPointDao chargingPointDao,
                                  ChargingPointToDtoConverterBean chargingPointToDtoConverterBean,
                                  TownStatisticsDao townStatisticsDao,
                                  AppPropertiesBean appPropertiesBean) {
        this.chargingPointDao = chargingPointDao;
        this.chargingPointToDtoConverterBean = chargingPointToDtoConverterBean;
        this.townStatisticsDao = townStatisticsDao;
        this.appPropertiesBean = appPropertiesBean;
    }

    public static final Logger LOG = LoggerFactory.getLogger(SearchByTownController.class);

    @RequestMapping(value = "/search-by-town", method = RequestMethod.GET)
    public ModelAndView getFindTheClosestPage(HttpSession session) {

        LOG.info("User searched charging station at town");

        ModelAndView modelAndView = new ModelAndView("body-templates/search-by-town");
        modelAndView.addObject("title", "Search by town");

        String town = (String) session.getAttribute("town");
        if (town == null || town.isEmpty()) {
            return modelAndView;
        } else {
            try {
                List<ChargingPointDto> chargingPointsDtoList = chargingPointToDtoConverterBean.convertList(chargingPointDao.findByTown(town));
                if (chargingPointsDtoList.size() > 0) {
                    townStatisticsDao.addToStatistics(town);
                    modelAndView = new ModelAndView("body-templates/results");
                    modelAndView.addObject("chargingPoints", chargingPointsDtoList);
                    modelAndView.addObject("title", "Search by town");
                    modelAndView.addObject("google_api_key", appPropertiesBean.getGoogleApiKey());
                } else {
                    errorMessages(modelAndView);
                }
            } catch (Exception e) {
                errorMessages(modelAndView);
                LOG.error("Exception was catched.");
            }
        }
        return modelAndView;
    }

    private void errorMessages(ModelAndView modelAndView) {
        modelAndView.addObject("error", "No charging points found");
    }
}
