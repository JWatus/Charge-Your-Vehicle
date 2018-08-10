package charge_your_vehicle.service.upload;

import charge_your_vehicle.model.ChargingPoint;
import charge_your_vehicle.service.json_parser.CustomGsonBuilder;
import charge_your_vehicle.service.json_parser.JsonParser;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FileUploadProcessorBean extends UploadProcessor {

    public int uploadJsonFile() throws IOException {

        File initialFile = new File("src/main/resources/sample.json");
        InputStream fileContent = new FileInputStream(initialFile);
        String content = convertInputSteamToString(fileContent);

        List<ChargingPoint> chargingPointList = new JsonParser(new CustomGsonBuilder()).jsonToChargingPointList(content);

//        clearTables();
//        saveChargingPoints(chargingPointList);

        return chargingPointList.size();
    }

    private String convertInputSteamToString(InputStream inputStream) {
        return new BufferedReader(new InputStreamReader(inputStream))
                .lines().collect(Collectors.joining(""));
    }
}
