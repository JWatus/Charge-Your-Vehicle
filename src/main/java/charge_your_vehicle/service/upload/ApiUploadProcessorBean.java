package charge_your_vehicle.service.upload;

import charge_your_vehicle.model.ChargingPoint;
import charge_your_vehicle.service.json_parser.CustomGsonBuilder;
import charge_your_vehicle.service.json_parser.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Service
public class ApiUploadProcessorBean extends UploadProcessor {

    private static final Logger log = LoggerFactory.getLogger(ApiUploadProcessorBean.class);

    public int uploadAllChargingPointsFromApi() {
        return uploadJsonApi(downloadJsonAsStringFromApi("maxresults=1000000"));
    }

    public int uploadAllChargingPointsInIndiaFromApi() {
        return uploadJsonApi(downloadJsonAsStringFromApi("countrycode=IN&maxresults=1000000"));
    }

    public int uploadAllChargingPointsInPolandFromApi() {
        return uploadJsonApi(downloadJsonAsStringFromApi("countrycode=PL&maxresults=1000000"));
    }

    private int uploadJsonApi(String Json) {

        List<ChargingPoint> chargingPointList = new JsonParser(new CustomGsonBuilder()).jsonToChargingPointList(Json);
        clearTables();
        log.info("Saving [{}] points", chargingPointList.size());
        saveChargingPoints(chargingPointList);
        return chargingPointList.size();
    }

    private String downloadJsonAsStringFromApi(String properties) {

        Client client = ClientBuilder.newClient();
        WebTarget target = client.target("https://api.openchargemap.io/v2/poi/?output=json&" + properties);
        Response response = target.request().accept(MediaType.APPLICATION_JSON).header("User-Agent", "curl/7.47.0").get();
        log.info("Response from HTTP Get openchargemap API: {}", response.getStatus());
        String data = response.readEntity(String.class);
        response.close();
        return data;
    }
}
