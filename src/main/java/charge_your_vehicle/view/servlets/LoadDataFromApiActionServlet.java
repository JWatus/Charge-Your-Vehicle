package charge_your_vehicle.view.servlets;


import charge_your_vehicle.service.upload.ApiUploadProcessorBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/administration/load-data-upload")
public class LoadDataFromApiActionServlet extends HttpServlet {

    public static final Logger LOG = LoggerFactory.getLogger(LoadDataFromApiActionServlet.class);

    private ApiUploadProcessorBean apiUploadProcessorBean;

    public LoadDataFromApiActionServlet(ApiUploadProcessorBean apiUploadProcessorBean) {
        this.apiUploadProcessorBean = apiUploadProcessorBean;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String option = req.getParameter("option");
        int recordsAdded = -1;
        if (option.equals("pl")) {
            try {
                recordsAdded = apiUploadProcessorBean.uploadAllChargingpointsInIndiaFromApi();
            } catch (Exception e) {
                LOG.error("Failed to update chargingpoints from api: {}", e);
            }
        } else if (option.equals("all")) {
            try {
                recordsAdded = apiUploadProcessorBean.uploadAllChargingpointsFromApi();
            } catch (Exception e) {
                LOG.error("Failed to update chargingpoints from api: {}", e);
            }
        }
        resp.sendRedirect("/administration/load-data?recordsAdded=" + recordsAdded);
    }
}
