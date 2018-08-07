package service.properties;

import charge_your_vehicle.service.properties.AppPropertiesBean;
import charge_your_vehicle.service.properties.Units;
import org.junit.jupiter.api.Test;

import static junit.framework.TestCase.assertEquals;

class AppPropertiesBeanTest {

    AppPropertiesBean appPropertiesBean = new AppPropertiesBean();

    @Test
    public void shouldReturnSetValue() {

        //GIVEN
        Units expected = Units.MILES;
        appPropertiesBean.setUnits(expected);

        //WHEN
        Units result = appPropertiesBean.getCurrentUnit();

        //THEN
        assertEquals(expected, result);
    }
}