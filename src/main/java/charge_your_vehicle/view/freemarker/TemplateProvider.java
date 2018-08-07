package charge_your_vehicle.view.freemarker;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;

import javax.servlet.ServletContext;
import java.io.IOException;

public class TemplateProvider {

    private static final String TEMPLATES_DIRECTORY_PATH = "fm-templates/";

    public static Template createTemplate(ServletContext servletContext, String templateName) throws IOException {
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_27);

        configuration.setServletContextForTemplateLoading(servletContext, TEMPLATES_DIRECTORY_PATH);
        configuration.setDefaultEncoding("UTF-8");
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        configuration.setLogTemplateExceptions(false);
        configuration.setWrapUncheckedExceptions(true);

        return configuration.getTemplate(templateName);
    }
}
