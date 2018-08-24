package charge_your_vehicle.controller.search;

import charge_your_vehicle.repository.ChargingPointRepository;
import charge_your_vehicle.model.dto.AddressDto;
import charge_your_vehicle.model.dto.ChargingPointDto;
import charge_your_vehicle.model.entity.charging_points_data.ChargingPoint;
import charge_your_vehicle.model.gmaps_api.Coordinates;
import charge_your_vehicle.service.converters.AddressToCoordinatesBean;
import charge_your_vehicle.service.data_filters.DataFilter;
import charge_your_vehicle.service.properties.AppPropertiesBean;
import charge_your_vehicle.service.formaters.Formaters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class FindTheClosestInRadiusByAddressController {

    private ChargingPointRepository chargingPointRepository;
    private DataFilter dataFilter;
    private AppPropertiesBean appPropertiesBean;
    private AddressToCoordinatesBean addressToCoordinatesBean;

    public FindTheClosestInRadiusByAddressController(ChargingPointRepository chargingPointRepository,
                                                     DataFilter dataFilter,
                                                     AppPropertiesBean appPropertiesBean,
                                                     AddressToCoordinatesBean addressToCoordinatesBean) {
        this.chargingPointRepository = chargingPointRepository;
        this.dataFilter = dataFilter;
        this.appPropertiesBean = appPropertiesBean;
        this.addressToCoordinatesBean = addressToCoordinatesBean;
    }

    public static final Logger LOG = LoggerFactory.getLogger(FindTheClosestInRadiusByAddressController.class);

    @RequestMapping(value = "/find-the-closest-in-radius-by-address", method = RequestMethod.GET)
    public ModelAndView getFindTheClosestInRadiusByAddressFormPage() {
        LOG.info("User searched charging station at the area");
        ModelAndView modelAndView = new ModelAndView("body-templates/find-the-closest-in-radius-by-address");
        modelAndView.addObject("title", "Find all charging points in radius by address");
        modelAndView.addObject("addressDto", new AddressDto());
        return modelAndView;
    }

    @RequestMapping(value = "/find-the-closest-in-radius-by-address", method = RequestMethod.POST)
    public ModelAndView getFindTheClosestInRadiusByAddressResultPage(@ModelAttribute AddressDto addressDto) {

        ModelAndView modelAndView = new ModelAndView("body-templates/find-the-closest-in-radius-by-address");

        String radiusString = addressDto.getRadius();
        String address = addressDto.getAddress();

        boolean isRadiusStringNull = (radiusString == null || radiusString.isEmpty());
        boolean isRadiusCorrect = false;

        if (!isRadiusStringNull) {
            isRadiusCorrect = (radiusString.length() < 10);
        }
        if (address == null || address.isEmpty()) {
            modelAndView.addObject("current_unit", Formaters.naturalFormat(appPropertiesBean.getCurrentUnit().name()));
            modelAndView.addObject("error", "Address can't be empty");
            return modelAndView;
        } else if (!isRadiusCorrect) {
            modelAndView.addObject("current_unit", Formaters.naturalFormat(appPropertiesBean.getCurrentUnit().name()));
            modelAndView.addObject("error", "Wrong radius. Value should be less then 999 999 999");
            return modelAndView;
        } else {
            Coordinates coordinates = addressToCoordinatesBean.getCoordinates(address);
            if (coordinates != null) {
                double radius = Double.valueOf(radiusString);
                double longitude = coordinates.getLongitude();
                double latitude = coordinates.getLatitude();

                List<ChargingPoint> chargingPointsList = dataFilter
                        .findChargingStationAtArea(chargingPointRepository.findAll(), longitude,
                                latitude, radius);

                List<ChargingPointDto> chargingPointsDtoList = ChargingPointDto.convertFromChargingPointList(chargingPointsList);
                modelAndView = new ModelAndView("body-templates/results");
                modelAndView.addObject("points-map", "results");
                modelAndView.addObject("chargingPoints", chargingPointsDtoList);
                modelAndView.addObject("chargingPointsSize", chargingPointsDtoList.size());
                modelAndView.addObject("google_api_key", appPropertiesBean.getGoogleApiKey());
            } else {
                modelAndView.addObject("error", "Google converter couldn't get coordinates from this address");
                return modelAndView;
            }
        }
        return modelAndView;
    }
}
