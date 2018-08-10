package charge_your_vehicle.view.servlets;

import charge_your_vehicle.dao.CountryStatisticsDao;
import charge_your_vehicle.dao.TownStatisticsDao;
import charge_your_vehicle.model.User;
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

@WebServlet("/statistics")
public class StatisticsServlet extends HttpServlet {

    private CountryStatisticsDao countryStatisticsDao;
    private TownStatisticsDao townStatisticsDao;

    public StatisticsServlet(CountryStatisticsDao countryStatisticsDao, TownStatisticsDao townStatisticsDao) {
        this.countryStatisticsDao = countryStatisticsDao;
        this.townStatisticsDao = townStatisticsDao;
    }

    public static final Logger LOG = LoggerFactory.getLogger(StatisticsServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String userSessionName = (String) req.getSession().getAttribute("user_name");
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("userSessionName", userSessionName);
        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter writer = resp.getWriter();
        dataModel.put("body_template", "statistics");
        dataModel.put("title", "Statistics");

        Object userObject = req.getSession().getAttribute("user");
        User user;
        if (userObject != null) {
            user = (User) userObject;
            dataModel.put("userSessionName", user.getName());
            dataModel.put("userAdmin", user.getRoleAdministration());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userSessionName = (String) req.getSession().getAttribute("user_name");
        String location = req.getParameter("location");
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("userSessionName", userSessionName);
        dataModel.put("title", "Statistics");
        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter writer = resp.getWriter();
        if (location.equals("town")) {
            List locationList = townStatisticsDao.findAllOrderByNumberOfVisitsDesc();
            if (locationList.size() == 0) {
                dataModel.put("body_template", "statistics");
                dataModel.put("error", "Noone was searching charging points by town name");
            } else {
                dataModel.put("body_template", "show-statistics");
                dataModel.put("locationList", locationList);
            }
        } else if (location.equals("country")) {
            List locationList = countryStatisticsDao.findAllOrderByNumberOfVisitsDesc();
            if (locationList.size() == 0) {
                dataModel.put("body_template", "statistics");
                dataModel.put("error", "Noone was searching charging points by country name");
            } else {
                dataModel.put("body_template", "show-statistics");
                dataModel.put("locationList", locationList);
            }
        }
    }
}