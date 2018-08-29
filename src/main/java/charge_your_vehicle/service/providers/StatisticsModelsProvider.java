package charge_your_vehicle.service.providers;

import charge_your_vehicle.model.dto.StatisticsDto;
import charge_your_vehicle.repository.statistics.CountryStatisticsRepository;
import charge_your_vehicle.repository.statistics.TownStatisticsRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Service
public class StatisticsModelsProvider {
    private CountryStatisticsRepository countryStatisticsRepository;
    private TownStatisticsRepository townStatisticsRepository;

    public StatisticsModelsProvider(CountryStatisticsRepository countryStatisticsRepository,
                                    TownStatisticsRepository townStatisticsRepository) {
        this.countryStatisticsRepository = countryStatisticsRepository;
        this.townStatisticsRepository = townStatisticsRepository;
    }

    public ModelAndView getModelAndViewStatisticsFormPage() {
        ModelAndView modelAndView = new ModelAndView("body-templates/statistics");
        modelAndView.addObject("title", "Statistics");
        modelAndView.addObject("statisticsDto", new StatisticsDto());
        return modelAndView;
    }

    public ModelAndView getModelAndViewStatisticsResultPage(@ModelAttribute StatisticsDto statisticsDto) {
        String location = statisticsDto.getLocation();
        ModelAndView modelAndView = new ModelAndView("body-templates/statistics");
        modelAndView.addObject("title", "Statistics");
        if (location.equals("town")) {
            modelAndView = getModelAndViewStatisticsForTown(modelAndView);
        } else if (location.equals("country")) {
            modelAndView = getModelAndViewStatisticsForCountry(modelAndView);
        }
        return modelAndView;
    }

    private ModelAndView getModelAndViewStatisticsForCountry(ModelAndView modelAndView) {
        List locationList = countryStatisticsRepository.findAllOrderByNumberOfVisitsDesc();
        modelAndView = checkListSizeForLocation(modelAndView, locationList, "Noone was searching charging points by country name");
        return modelAndView;
    }

    private ModelAndView getModelAndViewStatisticsForTown(ModelAndView modelAndView) {
        List locationList = townStatisticsRepository.findAllOrderByNumberOfVisitsDesc();
        modelAndView = checkListSizeForLocation(modelAndView, locationList, "Noone was searching charging points by town name");
        return modelAndView;
    }

    private ModelAndView checkListSizeForLocation(ModelAndView modelAndView, List locationList, String errorMessage) {
        if (locationList.size() == 0) {
            modelAndView.addObject("error", errorMessage);
        } else {
            modelAndView = new ModelAndView("body-templates/show-statistics");
            modelAndView.addObject("locationList", locationList);
        }
        return modelAndView;
    }
}