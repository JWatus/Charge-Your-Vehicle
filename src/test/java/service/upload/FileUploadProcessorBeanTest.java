package service.upload;


import charge_your_vehicle.exceptions.JsonFileNotFound;
import charge_your_vehicle.service.upload.FileUploadProcessorBean;
import org.junit.jupiter.api.Test;

import static junit.framework.TestCase.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FileUploadProcessorBeanTest {

    @Test
    void isJsonFileUploaded() {

        //when
        Throwable exception = assertThrows(JsonFileNotFound.class, () -> {
            new FileUploadProcessorBean().uploadJsonFile(null);
        });

        //then
        assertEquals("No json file has been uploaded #1", exception.getMessage());
    }
}