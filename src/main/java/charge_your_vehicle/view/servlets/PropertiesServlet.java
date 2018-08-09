package charge_your_vehicle.view.servlets;

import charge_your_vehicle.model.User;
import charge_your_vehicle.service.properties.AppPropertiesBean;
import charge_your_vehicle.service.properties.Units;
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
import java.util.Map;

@WebServlet("/administration/properties")
public class PropertiesServlet extends HttpServlet {

    public static final Logger LOG = LoggerFactory.getLogger(PropertiesServlet.class);

    private AppPropertiesBean appPropertiesBean;

    public PropertiesServlet(AppPropertiesBean appPropertiesBean) {
        this.appPropertiesBean = appPropertiesBean;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        resp.setContentType("text/html;charset=UTF-8");

        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("title", "Properties");

        Object userObject = req.getSession().getAttribute("user");
        User user;
        if (userObject != null) {
            user = (User) userObject;
            dataModel.put("userSessionName", user.getName());
            dataModel.put("userAdmin", user.getRoleAdministration());
        }

        String unit = req.getParameter("unit");
        if (unit != null && !unit.isEmpty()) {
            appPropertiesBean.setUnits(Units.valueOf(unit.toUpperCase()));
        }

        dataModel.put("body_template", "properties");
        dataModel.put("units", Formaters.getNames(Units.values()));
        dataModel.put("current_unit", Formaters.naturalFormat(appPropertiesBean.getCurrentUnit().name()));
        dataModel.put("api_key", appPropertiesBean.getGoogleApiKey());

        Template template = TemplateProvider.createTemplate(getServletContext(), "templates/body-templates/home.html");

        try {
            template.process(dataModel, writer);
        } catch (TemplateException e) {
            LOG.error("Template problem occurred.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String unit = req.getParameter("unit");
        if (unit != null && !unit.isEmpty()) {
            appPropertiesBean.setUnits(Units.valueOf(unit.toUpperCase()));
        }

        String apiKey = req.getParameter("api_key");
        if (apiKey != null && !apiKey.isEmpty()) {
            appPropertiesBean.setGoogleApiKey(apiKey);
        }

        resp.sendRedirect("/administration/properties");
    }
}
