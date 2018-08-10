package charge_your_vehicle.view.controllers;

import charge_your_vehicle.dao.ChargingPointRepository;
import charge_your_vehicle.dao.CountryStatisticsDao;
import charge_your_vehicle.dto.ChargingPointDto;
import charge_your_vehicle.service.promoted.ChargingPointToDtoConverterBean;
import charge_your_vehicle.service.properties.AppPropertiesBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class SearchByCountryController extends HttpServlet {

    private ChargingPointRepository chargingPointRepository;
    private ChargingPointToDtoConverterBean chargingPointToDtoConverterBean;
    private CountryStatisticsDao countryStatisticsDao;
    private AppPropertiesBean appPropertiesBean;

    public SearchByCountryController(ChargingPointRepository chargingPointRepository,
                                     ChargingPointToDtoConverterBean chargingPointToDtoConverterBean,
                                     CountryStatisticsDao countryStatisticsDao,
                                     AppPropertiesBean appPropertiesBean) {
        this.chargingPointRepository = chargingPointRepository;
        this.chargingPointToDtoConverterBean = chargingPointToDtoConverterBean;
        this.countryStatisticsDao = countryStatisticsDao;
        this.appPropertiesBean = appPropertiesBean;
    }

    public static final Logger LOG = LoggerFactory.getLogger(SearchByCountryController.class);

    @RequestMapping(value = "/search-by-country", method = RequestMethod.GET)
    public ModelAndView getFindTheClosestPage(HttpSession session) {

        LOG.info("User searched charging station at country");

        ModelAndView modelAndView = new ModelAndView("body-templates/search-by-country");
        modelAndView.addObject("title", "Search by country");

        String country = (String) session.getAttribute("country");
        if (country == null || country.isEmpty()) {
            modelAndView.addObject("body_template", "search-by-country");
        } else {
            try {
                List<ChargingPointDto> chargingPointsDtoList = chargingPointToDtoConverterBean.convertList(chargingPointRepository.findByAddressInfo_Country(country));
                if (chargingPointsDtoList.size() > 0) {
                    countryStatisticsDao.addToStatistics(country);
                    modelAndView = new ModelAndView("body-templates/results");
                    modelAndView.addObject("chargingPoints", chargingPointsDtoList);
                    modelAndView.addObject("title", "Search by country");
                    modelAndView.addObject("google_api_key", appPropertiesBean.getGoogleApiKey());
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
        return modelAndView;
    }

    private void errorMessages(ModelAndView modelAndView) {
        modelAndView.addObject("error", "No charging points found");
    }
}
