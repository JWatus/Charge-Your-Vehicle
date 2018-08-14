package charge_your_vehicle.view.controllers;

import charge_your_vehicle.dao.ChargingPointRepository;
import charge_your_vehicle.dto.ChargingPointDto;
import charge_your_vehicle.dto.CoordinatesDto;
import charge_your_vehicle.model.ChargingPoint;
import charge_your_vehicle.service.converters.CoordinatesConverter;
import charge_your_vehicle.service.data_filters.DataFilter;
import charge_your_vehicle.service.properties.AppPropertiesBean;
import charge_your_vehicle.view.commons.Formaters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class FindTheClosestInRadiusController {

    public static final Logger LOG = LoggerFactory.getLogger(FindTheClosestInRadiusController.class);

    private ChargingPointRepository chargingPointRepository;
    private DataFilter dataFilter;
    private CoordinatesConverter coordinatesConverter;
    private AppPropertiesBean appPropertiesBean;

    public FindTheClosestInRadiusController(ChargingPointRepository chargingPointRepository,
                                            DataFilter dataFilter,
                                            CoordinatesConverter coordinatesConverter,
                                            AppPropertiesBean appPropertiesBean) {
        this.chargingPointRepository = chargingPointRepository;
        this.dataFilter = dataFilter;
        this.coordinatesConverter = coordinatesConverter;
        this.appPropertiesBean = appPropertiesBean;
    }

    @RequestMapping(value = "/find-the-closest-in-radius", method = RequestMethod.GET)
    public ModelAndView getFindTheClosestPage() {

        ModelAndView modelAndView = new ModelAndView("body-templates/find-the-closest-in-radius");
        modelAndView.addObject("title", "Find all charging points in radius");
        modelAndView.addObject("current_unit", Formaters.naturalFormat(appPropertiesBean.getCurrentUnit().name()));
        modelAndView.addObject("coordinatesDto", new CoordinatesDto());
        return modelAndView;
    }

    @RequestMapping(value = "/find-the-closest-in-radius", method = RequestMethod.POST)
    public ModelAndView getFindTheClosestResultPage(@ModelAttribute CoordinatesDto coordinatesDto) {

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
            errorMessages(modelAndView);
        } else if ((isDegreesLongNull && isMinutesLongNull && isSecondsLongNull)
                || (isDegreesLatiNull && isMinutesLatiNull && isSecondsLatiNull)
                || isRadiusStringNull) {
            errorMessages(modelAndView);
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
                        double radius = Double.valueOf(radiusString);
                        double longitude = coordinatesConverter.convertCoordinatesToDecimal(directionLong, degreesLong, minutesLong, secondsLong);
                        double latitude = coordinatesConverter.convertCoordinatesToDecimal(directionLati, degreesLati, minutesLati, secondsLati);

                        List<ChargingPoint> chargingPointsList = dataFilter.findChargingStationAtArea(
                                chargingPointRepository.findAll(), longitude, latitude, radius);
                        List<ChargingPointDto> chargingPointsDtoList = ChargingPointDto.convertFromChargingPointList(chargingPointsList);

                        if (chargingPointsDtoList.size() > 0) {
                            modelAndView = new ModelAndView("body-templates/results");
                            modelAndView.addObject("chargingPoints", chargingPointsDtoList);
                            modelAndView.addObject("title", "Find all charging points in radius");
                            modelAndView.addObject("latitude", latitude);
                            modelAndView.addObject("longitude", longitude);
                            modelAndView.addObject("google_api_key", appPropertiesBean.getGoogleApiKey());
                            modelAndView.addObject("current_unit", Formaters.naturalFormat(appPropertiesBean.getCurrentUnit().name()));
                        } else {
                            modelAndView.addObject("title", "Find all charging points in radius");
                            modelAndView.addObject("error", "No charging points were found");
                            modelAndView.addObject("current_unit", Formaters.naturalFormat(appPropertiesBean.getCurrentUnit().name()));
                            LOG.error("No charging points were found");
                            return modelAndView;
                        }
                    } catch (Exception e) {
                        errorMessages(modelAndView);
                        LOG.error("Exception was catched.");
                        return modelAndView;
                    }
                } else {
                    errorMessages(modelAndView);
                    return modelAndView;
                }
            } else {
                errorMessages(modelAndView);
                return modelAndView;
            }
        }
        return modelAndView;
    }

    private void errorMessages(ModelAndView modelAndView) {
        modelAndView.addObject("title", "Find all charging points in radius");
        modelAndView.addObject("error", "Please fill all fields in the form with correct value");
        modelAndView.addObject("current_unit", Formaters.naturalFormat(appPropertiesBean.getCurrentUnit().name()));
    }

    private boolean isStringInRange(String value, int min, int max) {
        Double coordinateDouble = Double.valueOf(value);
        return coordinateDouble >= min && coordinateDouble <= max;
    }
}
