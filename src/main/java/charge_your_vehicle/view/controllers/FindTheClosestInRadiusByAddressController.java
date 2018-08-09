package charge_your_vehicle.view.controllers;

import charge_your_vehicle.dao.ChargingPointDao;
import charge_your_vehicle.dto.ChargingPointDto;
import charge_your_vehicle.model.ChargingPoint;
import charge_your_vehicle.model.Coordinates;
import charge_your_vehicle.service.converters.AddressToCoordinatesBean;
import charge_your_vehicle.service.converters.CoordinatesConverter;
import charge_your_vehicle.service.data_filters.DataFilter;
import charge_your_vehicle.service.promoted.ChargingPointToDtoConverterBean;
import charge_your_vehicle.service.properties.AppPropertiesBean;
import charge_your_vehicle.view.commons.Formaters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class FindTheClosestInRadiusByAddressController {

    private ChargingPointDao chargingPointDao;
    private DataFilter dataFilter;
    private CoordinatesConverter coordinatesConverter;
    private AppPropertiesBean appPropertiesBean;
    private ChargingPointToDtoConverterBean chargingPointToDtoConverterBean;
    private AddressToCoordinatesBean addressToCoordinatesBean;

    public FindTheClosestInRadiusByAddressController(ChargingPointDao chargingPointDao,
                                                     DataFilter dataFilter,
                                                     CoordinatesConverter coordinatesConverter,
                                                     AppPropertiesBean appPropertiesBean,
                                                     ChargingPointToDtoConverterBean chargingPointToDtoConverterBean,
                                                     AddressToCoordinatesBean addressToCoordinatesBean) {
        this.chargingPointDao = chargingPointDao;
        this.dataFilter = dataFilter;
        this.coordinatesConverter = coordinatesConverter;
        this.appPropertiesBean = appPropertiesBean;
        this.chargingPointToDtoConverterBean = chargingPointToDtoConverterBean;
        this.addressToCoordinatesBean = addressToCoordinatesBean;
    }

    public static final Logger LOG = LoggerFactory.getLogger(FindTheClosestInRadiusByAddressController.class);

    @RequestMapping(value = "/find-the-closest-in-radius-by-address", method = RequestMethod.GET)
    public ModelAndView getFindTheClosestPage(HttpSession session) {

        LOG.info("User searched charging station at the area");

        ModelAndView modelAndView = new ModelAndView("body-templates/find-the-closest-in-radius-by-address");
        modelAndView.addObject("title", "Find all charging points in radius by address");

        String radiusString = (String) session.getAttribute("radius");
        String address = (String) session.getAttribute("address");

        boolean isRadiusStringNull = (radiusString == null || radiusString.isEmpty());
        boolean isRadiusCorrect = false;

        if (!isRadiusStringNull) {
            isRadiusCorrect = (radiusString.length() < 10);
        }
        if (address == null || address.isEmpty()) {
            modelAndView.addObject("body_template", "find-the-closest-in-radius-by-address");
            modelAndView.addObject("current_unit", Formaters.naturalFormat(appPropertiesBean.getCurrentUnit().name()));
        } else if (!isRadiusCorrect) {
            modelAndView.addObject("body_template", "find-the-closest-in-radius-by-address");
            modelAndView.addObject("current_unit", Formaters.naturalFormat(appPropertiesBean.getCurrentUnit().name()));
            modelAndView.addObject("error", "Wrong radius. Value should be less then 999 999 999");
        } else {
            Coordinates coordinates = addressToCoordinatesBean.getCoordinates(address);
            if (coordinates != null) {
                double radius = Double.valueOf(radiusString);
                double longitude = coordinates.getLongitude();
                double latitude = coordinates.getLatitude();

                List<ChargingPoint> chargingPointsList = dataFilter
                        .findChargingStationAtArea(chargingPointDao.findAll(), longitude,
                                latitude, radius);

                List<ChargingPointDto> chargingPointsDtoList = chargingPointToDtoConverterBean.convertList(chargingPointsList);
                modelAndView = new ModelAndView("body-templates/results");
                modelAndView.addObject("points-map", "results");
                modelAndView.addObject("chargingPoints", chargingPointsDtoList);
                modelAndView.addObject("google_api_key", appPropertiesBean.getGoogleApiKey());
            } else {
                return modelAndView;
            }
        }
        return modelAndView;
    }
}
