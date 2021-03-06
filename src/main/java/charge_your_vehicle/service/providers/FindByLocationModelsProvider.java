package charge_your_vehicle.service.providers;

import charge_your_vehicle.model.dto.ChargingPointDto;
import charge_your_vehicle.repository.ChargingPointRepository;
import charge_your_vehicle.repository.statistics.CountryStatisticsRepository;
import charge_your_vehicle.repository.statistics.StatisticsRepository;
import charge_your_vehicle.repository.statistics.TownStatisticsRepository;
import charge_your_vehicle.service.properties.AppPropertiesBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Service
public class FindByLocationModelsProvider {

    private static final Logger LOG = LoggerFactory.getLogger(FindByLocationModelsProvider.class);

    private ChargingPointRepository chargingPointRepository;
    private TownStatisticsRepository townStatisticsRepository;
    private CountryStatisticsRepository countryStatisticsRepository;
    private AppPropertiesBean appPropertiesBean;

    public FindByLocationModelsProvider(ChargingPointRepository chargingPointRepository,
                                        TownStatisticsRepository townStatisticsRepository,
                                        AppPropertiesBean appPropertiesBean, CountryStatisticsRepository countryStatisticsRepository) {
        this.chargingPointRepository = chargingPointRepository;
        this.townStatisticsRepository = townStatisticsRepository;
        this.appPropertiesBean = appPropertiesBean;
        this.countryStatisticsRepository = countryStatisticsRepository;
    }

    public ModelAndView getModelAndViewSearchByLocationFormPage(String location) {
        LOG.info("User searched charging station at " + location);
        ModelAndView modelAndView = new ModelAndView("body-templates/search-by-" + location);
        modelAndView.addObject("title", "Search by " + location);
        modelAndView.addObject("chargingPointDto", new ChargingPointDto());
        return modelAndView;
    }

    public ModelAndView getModelAndViewSearchByTownResultPage(@ModelAttribute ChargingPointDto chargingPointDto) {
        String town = chargingPointDto.getTown();
        ModelAndView modelAndView = new ModelAndView("body-templates/search-by-town");
        if (town == null || town.isEmpty()) {
            return getErrorMessage(modelAndView, "Fill the field with correct value", "Search by town");
        } else {
            try {
                List<ChargingPointDto> chargingPointsDtoList = ChargingPointDto.convertFromChargingPointList(chargingPointRepository.findByTown(town));
                return checkChargingPointsInGivenLocation(town, "town", modelAndView, townStatisticsRepository, chargingPointsDtoList);
            } catch (Exception e) {
                LOG.error("Exception was catched.");
                return getErrorMessage(modelAndView, "No charging points found", "Search by town");
            }
        }
    }

    public ModelAndView getModelAndViewSearchByCountryResultPage(@ModelAttribute ChargingPointDto chargingPointDto) {
        String country = chargingPointDto.getCountry();
        ModelAndView modelAndView = new ModelAndView("body-templates/search-by-country");
        if (country == null || country.isEmpty()) {
            return getErrorMessage(modelAndView, "Fill the field with correct value", "Search by country");
        } else {
            try {
                List<ChargingPointDto> chargingPointsDtoList = ChargingPointDto.convertFromChargingPointList(chargingPointRepository.findByCountry(country));
                return checkChargingPointsInGivenLocation(country, "country", modelAndView, countryStatisticsRepository, chargingPointsDtoList);
            } catch (Exception e) {
                LOG.error("Exception was catched.");
                return getErrorMessage(modelAndView, "No charging points found", "Search by country");
            }
        }
    }

    private ModelAndView checkChargingPointsInGivenLocation(String location,
                                                            String townOrCountry,
                                                            ModelAndView modelAndView,
                                                            StatisticsRepository statisticsRepository,
                                                            List<ChargingPointDto> chargingPointsDtoList) {
        if (chargingPointsDtoList.size() > 0) {
            statisticsRepository.addToStatistics(location);
            modelAndView = new ModelAndView("body-templates/results-town-and-country");
            modelAndView.addObject("chargingPoints", chargingPointsDtoList);
            modelAndView.addObject("chargingPointsSize", chargingPointsDtoList.size());
            modelAndView.addObject("title", "Search by " + townOrCountry);
            modelAndView.addObject("google_api_key", appPropertiesBean.getGoogleApiKey());
            return modelAndView;
        } else {
            return getErrorMessage(modelAndView, "No charging points found", "Search by " + townOrCountry);
        }
    }

    private ModelAndView getErrorMessage(ModelAndView modelAndView, String errorMessage, String title) {
        modelAndView.addObject("error", errorMessage);
        modelAndView.addObject("title", title);
        return modelAndView;
    }
}