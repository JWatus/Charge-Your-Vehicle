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

import java.util.ArrayList;
import java.util.List;

@Controller
public class FindTheClosestController {
    public static final Logger LOG = LoggerFactory.getLogger(FindTheClosestInRadiusController.class);

    private ChargingPointRepository chargingPointRepository;
    private DataFilter dataFilter;
    private CoordinatesConverter coordinatesConverter;
    private AppPropertiesBean appPropertiesBean;

    public FindTheClosestController(ChargingPointRepository chargingPointRepository,
                                    DataFilter dataFilter,
                                    CoordinatesConverter coordinatesConverter,
                                    AppPropertiesBean appPropertiesBean) {
        this.chargingPointRepository = chargingPointRepository;
        this.dataFilter = dataFilter;
        this.coordinatesConverter = coordinatesConverter;
        this.appPropertiesBean = appPropertiesBean;
    }

    @RequestMapping(value = "/find-the-closest", method = RequestMethod.GET)
    public ModelAndView getFindTheClosestFormPage() {

        LOG.info("User searched closest charging station");

        ModelAndView modelAndView = new ModelAndView("body-templates/find-the-closest");
        modelAndView.addObject("title", "Find the closest charging point");
        modelAndView.addObject("coordinatesDto", new CoordinatesDto());
        return modelAndView;
    }

    @RequestMapping(value = "/find-the-closest", method = RequestMethod.POST)
    public ModelAndView getFindTheClosestResultPage(@ModelAttribute CoordinatesDto coordinatesDto) {

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
            errorMessages(modelAndView);
        } else if ((isDegreesLongNull && isMinutesLongNull && isSecondsLongNull)
                || (isDegreesLatiNull && isMinutesLatiNull && isSecondsLatiNull)) {
            errorMessages(modelAndView);
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
                    double longitude = coordinatesConverter.convertCoordinatesToDecimal(directionLong, degreesLong, minutesLong, secondsLong);
                    double latitude = coordinatesConverter.convertCoordinatesToDecimal(directionLati, degreesLati, minutesLati, secondsLati);
                    List<ChargingPoint> chargingPointsList = new ArrayList<>();
                    ChargingPoint chargingPoint = dataFilter.findClosestChargingStation(chargingPointRepository.findAll(), longitude, latitude);
                    chargingPointsList.add(chargingPoint);
                    List<ChargingPointDto> chargingPointsDtoList = ChargingPointDto.convertFromChargingPointList(chargingPointsList);
                    modelAndView = new ModelAndView("body-templates/results");
                    modelAndView.addObject("chargingPoints", chargingPointsDtoList);
                    modelAndView.addObject("title", "Find the closest charging point");
                    modelAndView.addObject("latitude", latitude);
                    modelAndView.addObject("longitude", longitude);
                    modelAndView.addObject("google_api_key", appPropertiesBean.getGoogleApiKey());
                } catch (Exception e) {
                    modelAndView.addObject("title", "Find the closest charging point");
                    modelAndView.addObject("error", "No charging points were found");
                    LOG.error("No charging points were found");
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
        modelAndView.addObject("title", "Find the closest charging point");
        modelAndView.addObject("error", "Please fill all fields in the form with correct value");
    }

    private boolean isStringInRange(String value, int min, int max) {
        Double coordinateDouble = Double.valueOf(value);
        return coordinateDouble >= min && coordinateDouble <= max;
    }
}
