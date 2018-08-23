package charge_your_vehicle.view.controllers;

import charge_your_vehicle.dao.ChargingPointRepository;
import charge_your_vehicle.dto.AddressDto;
import charge_your_vehicle.dto.ChargingPointDto;
import charge_your_vehicle.model.ChargingPoint;
import charge_your_vehicle.model.Coordinates;
import charge_your_vehicle.service.converters.AddressToCoordinatesBean;
import charge_your_vehicle.service.data_filters.DataFilter;
import charge_your_vehicle.service.properties.AppPropertiesBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
public class FindTheClosestByAddressController {

    private ChargingPointRepository chargingPointRepository;
    private DataFilter dataFilter;
    private AddressToCoordinatesBean addressToCoordinatesBean;
    private AppPropertiesBean appPropertiesBean;

    public FindTheClosestByAddressController(ChargingPointRepository chargingPointRepository,
                                             DataFilter dataFilter,
                                             AddressToCoordinatesBean addressToCoordinatesBean,
                                             AppPropertiesBean appPropertiesBean) {
        this.chargingPointRepository = chargingPointRepository;
        this.dataFilter = dataFilter;
        this.addressToCoordinatesBean = addressToCoordinatesBean;
        this.appPropertiesBean = appPropertiesBean;
    }

    public static final Logger LOG = LoggerFactory.getLogger(FindTheClosestByAddressController.class);

    @RequestMapping(value = "/find-the-closest-by-address", method = RequestMethod.GET)
    public ModelAndView getFindTheClosestByAddressFormPage() {
        LOG.info("User searched closest charging station by address");
        ModelAndView modelAndView = new ModelAndView("body-templates/find-the-closest-by-address");
        modelAndView.addObject("title", "Find the closest charging point by address");
        modelAndView.addObject("addressDto", new AddressDto());
        return modelAndView;
    }

    @RequestMapping(value = "/find-the-closest-by-address", method = RequestMethod.POST)
    public ModelAndView getFindTheClosestByAddressResultPage(@ModelAttribute AddressDto addressDto) {

        ModelAndView modelAndView = new ModelAndView("body-templates/find-the-closest-by-address");

        String address = addressDto.getAddress();
        if (address == null || address.isEmpty()) {
            modelAndView.addObject("error", "Address can't be empty");
            return modelAndView;
        } else {
            Coordinates coordinates = addressToCoordinatesBean.getCoordinates(address);
            if (coordinates != null) {
                double longitude = coordinates.getLongitude();
                double latitude = coordinates.getLatitude();
                List<ChargingPoint> chargingPointsList = new ArrayList<>();
                ChargingPoint chargingPoint = dataFilter.findClosestChargingStation(chargingPointRepository.findAll(), longitude, latitude);
                chargingPointsList.add(chargingPoint);
                List<ChargingPointDto> chargingPointsDtoList = ChargingPointDto.convertFromChargingPointList(chargingPointsList);

                modelAndView = new ModelAndView("body-templates/results");
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
