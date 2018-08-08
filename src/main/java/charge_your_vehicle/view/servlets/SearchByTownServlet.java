package charge_your_vehicle.view.servlets;

import charge_your_vehicle.dao.ChargingPointDao;
import charge_your_vehicle.dao.TownStatisticsDao;
import charge_your_vehicle.dto.ChargingPointDto;
import charge_your_vehicle.model.User;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/search-by-town")
public class SearchByTownServlet extends HttpServlet {

    private ChargingPointDao chargingPointDao;
    private ChargingPointToDtoConverterBean chargingPointToDtoConverterBean;
    private TownStatisticsDao townStatisticsDao;
    private AppPropertiesBean appPropertiesBean;

    public SearchByTownServlet(ChargingPointDao chargingPointDao,
                               ChargingPointToDtoConverterBean chargingPointToDtoConverterBean,
                               TownStatisticsDao townStatisticsDao,
                               AppPropertiesBean appPropertiesBean) {
        this.chargingPointDao = chargingPointDao;
        this.chargingPointToDtoConverterBean = chargingPointToDtoConverterBean;
        this.townStatisticsDao = townStatisticsDao;
        this.appPropertiesBean = appPropertiesBean;
    }

    public static final Logger LOG = LoggerFactory.getLogger(SearchByTownServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        LOG.info("User searched charging station at town");

        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("title", "Search by town");

        Object userObject = req.getSession().getAttribute("user");
        User user;
        if (userObject != null) {
            user = (User) userObject;
            dataModel.put("userSessionName", user.getName());
            dataModel.put("userAdmin", user.getRoleAdministration());
        }

        String town = req.getParameter("town");
        if (town == null || town.isEmpty()) {
            dataModel.put("body_template", "search-by-town");
        } else {
            try {
                List<ChargingPointDto> chargingPointsDtoList = chargingPointToDtoConverterBean.convertList(chargingPointDao.findByTown(town));
                if (chargingPointsDtoList.size() > 0) {
                    townStatisticsDao.addToStatistics(town);
                    dataModel.put("body_template", "results");
                    dataModel.put("chargingPoints", chargingPointsDtoList);
                    dataModel.put("google_api_key", appPropertiesBean.getGoogleApiKey());
                } else {
                    errorMessages(dataModel);
                }
            } catch (Exception e) {
                errorMessages(dataModel);
                LOG.error("Exception was catched.");
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

    private void errorMessages(Map<String, Object> dataModel) {
        dataModel.put("body_template", "search-by-town");
        dataModel.put("error", "No charging points found");
    }
}
