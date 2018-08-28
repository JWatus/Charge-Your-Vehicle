package charge_your_vehicle.controller;

import charge_your_vehicle.model.dto.StatisticsDto;
import charge_your_vehicle.service.providers.StatisticsModelProvider;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class StatisticsController {

    private final StatisticsModelProvider statisticsModelProvider;

    public StatisticsController(StatisticsModelProvider statisticsModelProvider) {
        this.statisticsModelProvider = statisticsModelProvider;
    }

    @RequestMapping(value = "/statistics", method = RequestMethod.GET)
    public ModelAndView getStatisticsFormPage() {
        return statisticsModelProvider.getModelAndViewStatisticsFormPage();
    }

    @RequestMapping(value = "/statistics", method = RequestMethod.POST)
    public ModelAndView getStatisticsResultPage(@ModelAttribute StatisticsDto statisticsDto) {
        return statisticsModelProvider.getModelAndViewStatisticsResultPage(statisticsDto);
    }
}