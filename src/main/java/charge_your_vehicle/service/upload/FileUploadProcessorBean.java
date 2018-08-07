package charge_your_vehicle.service.upload;

import charge_your_vehicle.exceptions.JsonFileNotFound;
import charge_your_vehicle.model.ChargingPoint;
import charge_your_vehicle.service.json_parser.CustomGsonBuilder;
import charge_your_vehicle.service.json_parser.JsonParser;
import org.springframework.stereotype.Service;

import javax.servlet.http.Part;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FileUploadProcessorBean extends UploadProcessor {

    public int uploadJsonFile(Part filePart) throws JsonFileNotFound, IOException {
        if (filePart == null) {
            throw new JsonFileNotFound("No json file has been uploaded #1");
        }
        String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();

        if (fileName == null || fileName.isEmpty()) {
            throw new JsonFileNotFound("No json file has been uploaded #2");
        }

        InputStream fileContent = filePart.getInputStream();
        String content = convertInputSteamToString(fileContent);

        List<ChargingPoint> chargingPointList = new JsonParser(new CustomGsonBuilder()).jsonToChargingPointList(content);

        clearTables();
        saveChargingPoints(chargingPointList);

        return chargingPointList.size();
    }

    private String convertInputSteamToString(InputStream inputStream) {
        String result = new BufferedReader(new InputStreamReader(inputStream))
                .lines().collect(Collectors.joining(""));
        return result;
    }

}
