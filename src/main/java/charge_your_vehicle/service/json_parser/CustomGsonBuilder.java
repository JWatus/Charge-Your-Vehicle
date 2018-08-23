package charge_your_vehicle.service.json_parser;

import charge_your_vehicle.model.entity.charging_points_data.ChargingPoint;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

public class CustomGsonBuilder {

    public List<ChargingPoint> deserialize(JsonDeserializer jsonDeserializer, String json) {
        Type listType = new TypeToken<LinkedList<ChargingPoint>>() {
        }.getType();
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(listType, jsonDeserializer);
        Gson gson = gsonBuilder.create();

        List<ChargingPoint> chargingPoints = gson.fromJson(json, listType);
        return chargingPoints;
    }
}
