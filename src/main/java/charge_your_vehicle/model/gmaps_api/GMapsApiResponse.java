package charge_your_vehicle.model.gmaps_api;

import java.util.List;

public class GMapsApiResponse {

    private List<GMapsApiResult> results;
    private String status;

    public List<GMapsApiResult> getResults() {
        return results;
    }

    public void setResults(List<GMapsApiResult> results) {
        this.results = results;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
