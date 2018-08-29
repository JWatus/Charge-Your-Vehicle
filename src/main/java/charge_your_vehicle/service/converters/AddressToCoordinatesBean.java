package charge_your_vehicle.service.converters;

import charge_your_vehicle.model.gmaps_api.Coordinates;
import charge_your_vehicle.model.gmaps_api.GMapsApiLocation;
import charge_your_vehicle.model.gmaps_api.GMapsApiResponse;
import charge_your_vehicle.service.builders.UrlBuilder;
import charge_your_vehicle.service.properties.AppProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Service
public class AddressToCoordinatesBean {
    private static final String API_URL = "https://maps.googleapis.com/maps/api/geocode/json";

    public Coordinates getCoordinates(String address) {
        try {
            String escapedAddress = HtmlUtils.htmlEscape(address);
            escapedAddress = escapedAddress.replaceAll(" ", "+");

            Client client = ClientBuilder.newClient();
            UrlBuilder builder = new UrlBuilder(API_URL);
            builder.addParameterToUrl("address", escapedAddress);
            builder.addParameterToUrl("key", AppProperties.getInstance().getGoogleApiKey());
            WebTarget webTarget = client.target(builder.toString());
            Response response = webTarget.request().accept(MediaType.APPLICATION_JSON).get();
            GMapsApiResponse data = response.readEntity(GMapsApiResponse.class);
            response.close();

            GMapsApiLocation location = data.getResults().get(0).getGeometry().getLocation();
            return new Coordinates(location.getLat(), location.getLng());
        } catch (Exception e) {
            return null;
        }
    }
}
