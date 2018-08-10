package charge_your_vehicle.view.servlets;

import charge_your_vehicle.dao.ChargingPointDao;
import charge_your_vehicle.dto.ChargingPointDto;
import charge_your_vehicle.service.promoted.ChargingPointToDtoConverterBean;
import charge_your_vehicle.service.promoted.PromotedChargingPointsBean;
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
public class PromotedPointsSettingsController extends HttpServlet {

    public static final Logger LOG = LoggerFactory.getLogger(PromotedPointsSettingsController.class);

    private PromotedChargingPointsBean promotedChargingPointsBean;
    private ChargingPointDao chargingPointDao;
    private ChargingPointToDtoConverterBean chargingPointToDtoConverterBean;

    public PromotedPointsSettingsController(PromotedChargingPointsBean promotedChargingPointsBean,
                                            ChargingPointDao chargingPointDao,
                                            ChargingPointToDtoConverterBean chargingPointToDtoConverterBean) {
        this.promotedChargingPointsBean = promotedChargingPointsBean;
        this.chargingPointDao = chargingPointDao;
        this.chargingPointToDtoConverterBean = chargingPointToDtoConverterBean;
    }

    @RequestMapping(value = "/administration/promoted-settings", method = RequestMethod.GET)
    public ModelAndView getFindTheClosestPage(HttpSession session) {

        ModelAndView modelAndView = new ModelAndView("/administration/promoted-settings");

        String action = (String) session.getAttribute("action");
        if (action != null && !action.isEmpty()) {
            String idString = (String) session.getAttribute("id");
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

        List<ChargingPointDto> chargingPointsDtoList = chargingPointToDtoConverterBean.convertList(chargingPointDao.findAll());
        modelAndView.addObject("title", "Promoted Charging Points Settings");
        modelAndView.addObject("chargingPointDtoList", chargingPointsDtoList);

        return modelAndView;
    }
}
