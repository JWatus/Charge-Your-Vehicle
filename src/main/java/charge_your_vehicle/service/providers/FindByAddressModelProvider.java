package charge_your_vehicle.service.providers;

import charge_your_vehicle.model.dto.AddressDto;
import charge_your_vehicle.model.dto.ChargingPointDto;
import charge_your_vehicle.model.entity.charging_points_data.ChargingPoint;
import charge_your_vehicle.model.gmaps_api.Coordinates;
import charge_your_vehicle.repository.ChargingPointRepository;
import charge_your_vehicle.service.converters.AddressToCoordinatesBean;
import charge_your_vehicle.service.data_filters.DataFilter;
import charge_your_vehicle.service.formaters.Formaters;
import charge_your_vehicle.service.properties.AppPropertiesBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Service
public class FindByAddressModelProvider {

    private static final Logger LOG = LoggerFactory.getLogger(FindByAddressModelProvider.class);

    private ChargingPointRepository chargingPointRepository;
    private DataFilter dataFilter;
    private AddressToCoordinatesBean addressToCoordinatesBean;
    private AppPropertiesBean appPropertiesBean;

    public FindByAddressModelProvider(ChargingPointRepository chargingPointRepository,
                                      DataFilter dataFilter,
                                      AddressToCoordinatesBean addressToCoordinatesBean,
                                      AppPropertiesBean appPropertiesBean) {
        this.chargingPointRepository = chargingPointRepository;
        this.dataFilter = dataFilter;
        this.addressToCoordinatesBean = addressToCoordinatesBean;
        this.appPropertiesBean = appPropertiesBean;
    }

    /* FIND CLOSEST POINT TO CHOSEN ADDRESS */

    public ModelAndView getModelAndViewFindTheClosestByAddressFormPage() {
        LOG.info("User searched closest charging station by address");
        ModelAndView modelAndView = new ModelAndView("body-templates/find-the-closest-by-address");
        modelAndView.addObject("title", "Find the closest charging point by address");
        modelAndView.addObject("addressDto", new AddressDto());
        return modelAndView;
    }

    public ModelAndView getModelAndViewFindTheClosestByAddressResultPage(@ModelAttribute AddressDto addressDto) {
        ModelAndView modelAndView = new ModelAndView("body-templates/find-the-closest-by-address");
        modelAndView.addObject("title", "Find the closest charging point by address");
        String address = addressDto.getAddress();
        if (address == null || address.isEmpty()) {
            return getErrorMessage(modelAndView, "Address can't be empty");
        } else {
            return getClosestPointToConvertedAddress(modelAndView, address);
        }
    }

    private ModelAndView getClosestPointToConvertedAddress(ModelAndView modelAndView, String address) {
        Coordinates coordinates = addressToCoordinatesBean.getCoordinates(address);
        if (coordinates != null) {
            double longitude = coordinates.getLongitude();
            double latitude = coordinates.getLatitude();
            List<ChargingPointDto> chargingPointsDtoList = findClosestPointToAddress(longitude, latitude);
            if (!chargingPointsDtoList.isEmpty()) {
                modelAndView = new ModelAndView("body-templates/results");
                addObjectsToModelAndView(modelAndView, longitude, latitude, chargingPointsDtoList);
            } else {
                return getErrorMessage(modelAndView, "There is not loaded any charging point to database");
            }
            return modelAndView;
        } else {
            return getErrorMessage(modelAndView, "Google converter couldn't get coordinates from this address");
        }
    }

    private List<ChargingPointDto> findClosestPointToAddress(double longitude, double latitude) {
        List<ChargingPoint> chargingPointsList = new ArrayList<>();
        ChargingPoint chargingPoint = dataFilter.findClosestChargingStation(chargingPointRepository.findAll(), longitude, latitude);
        if (chargingPoint != null) {
            chargingPointsList.add(chargingPoint);
            return ChargingPointDto.convertFromChargingPointList(chargingPointsList);
        } else {
            return new ArrayList<>();
        }
    }

    /* FIND ALL POINTS BY ADDRESS IN RADIUS */

    public ModelAndView getModelAndViewFindTheClosestInRadiusByAddressFormPage() {
        LOG.info("User searched charging station at the area");
        ModelAndView modelAndView = new ModelAndView("body-templates/find-the-closest-in-radius-by-address");
        modelAndView.addObject("title", "Find all charging points in radius by address");
        modelAndView.addObject("current_unit", Formaters.naturalFormat(appPropertiesBean.getCurrentUnit().name()));
        modelAndView.addObject("addressDto", new AddressDto());
        return modelAndView;
    }

    public ModelAndView getModelAndViewFindTheClosestInRadiusByAddressResultPage(@ModelAttribute AddressDto addressDto) {
        ModelAndView modelAndView = new ModelAndView("body-templates/find-the-closest-in-radius-by-address");
        modelAndView.addObject("title", "Find all charging points in radius by address");
        modelAndView.addObject("current_unit", Formaters.naturalFormat(appPropertiesBean.getCurrentUnit().name()));
        String radiusString = addressDto.getRadius();
        String address = addressDto.getAddress();
        boolean isRadiusStringNull = (radiusString == null || radiusString.isEmpty());
        boolean isRadiusCorrect = false;
        if (!isRadiusStringNull) {
            isRadiusCorrect = (radiusString.length() < 10);
        }
        if (address == null || address.isEmpty()) {
            return getErrorMessage(modelAndView, "Address can't be empty");
        } else if (!isRadiusCorrect) {
            return getErrorMessage(modelAndView, "Wrong radius. Value should be less then 999 999 999");
        } else {
            return getAllPointsInRadiusForAddress(modelAndView, radiusString, address);
        }
    }

    private ModelAndView getAllPointsInRadiusForAddress(ModelAndView modelAndView, String radiusString, String address) {
        Coordinates coordinates = addressToCoordinatesBean.getCoordinates(address);
        if (coordinates != null) {
            return filterPointsForGivenCoordinates(modelAndView, radiusString, coordinates);
        } else {
            return getErrorMessage(modelAndView, "Google converter couldn't get coordinates from this address");
        }
    }

    private ModelAndView filterPointsForGivenCoordinates(ModelAndView modelAndView, String radiusString, Coordinates coordinates) {
        double radius = Double.valueOf(radiusString);
        double longitude = coordinates.getLongitude();
        double latitude = coordinates.getLatitude();
        List<ChargingPoint> chargingPointsList = dataFilter.findChargingStationAtArea(chargingPointRepository.findAll(), longitude, latitude, radius);
        if (!chargingPointsList.isEmpty()) {
            List<ChargingPointDto> chargingPointsDtoList = ChargingPointDto.convertFromChargingPointList(chargingPointsList);
            modelAndView = new ModelAndView("body-templates/results");
            addObjectsToModelAndView(modelAndView, longitude, latitude, chargingPointsDtoList);
            return modelAndView;
        } else {
            return getErrorMessage(modelAndView, "No charging points were found in given radius");
        }
    }

    private void addObjectsToModelAndView(ModelAndView modelAndView, double longitude, double latitude, List<ChargingPointDto> chargingPointsDtoList) {
        modelAndView.addObject("chargingPoints", chargingPointsDtoList);
        modelAndView.addObject("chargingPointsSize", chargingPointsDtoList.size());
        modelAndView.addObject("google_api_key", appPropertiesBean.getGoogleApiKey());
        modelAndView.addObject("by_address", "By address");
        modelAndView.addObject("latitude", latitude);
        modelAndView.addObject("longitude", longitude);
    }

    private ModelAndView getErrorMessage(ModelAndView modelAndView, String errorMessage) {
        modelAndView.addObject("error", errorMessage);
        return modelAndView;
    }
}