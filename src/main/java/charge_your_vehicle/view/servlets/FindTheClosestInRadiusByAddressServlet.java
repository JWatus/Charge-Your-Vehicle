package charge_your_vehicle.view.servlets;

import charge_your_vehicle.dao.ChargingPointDao;
import charge_your_vehicle.dto.ChargingPointDto;
import charge_your_vehicle.model.ChargingPoint;
import charge_your_vehicle.model.Coordinates;
import charge_your_vehicle.model.User;
import charge_your_vehicle.service.converters.AddressToCoordinatesBean;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/find-the-closest-in-radius-by-address")
public class FindTheClosestInRadiusByAddressServlet extends HttpServlet {

    private ChargingPointDao chargingPointDao;
    private DataFilter dataFilter;
    private CoordinatesConverter coordinatesConverter;
    private AppPropertiesBean appPropertiesBean;
    private ChargingPointToDtoConverterBean chargingPointToDtoConverterBean;
    private AddressToCoordinatesBean addressToCoordinatesBean;

    public FindTheClosestInRadiusByAddressServlet(ChargingPointDao chargingPointDao,
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

    public static final Logger LOG = LoggerFactory.getLogger(FindTheClosestInRadiusByAddressServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        LOG.info("User searched charging station at the area");

        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("title", "Find all charging points in radius");

        Object userObject = req.getSession().getAttribute("user");
        User user;
        if (userObject != null) {
            user = (User) userObject;
            dataModel.put("userSessionName", user.getName());
            dataModel.put("userAdmin", user.getRoleAdministration());
        }

        String radiusString = req.getParameter("radius");
        String address = req.getParameter("address");

        boolean isRadiusStringNull = (radiusString == null || radiusString.isEmpty());
        boolean isRadiusCorrect = false;

        if (!isRadiusStringNull) {
            isRadiusCorrect = (radiusString.length() < 10);
        }
        if (address == null || address.isEmpty()) {
            dataModel.put("body_template", "find-the-closest-in-radius-by-address");
            dataModel.put("current_unit", Formaters.naturalFormat(appPropertiesBean.getCurrentUnit().name()));
        } else if (!isRadiusCorrect) {
            dataModel.put("body_template", "find-the-closest-in-radius-by-address");
            dataModel.put("current_unit", Formaters.naturalFormat(appPropertiesBean.getCurrentUnit().name()));
            dataModel.put("error", "Wrong radius. Value should be less then 999 999 999");
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
                dataModel.put("points-map", "results");
                dataModel.put("body_template", "results");
                dataModel.put("chargingPoints", chargingPointsDtoList);
                dataModel.put("google_api_key", appPropertiesBean.getGoogleApiKey());
            } else {
                resp.sendError(500, "Wrong Google Api Key");
            }
        }

        PrintWriter writer = resp.getWriter();
        resp.setContentType("text/html;charset=UTF-8");

        Template template = TemplateProvider.createTemplate(getServletContext(), "templates/layout.html");

        try {
            template.process(dataModel, writer);
        } catch (TemplateException e) {
            LOG.error("Template problem occurred.");
        }
    }
}
