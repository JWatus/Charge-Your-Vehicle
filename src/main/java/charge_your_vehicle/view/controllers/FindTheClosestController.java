package charge_your_vehicle.view.controllers;

import charge_your_vehicle.dao.ChargingPointDao;
import charge_your_vehicle.dto.ChargingPointDto;
import charge_your_vehicle.model.ChargingPoint;
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
import java.util.ArrayList;
import java.util.List;

@Controller
public class FindTheClosestController {
    public static final Logger LOG = LoggerFactory.getLogger(FindTheClosestInRadiusController.class);

    private ChargingPointDao chargingPointDao;
    private DataFilter dataFilter;
    private CoordinatesConverter coordinatesConverter;
    private ChargingPointToDtoConverterBean chargingPointToDtoConverterBean;
    private AppPropertiesBean appPropertiesBean;

    public FindTheClosestController(ChargingPointDao chargingPointDao,
                                    DataFilter dataFilter,
                                    CoordinatesConverter coordinatesConverter,
                                    ChargingPointToDtoConverterBean chargingPointToDtoConverterBean,
                                    AppPropertiesBean appPropertiesBean) {
        this.chargingPointDao = chargingPointDao;
        this.dataFilter = dataFilter;
        this.coordinatesConverter = coordinatesConverter;
        this.chargingPointToDtoConverterBean = chargingPointToDtoConverterBean;
        this.appPropertiesBean = appPropertiesBean;
    }

    @RequestMapping(value = "/find-the-closest", method = RequestMethod.GET)
    public ModelAndView getFindTheClosestPage(HttpSession session) {

        LOG.info("User searched closest charging station");

        ModelAndView modelAndView = new ModelAndView("body-templates/find-the-closest");

        String directionLong = (String) session.getAttribute("directionLong");
        String degreesLong = (String) session.getAttribute("degreesLong");
        String minutesLong = (String) session.getAttribute("minutesLong");
        String secondsLong = (String) session.getAttribute("secondLong");
        String directionLati = (String) session.getAttribute("directionLati");
        String degreesLati = (String) session.getAttribute("degreesLati");
        String minutesLati = (String) session.getAttribute("minutesLati");
        String secondsLati = (String) session.getAttribute("secondLati");

        boolean isDegreesLongNull = (degreesLong == null || degreesLong.isEmpty());
        boolean isMinutesLongNull = (minutesLong == null || minutesLong.isEmpty());
        boolean isSecondsLongNull = (secondsLong == null || secondsLong.isEmpty());
        boolean isDegreesLatiNull = (degreesLati == null || degreesLati.isEmpty());
        boolean isMinutesLatiNull = (minutesLati == null || minutesLati.isEmpty());
        boolean isSecondsLatiNull = (secondsLati == null || secondsLati.isEmpty());

        if ((isDegreesLongNull && isMinutesLongNull && isSecondsLongNull)
                && (isDegreesLatiNull && isMinutesLatiNull && isSecondsLatiNull)) {
            modelAndView.addObject("current_unit", Formaters.naturalFormat(appPropertiesBean.getCurrentUnit().name()));
            modelAndView.addObject("title", "Find the closest charging point");
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
                    ChargingPoint chargingPoint = dataFilter
                            .findClosestChargingStation(chargingPointDao.findAll(), longitude,
                                    latitude);
                    chargingPointsList.add(chargingPoint);
                    List<ChargingPointDto> chargingPointsDtoList = chargingPointToDtoConverterBean.convertList(chargingPointsList);
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
                }
            } else {
                errorMessages(modelAndView);
            }
        }
        return modelAndView;
    }

    private void errorMessages(ModelAndView modelAndView) {
        modelAndView.addObject("title", "Find the closest charging point");
        modelAndView.addObject("error", "Please fill the form with correct value");
    }

    private boolean isStringInRange(String value, int min, int max) {
        Double coordinateDouble = Double.valueOf(value);
        return coordinateDouble >= min && coordinateDouble <= max;
    }
}
