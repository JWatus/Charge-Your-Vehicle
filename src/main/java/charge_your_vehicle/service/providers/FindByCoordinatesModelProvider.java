package charge_your_vehicle.service.providers;

import charge_your_vehicle.model.dto.ChargingPointDto;
import charge_your_vehicle.model.dto.CoordinatesDto;
import charge_your_vehicle.model.entity.charging_points_data.ChargingPoint;
import charge_your_vehicle.repository.ChargingPointRepository;
import charge_your_vehicle.service.converters.CoordinatesConverter;
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
public class FindByCoordinatesModelProvider {

    private static final Logger LOG = LoggerFactory.getLogger(FindByCoordinatesModelProvider.class);

    private ChargingPointRepository chargingPointRepository;
    private DataFilter dataFilter;
    private CoordinatesConverter coordinatesConverter;
    private AppPropertiesBean appPropertiesBean;

    public FindByCoordinatesModelProvider(ChargingPointRepository chargingPointRepository,
                                          DataFilter dataFilter,
                                          CoordinatesConverter coordinatesConverter,
                                          AppPropertiesBean appPropertiesBean) {
        this.chargingPointRepository = chargingPointRepository;
        this.dataFilter = dataFilter;
        this.coordinatesConverter = coordinatesConverter;
        this.appPropertiesBean = appPropertiesBean;
    }

    /*  FIND THE CLOSEST BY COORDINATES */

    public ModelAndView getModelAndViewFindTheClosestFormPage() {
        LOG.info("User searched closest charging station");
        ModelAndView modelAndView = new ModelAndView("body-templates/find-the-closest");
        modelAndView.addObject("title", "Find the closest charging point");
        modelAndView.addObject("coordinatesDto", new CoordinatesDto());
        return modelAndView;
    }

    public ModelAndView getModelAndViewFindTheClosestResultPage(@ModelAttribute CoordinatesDto coordinatesDto) {
        ModelAndView modelAndView = new ModelAndView("body-templates/find-the-closest");

        String directionLong = coordinatesDto.getDirectionLong();
        String degreesLong = coordinatesDto.getDegreesLong();
        String minutesLong = coordinatesDto.getMinutesLong();
        String secondsLong = coordinatesDto.getSecondsLong();
        String directionLati = coordinatesDto.getDirectionLati();
        String degreesLati = coordinatesDto.getDegreesLati();
        String minutesLati = coordinatesDto.getMinutesLati();
        String secondsLati = coordinatesDto.getSecondsLati();

        boolean isDegreesLongNull = (degreesLong == null || degreesLong.isEmpty());
        boolean isMinutesLongNull = (minutesLong == null || minutesLong.isEmpty());
        boolean isSecondsLongNull = (secondsLong == null || secondsLong.isEmpty());
        boolean isDegreesLatiNull = (degreesLati == null || degreesLati.isEmpty());
        boolean isMinutesLatiNull = (minutesLati == null || minutesLati.isEmpty());
        boolean isSecondsLatiNull = (secondsLati == null || secondsLati.isEmpty());

        if ((isDegreesLongNull && isMinutesLongNull && isSecondsLongNull)
                && (isDegreesLatiNull && isMinutesLatiNull && isSecondsLatiNull)) {
            return getErrorMessage(modelAndView,
                    "Please fill at least both degrees fields in the form with correct value",
                    "Find the closest charging point");
        } else if ((isDegreesLongNull && isMinutesLongNull && isSecondsLongNull)
                || (isDegreesLatiNull && isMinutesLatiNull && isSecondsLatiNull)) {
            return getErrorMessage(modelAndView,
                    "Please fill at least both degrees fields in the form with correct value",
                    "Find the closest charging point");
        } else {
            if (isDegreesLongNull) degreesLong = "0";
            if (isMinutesLongNull) minutesLong = "0";
            if (isSecondsLongNull) secondsLong = "0";
            if (isDegreesLatiNull) degreesLati = "0";
            if (isMinutesLatiNull) minutesLati = "0";
            if (isSecondsLatiNull) secondsLati = "0";

            if (isStringInRange(degreesLati, 0, 90)
                    && isStringInRange(minutesLati, 0, 60)
                    && isStringInRange(secondsLati, 0, 60)
                    && isStringInRange(degreesLong, 0, 180)
                    && isStringInRange(minutesLong, 0, 60)
                    && isStringInRange(secondsLong, 0, 60)) {
                try {
                    return findTheClosestChargingPointToChosenCoordinates(modelAndView,
                            directionLong, degreesLong, minutesLong, secondsLong,
                            directionLati, degreesLati, minutesLati, secondsLati);
                } catch (Exception e) {
                    LOG.error("No charging points were found");
                    return getErrorMessage(modelAndView,
                            "No charging points were found",
                            "Find the closest charging point");
                }
            } else {
                return getErrorMessage(modelAndView,
                        "Please fill at least both degrees fields in the form with correct value",
                        "Find the closest charging point");
            }
        }
    }

    private ModelAndView findTheClosestChargingPointToChosenCoordinates(ModelAndView modelAndView,
                                                                        String directionLong, String degreesLong, String minutesLong, String secondsLong,
                                                                        String directionLati, String degreesLati, String minutesLati, String secondsLati) {
        double longitude = coordinatesConverter.convertCoordinatesToDecimal(directionLong, degreesLong, minutesLong, secondsLong);
        double latitude = coordinatesConverter.convertCoordinatesToDecimal(directionLati, degreesLati, minutesLati, secondsLati);
        List<ChargingPointDto> chargingPointsDtoList = getChargingPointDtos(longitude, latitude);
        modelAndView = new ModelAndView("body-templates/results");
        modelAndView.addObject("title", "Find the closest charging point");
        addObjectsToModelAndView(modelAndView, longitude, latitude, chargingPointsDtoList);
        return modelAndView;
    }

    private List<ChargingPointDto> getChargingPointDtos(double longitude, double latitude) {
        List<ChargingPoint> chargingPointsList = new ArrayList<ChargingPoint>();
        ChargingPoint chargingPoint = dataFilter.findClosestChargingStation(chargingPointRepository.findAll(), longitude, latitude);
        chargingPointsList.add(chargingPoint);
        return ChargingPointDto.convertFromChargingPointList(chargingPointsList);
    }

    /*  FIND THE CLOSEST BY COORDINATES IN RADIUS  */

    public ModelAndView getModelAndViewFindTheClosestInRadiusFormPage() {
        ModelAndView modelAndView = new ModelAndView("body-templates/find-the-closest-in-radius");
        modelAndView.addObject("title", "Find all charging points in radius");
        modelAndView.addObject("current_unit", Formaters.naturalFormat(appPropertiesBean.getCurrentUnit().name()));
        modelAndView.addObject("coordinatesDto", new CoordinatesDto());
        return modelAndView;
    }

    public ModelAndView getModelAndViewFindTheClosestInRadiusResultPage(@ModelAttribute CoordinatesDto coordinatesDto) {
        ModelAndView modelAndView = new ModelAndView("body-templates/find-the-closest-in-radius");

        String directionLong = coordinatesDto.getDirectionLong();
        String degreesLong = coordinatesDto.getDegreesLong();
        String minutesLong = coordinatesDto.getMinutesLong();
        String secondsLong = coordinatesDto.getSecondsLong();
        String directionLati = coordinatesDto.getDirectionLati();
        String degreesLati = coordinatesDto.getDegreesLati();
        String minutesLati = coordinatesDto.getMinutesLati();
        String secondsLati = coordinatesDto.getSecondsLati();
        String radiusString = coordinatesDto.getRadius();

        boolean isDegreesLongNull = (degreesLong == null || degreesLong.isEmpty());
        boolean isMinutesLongNull = (minutesLong == null || minutesLong.isEmpty());
        boolean isSecondsLongNull = (secondsLong == null || secondsLong.isEmpty());
        boolean isDegreesLatiNull = (degreesLati == null || degreesLati.isEmpty());
        boolean isMinutesLatiNull = (minutesLati == null || minutesLati.isEmpty());
        boolean isSecondsLatiNull = (secondsLati == null || secondsLati.isEmpty());
        boolean isRadiusStringNull = (radiusString == null || radiusString.isEmpty());

        if ((isDegreesLongNull && isMinutesLongNull && isSecondsLongNull)
                && (isDegreesLatiNull && isMinutesLatiNull && isSecondsLatiNull)
                && isRadiusStringNull) {
            return getErrorMessage(modelAndView,
                    "Please fill at least both degrees and radius fields in the form with correct value",
                    "Find all charging points in radius");
        } else if ((isDegreesLongNull && isMinutesLongNull && isSecondsLongNull)
                || (isDegreesLatiNull && isMinutesLatiNull && isSecondsLatiNull)
                || isRadiusStringNull) {
            return getErrorMessage(modelAndView,
                    "Please fill at least both degrees and radius fields in the form with correct value",
                    "Find all charging points in radius");
        } else {
            if (isDegreesLongNull) degreesLong = "0";
            if (isMinutesLongNull) minutesLong = "0";
            if (isSecondsLongNull) secondsLong = "0";
            if (isDegreesLatiNull) degreesLati = "0";
            if (isMinutesLatiNull) minutesLati = "0";
            if (isSecondsLatiNull) secondsLati = "0";
            if (isRadiusStringNull) radiusString = "0";
            if (radiusString.length() < 10) {
                if (isStringInRange(degreesLati, 0, 90)
                        && isStringInRange(minutesLati, 0, 60)
                        && isStringInRange(secondsLati, 0, 60)
                        && isStringInRange(degreesLong, 0, 180)
                        && isStringInRange(minutesLong, 0, 60)
                        && isStringInRange(secondsLong, 0, 60)
                        && isStringInRange(radiusString, 0, Integer.MAX_VALUE)) {
                    try {
                        return findAllPointsInTheIndicatedArea(modelAndView,
                                directionLong, degreesLong, minutesLong, secondsLong,
                                directionLati, degreesLati, minutesLati, secondsLati,
                                radiusString);
                    } catch (Exception e) {
                        LOG.error("Exception was catched.");
                        return getErrorMessage(modelAndView,
                                "No charging points were found",
                                "Find all charging points in radius");
                    }
                } else {
                    return getErrorMessage(modelAndView,
                            "Please fill at least both degrees and radius fields in the form with correct value",
                            "Find all charging points in radius");
                }
            } else {
                return getErrorMessage(modelAndView,
                        "Please fill radius field with correct value",
                        "Find all charging points in radius");
            }
        }
    }

    private ModelAndView findAllPointsInTheIndicatedArea(ModelAndView modelAndView,
                                                         String directionLong, String degreesLong, String minutesLong, String secondsLong,
                                                         String directionLati, String degreesLati, String minutesLati, String secondsLati,
                                                         String radiusString) {
        double radius = Double.valueOf(radiusString);
        double longitude = coordinatesConverter.convertCoordinatesToDecimal(directionLong, degreesLong, minutesLong, secondsLong);
        double latitude = coordinatesConverter.convertCoordinatesToDecimal(directionLati, degreesLati, minutesLati, secondsLati);

        List<ChargingPoint> chargingPointsList = dataFilter.findChargingStationAtArea(
                chargingPointRepository.findAll(), longitude, latitude, radius);
        List<ChargingPointDto> chargingPointsDtoList = ChargingPointDto.convertFromChargingPointList(chargingPointsList);

        if (chargingPointsDtoList.size() > 0) {
            modelAndView = new ModelAndView("body-templates/results");
            modelAndView.addObject("current_unit", Formaters.naturalFormat(appPropertiesBean.getCurrentUnit().name()));
            modelAndView.addObject("title", "Find all charging points in radius");
            addObjectsToModelAndView(modelAndView, longitude, latitude, chargingPointsDtoList);
            return modelAndView;
        } else {
            LOG.error("No charging points were found in given radius");
            return getErrorMessage(modelAndView,
                    "No charging points were found",
                    "Find all charging points in radius");
        }
    }

    private void addObjectsToModelAndView(ModelAndView modelAndView,
                                          double longitude,
                                          double latitude,
                                          List<ChargingPointDto> chargingPointsDtoList) {
        modelAndView.addObject("chargingPoints", chargingPointsDtoList);
        modelAndView.addObject("chargingPointsSize", chargingPointsDtoList.size());
        modelAndView.addObject("latitude", latitude);
        modelAndView.addObject("longitude", longitude);
        modelAndView.addObject("google_api_key", appPropertiesBean.getGoogleApiKey());
    }

    private ModelAndView getErrorMessage(ModelAndView modelAndView, String errorMessage, String title) {
        modelAndView.addObject("error", errorMessage);
        modelAndView.addObject("title", title);
        modelAndView.addObject("current_unit", Formaters.naturalFormat(appPropertiesBean.getCurrentUnit().name()));
        return modelAndView;
    }

    private boolean isStringInRange(String value, int min, int max) {
        Double coordinateDouble = Double.valueOf(value);
        return coordinateDouble >= min && coordinateDouble <= max;
    }
}