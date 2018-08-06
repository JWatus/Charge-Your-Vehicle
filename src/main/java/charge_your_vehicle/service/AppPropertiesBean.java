package charge_your_vehicle.service;

import org.springframework.stereotype.Service;

@Service
public class AppPropertiesBean {
    public Units getCurrentUnit() {
        return AppProperties.getInstance().getUnits();
    }

    public void setUnits(Units units) {
        AppProperties.getInstance().setUnits(units);
        AppProperties.getInstance().save();
    }

    public String getGoogleApiKey() {
        return AppProperties.getInstance().getGoogleApiKey();
    }

    public void setGoogleApiKey(String key) {
        AppProperties.getInstance().setGoogleApiKeyp(key);
        AppProperties.getInstance().save();
    }
}
