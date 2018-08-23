package charge_your_vehicle.controller;

import charge_your_vehicle.service.upload.FileUploadProcessorBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

@Controller
public class JsonFileUploadController {

    public static final Logger LOG = LoggerFactory.getLogger(JsonFileUploadController.class);

    private FileUploadProcessorBean fileUploadProcessor;

    public JsonFileUploadController(FileUploadProcessorBean fileUploadProcessor) {
        this.fileUploadProcessor = fileUploadProcessor;
    }

    @RequestMapping(value = "/administration/json-upload", method = RequestMethod.GET)
    public ModelAndView uploadJSONFile() throws IOException {
        ModelAndView modelAndView = new ModelAndView("body-templates/json-upload");
        modelAndView.addObject("title", "Administration");
        int recordsAdded = fileUploadProcessor.uploadJsonFile();
        if (recordsAdded != 0) {
            modelAndView.addObject("recordsAdded", recordsAdded);
        }
        return modelAndView;
    }
}
