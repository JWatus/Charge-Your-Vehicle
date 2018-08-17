package charge_your_vehicle.view.servlets;

import charge_your_vehicle.dao.CountryStatisticsRepository;
import charge_your_vehicle.dao.TownStatisticsRepository;
import charge_your_vehicle.dto.StatisticsDto;
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
public class StatisticsController extends HttpServlet {

    private CountryStatisticsRepository countryStatisticsRepository;
    private TownStatisticsRepository townStatisticsRepository;

    public StatisticsController(CountryStatisticsRepository countryStatisticsRepository, TownStatisticsRepository townStatisticsRepository) {
        this.countryStatisticsRepository = countryStatisticsRepository;
        this.townStatisticsRepository = townStatisticsRepository;
    }

    public static final Logger LOG = LoggerFactory.getLogger(StatisticsController.class);

    @RequestMapping(value = "/statistics", method = RequestMethod.GET)
    public ModelAndView getStatisticsPage() {
        ModelAndView modelAndView = new ModelAndView("body-templates/statistics");
        modelAndView.addObject("body_template", "statistics");
        modelAndView.addObject("title", "Statistics");
        modelAndView.addObject("statisticsDto", new StatisticsDto());
        return modelAndView;
    }

    @RequestMapping(value = "/statistics", method = RequestMethod.POST)
    public ModelAndView getStatisticsResultPage(@ModelAttribute StatisticsDto statisticsDto) {

        String location = statisticsDto.getLocation();

        ModelAndView modelAndView = new ModelAndView("body-templates/statistics");
        modelAndView.addObject("title", "Statistics");

        if (location.equals("town")) {
            List locationList = townStatisticsRepository.findAllOrderByNumberOfVisitsDesc();
            if (locationList.size() == 0) {
                modelAndView.addObject("error", "Noone was searching charging points by town name");
            } else {
                modelAndView = new ModelAndView("body-templates/show-statistics");
                modelAndView.addObject("locationList", locationList);
            }
        } else if (location.equals("country")) {
            List locationList = countryStatisticsRepository.findAllOrderByNumberOfVisitsDesc();
            if (locationList.size() == 0) {
                modelAndView.addObject("error", "Noone was searching charging points by country name");
            } else {
                modelAndView = new ModelAndView("body-templates/show-statistics");
                modelAndView.addObject("locationList", locationList);
            }
        }

        return modelAndView;
    }
}