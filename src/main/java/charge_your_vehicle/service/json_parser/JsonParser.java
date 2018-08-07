package charge_your_vehicle.service.json_parser;

import charge_your_vehicle.model.ChargingPoint;
import java.util.List;

public class JsonParser {

    private CustomGsonBuilder gsonBuilder;

    public JsonParser(CustomGsonBuilder gsonBuilder) {
        this.gsonBuilder = gsonBuilder;
    }

    public List<ChargingPoint> jsonToChargingPointList(String json) {
        List<ChargingPoint> chargingPoints = gsonBuilder.deserialize(new ChargingPointDeserializer(), json);
        return chargingPoints;
    }
}