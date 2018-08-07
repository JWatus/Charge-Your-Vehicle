package service.json_parser;

import charge_your_vehicle.model.ChargingPoint;
import charge_your_vehicle.service.json_parser.CustomGsonBuilder;
import charge_your_vehicle.service.json_parser.JsonParser;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

class JsonParserTest {

    @InjectMocks
    public JsonParser testee;

    @Mock
    private CustomGsonBuilder customGsonBuilder;

    @Test
    void jsonToChargingPointListDeserialize() {

        MockitoAnnotations.initMocks(this);

        List<ChargingPoint> list = new ArrayList<>();
        ChargingPoint point = new ChargingPoint();
        list.add(point);

        Mockito.when(customGsonBuilder.deserialize(any(), any())).thenReturn(list);

        testee.jsonToChargingPointList("json");
    }

    @Test
    void jsonToChargingPointListOne() throws IOException {

        //given
        String json = "{\n" +
                "  \"id\": 99562,\n" +
                "  \"uuid\": \"75E21538-5A47-4A8E-995A-7D07B02EEE5F\"\n" +
                "}";

        //when
        ChargingPoint point = new Gson().fromJson(json, ChargingPoint.class);

        //then
        assertAll("point",
                () -> assertEquals(99562, point.getId(), "ID: "),
                () -> assertEquals("75E21538-5A47-4A8E-995A-7D07B02EEE5F", point.getUuid(), "UUID: ")
        );
    }

    @Test
    void jsonToChargingPointListTwo() throws IOException {

        //given
        String json = "{   \"id\": 99559,\n" +
                "  \"uuid\": \"99D22B9B-B787-4105-8236-D3871F99F9F3\" }";

        //when
        ChargingPoint point = new Gson().fromJson(json, ChargingPoint.class);

        //then
        assertAll("point",
                () -> assertEquals(99559, point.getId(), "ID: "),
                () -> assertEquals("99D22B9B-B787-4105-8236-D3871F99F9F3", point.getUuid(), "UUID: ")
        );
    }
}