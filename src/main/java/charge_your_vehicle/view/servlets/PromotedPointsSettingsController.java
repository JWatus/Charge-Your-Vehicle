package charge_your_vehicle.view.servlets;

import charge_your_vehicle.dao.ChargingPointRepository;
import charge_your_vehicle.dto.ChargingPointDto;
import charge_your_vehicle.dto.PromotedDto;
import charge_your_vehicle.service.promoted.ChargingPointToDtoConverterBean;
import charge_your_vehicle.service.promoted.PromotedChargingPointsBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class PromotedPointsSettingsController extends HttpServlet {

    public static final Logger LOG = LoggerFactory.getLogger(PromotedPointsSettingsController.class);

    private PromotedChargingPointsBean promotedChargingPointsBean;
    private ChargingPointRepository chargingPointRepository;
    private ChargingPointToDtoConverterBean chargingPointToDtoConverterBean;

    public PromotedPointsSettingsController(PromotedChargingPointsBean promotedChargingPointsBean,
                                            ChargingPointRepository chargingPointRepository,
                                            ChargingPointToDtoConverterBean chargingPointToDtoConverterBean) {
        this.promotedChargingPointsBean = promotedChargingPointsBean;
        this.chargingPointRepository = chargingPointRepository;
        this.chargingPointToDtoConverterBean = chargingPointToDtoConverterBean;
    }

    @RequestMapping(value = "/administration/promoted-settings", method = RequestMethod.GET)
    public ModelAndView getPromotedPage() {
        ModelAndView modelAndView = new ModelAndView("body-templates/promoted-settings");
        List<ChargingPointDto> chargingPointsDtoList = ChargingPointDto.convertFromChargingPointList(chargingPointRepository.findAll());
        modelAndView.addObject("title", "Promoted Charging Points Settings");
        modelAndView.addObject("chargingPoints", chargingPointsDtoList);
        return modelAndView;
    }

    @RequestMapping(value = "/administration/promoted-settings", method = RequestMethod.POST)
    public ModelAndView getPromotedResultPage(@ModelAttribute PromotedDto promotedDto) {

        ModelAndView modelAndView = new ModelAndView("body-templates/promoted-settings");

        String action = promotedDto.getAction();
        if (action != null && !action.isEmpty()) {
            String idString = promotedDto.getId();
            if (idString != null && !idString.isEmpty()) {
                Integer id = Integer.parseInt(idString);
                if (action.equalsIgnoreCase("remove")) {
                    promotedChargingPointsBean.removeFromPromoted(id);
                }
                if (action.equalsIgnoreCase("add")) {
                    promotedChargingPointsBean.addToPromoted(id);
                }
            }
            return modelAndView;
        }

        List<ChargingPointDto> chargingPointsDtoList = ChargingPointDto.convertFromChargingPointList(chargingPointRepository.findAll());
        modelAndView.addObject("title", "Promoted Charging Points Settings");
        modelAndView.addObject("chargingPoints", chargingPointsDtoList);
        return modelAndView;
    }


}
