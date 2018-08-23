package charge_your_vehicle.controller;

import charge_your_vehicle.repository.ChargingPointRepository;
import charge_your_vehicle.model.dto.ChargingPointDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class AllLoadedPointsController {

    public static final Logger LOG = LoggerFactory.getLogger(AllLoadedPointsController.class);

    private ChargingPointRepository chargingPointRepository;

    public AllLoadedPointsController(ChargingPointRepository chargingPointRepository) {
        this.chargingPointRepository = chargingPointRepository;
    }

    @RequestMapping(value = "/administration/all-loaded-points", method = RequestMethod.GET)
    public ModelAndView getAllLoadedPointsPage() {
        ModelAndView modelAndView = new ModelAndView("body-templates/all-loaded-points");
        List<ChargingPointDto> chargingPointsDtoList = ChargingPointDto.convertFromChargingPointList(chargingPointRepository.findAll());
        modelAndView.addObject("title", "All loaded charging points");
        modelAndView.addObject("chargingPoints", chargingPointsDtoList);
        return modelAndView;
    }
}
