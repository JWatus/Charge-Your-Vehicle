package charge_your_vehicle.view.servlets;


import charge_your_vehicle.model.User;
import charge_your_vehicle.service.upload.ApiUploadProcessorBean;
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

@WebServlet("/administration/load-data")
public class LoadDataFromApiFormServlet extends HttpServlet {


    public static final Logger LOG = LoggerFactory.getLogger(LoadDataFromApiFormServlet.class);

    private ApiUploadProcessorBean apiUploadProcessorBean;

    public LoadDataFromApiFormServlet(ApiUploadProcessorBean apiUploadProcessorBean) {
        this.apiUploadProcessorBean = apiUploadProcessorBean;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, Object> dataModel = new HashMap<>();
        PrintWriter writer = resp.getWriter();
        resp.setContentType("text/html;charset=UTF-8");

        dataModel.put("body_template", "api-data-upload");
        dataModel.put("title", "Administration");

        Object userObject = req.getSession().getAttribute("user");
        User user;
        if (userObject != null) {
            user = (User) userObject;
            dataModel.put("userSessionName", user.getName());
            dataModel.put("userAdmin", user.getRoleAdministration());
        }

        String recordsAdded = req.getParameter("recordsAdded");
        if (recordsAdded != null && !recordsAdded.isEmpty()) {
            dataModel.put("recordsAdded", recordsAdded);
        }
    }
}
