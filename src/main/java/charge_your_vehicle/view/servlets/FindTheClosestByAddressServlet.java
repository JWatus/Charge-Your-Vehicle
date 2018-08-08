package charge_your_vehicle.view.servlets;

import charge_your_vehicle.dao.ChargingPointDao;
import charge_your_vehicle.dto.ChargingPointDto;
import charge_your_vehicle.model.ChargingPoint;
import charge_your_vehicle.model.Coordinates;
import charge_your_vehicle.model.User;
import charge_your_vehicle.service.converters.AddressToCoordinatesBean;
import charge_your_vehicle.service.data_filters.DataFilter;
import charge_your_vehicle.service.promoted.ChargingPointToDtoConverterBean;
import charge_your_vehicle.service.properties.AppPropertiesBean;
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

@WebServlet("/find-the-closest-by-address")
public class FindTheClosestByAddressServlet extends HttpServlet {

    private ChargingPointDao chargingPointDao;
    private DataFilter dataFilter;
    private ChargingPointToDtoConverterBean chargingPointToDtoConverterBean;
    private AddressToCoordinatesBean addressToCoordinatesBean;
    private AppPropertiesBean appPropertiesBean;

    public FindTheClosestByAddressServlet(ChargingPointDao chargingPointDao,
                                          DataFilter dataFilter,
                                          ChargingPointToDtoConverterBean chargingPointToDtoConverterBean,
                                          AddressToCoordinatesBean addressToCoordinatesBean,
                                          AppPropertiesBean appPropertiesBean) {
        this.chargingPointDao = chargingPointDao;
        this.dataFilter = dataFilter;
        this.chargingPointToDtoConverterBean = chargingPointToDtoConverterBean;
        this.addressToCoordinatesBean = addressToCoordinatesBean;
        this.appPropertiesBean = appPropertiesBean;
    }

    public static final Logger LOG = LoggerFactory.getLogger(FindTheClosestByAddressServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        LOG.info("User searched closest charging station by address");

        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("title", "Find the closest charging point by address");

        Object userObject = req.getSession().getAttribute("user");
        User user;
        if (userObject != null) {
            user = (User) userObject;
            dataModel.put("userSessionName", user.getName());
            dataModel.put("userAdmin", user.getRoleAdministration());
        }

        String address = req.getParameter("address");


        if (address == null || address.isEmpty()) {
            dataModel.put("body_template", "find-the-closest-by-address");

        } else {
            Coordinates coordinates = addressToCoordinatesBean.getCoordinates(address);
            if (coordinates != null) {
                double longitude = coordinates.getLongitude();
                double latitude = coordinates.getLatitude();
                List<ChargingPoint> chargingPointsList = new ArrayList<>();
                ChargingPoint chargingPoint = dataFilter
                        .findClosestChargingStation(chargingPointDao.findAll(), longitude,
                                latitude);
                chargingPointsList.add(chargingPoint);
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
