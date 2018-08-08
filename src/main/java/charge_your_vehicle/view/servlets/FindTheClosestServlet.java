package charge_your_vehicle.view.servlets;

import charge_your_vehicle.dao.ChargingPointDao;
import charge_your_vehicle.dto.ChargingPointDto;
import charge_your_vehicle.model.ChargingPoint;
import charge_your_vehicle.model.User;
import charge_your_vehicle.service.converters.CoordinatesConverter;
import charge_your_vehicle.service.data_filters.DataFilter;
import charge_your_vehicle.service.promoted.ChargingPointToDtoConverterBean;
import charge_your_vehicle.service.properties.AppPropertiesBean;
import charge_your_vehicle.view.commons.Formaters;
import charge_your_vehicle.view.freemarker.TemplateProvider;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/find-the-closest")
public class FindTheClosestServlet extends HttpServlet {
    public static final Logger LOG = LoggerFactory.getLogger(FindTheClosestInRadiusServlet.class);

    private ChargingPointDao chargingPointDao;
    private DataFilter dataFilter;
    private CoordinatesConverter coordinatesConverter;
    private ChargingPointToDtoConverterBean chargingPointToDtoConverterBean;
    private AppPropertiesBean appPropertiesBean;

    public FindTheClosestServlet(ChargingPointDao chargingPointDao,
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

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        LOG.info("User searched closest charging station");

        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("title", "Find the closest charging point");

        Object userObject = req.getSession().getAttribute("user");
        User user;
        if (userObject != null) {
            user = (User) userObject;
            dataModel.put("userSessionName", user.getName());
            dataModel.put("userAdmin", user.getRoleAdministration());
        }

        PrintWriter writer = resp.getWriter();
        resp.setContentType("text/html;charset=UTF-8");
        String directionLong = req.getParameter("directionLong");
        String degreesLong = req.getParameter("degreesLong");
        String minutesLong = req.getParameter("minutesLong");
        String secondsLong = req.getParameter("secondLong");
        String directionLati = req.getParameter("directionLati");
        String degreesLati = req.getParameter("degreesLati");
        String minutesLati = req.getParameter("minutesLati");
        String secondsLati = req.getParameter("secondLati");

        boolean isDegreesLongNull = (degreesLong == null || degreesLong.isEmpty());
        boolean isMinutesLongNull = (minutesLong == null || minutesLong.isEmpty());
        boolean isSecondsLongNull = (secondsLong == null || secondsLong.isEmpty());
        boolean isDegreesLatiNull = (degreesLati == null || degreesLati.isEmpty());
        boolean isMinutesLatiNull = (minutesLati == null || minutesLati.isEmpty());
        boolean isSecondsLatiNull = (secondsLati == null || secondsLati.isEmpty());

        if ((isDegreesLongNull && isMinutesLongNull && isSecondsLongNull)
                && (isDegreesLatiNull && isMinutesLatiNull && isSecondsLatiNull)) {
            dataModel.put("body_template", "find-the-closest");
            dataModel.put("current_unit", Formaters.naturalFormat(appPropertiesBean.getCurrentUnit().name()));
            dataModel.put("title", "Find the closest charging point");
        } else if ((isDegreesLongNull && isMinutesLongNull && isSecondsLongNull)
                || (isDegreesLatiNull && isMinutesLatiNull && isSecondsLatiNull)) {
            errorMessages(dataModel);
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
                    dataModel.put("body_template", "results");
                    dataModel.put("chargingPoints", chargingPointsDtoList);
                    dataModel.put("latitude", latitude);
                    dataModel.put("longitude", longitude);
                    dataModel.put("google_api_key", appPropertiesBean.getGoogleApiKey());
                } catch (Exception e) {
                    dataModel.put("body_template", "find-the-closest");
                    dataModel.put("title", "Find the closest charging point");
                    dataModel.put("error", "No charging points were found");
                    LOG.error("No charging points were found");
                }
            } else {
                errorMessages(dataModel);
            }
        }
        resp.setContentType("text/html;charset=UTF-8");

        Template template = TemplateProvider.createTemplate(getServletContext(), "templates/layout.html");

        try {
            template.process(dataModel, writer);
        } catch (TemplateException e) {
            LOG.error("Template problem occurred.");
        }
    }

    private void errorMessages(Map<String, Object> dataModel) {
        dataModel.put("body_template", "find-the-closest");
        dataModel.put("title", "Find the closest charging point");
        dataModel.put("error", "Please fill the form with correct value");
    }

    private boolean isStringInRange(String value, int min, int max) {
        Double coordinateDouble = Double.valueOf(value);
        return coordinateDouble >= min && coordinateDouble <= max;
    }
}
