package charge_your_vehicle.view.controllers;

import charge_your_vehicle.dao.ChargingPointRepository;
import charge_your_vehicle.dao.CountryStatisticsRepository;
import charge_your_vehicle.dto.ChargingPointDto;
import charge_your_vehicle.service.properties.AppPropertiesBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServlet;
import java.util.List;

@Controller
public class SearchByCountryController extends HttpServlet {

    private ChargingPointRepository chargingPointRepository;
    private CountryStatisticsRepository countryStatisticsRepository;
    private AppPropertiesBean appPropertiesBean;

    public SearchByCountryController(ChargingPointRepository chargingPointRepository,
                                     CountryStatisticsRepository countryStatisticsRepository,
                                     AppPropertiesBean appPropertiesBean) {
        this.chargingPointRepository = chargingPointRepository;
        this.countryStatisticsRepository = countryStatisticsRepository;
        this.appPropertiesBean = appPropertiesBean;
    }

    public static final Logger LOG = LoggerFactory.getLogger(SearchByCountryController.class);

    @RequestMapping(value = "/search-by-country", method = RequestMethod.GET)
    public ModelAndView getFindTheClosestPage() {
        LOG.info("User searched charging station at country");
        ModelAndView modelAndView = new ModelAndView("body-templates/search-by-country");
        modelAndView.addObject("title", "Search by country");
        modelAndView.addObject("chargingPointDto", new ChargingPointDto());
        return modelAndView;
    }

    @RequestMapping(value = "/search-by-country", method = RequestMethod.POST)
    public ModelAndView getFindAllInTownResultPage(@ModelAttribute ChargingPointDto chargingPointDto) {

        String country = chargingPointDto.getCountry();
        ModelAndView modelAndView = new ModelAndView("body-templates/search-by-country");

        if (country == null || country.isEmpty()) {
            modelAndView.addObject("error", "Fill the field with correct value");
            return modelAndView;
        } else {
            try {
                List<ChargingPointDto> chargingPointsDtoList = ChargingPointDto.convertFromChargingPointList(chargingPointRepository.findByCountry(country));
                if (chargingPointsDtoList.size() > 0) {
//                    countryStatisticsRepository.addToStatistics(country);
                    modelAndView = new ModelAndView("body-templates/results");
                    modelAndView.addObject("chargingPoints", chargingPointsDtoList);
                    modelAndView.addObject("chargingPointsSize", chargingPointsDtoList.size());
                    modelAndView.addObject("title", "Search by country");
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
