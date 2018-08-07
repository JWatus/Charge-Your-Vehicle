package service.properties;

import charge_your_vehicle.service.properties.AppPropertiesBean;
import charge_your_vehicle.service.properties.Units;
import org.junit.jupiter.api.Test;

import static junit.framework.TestCase.assertEquals;

class AppPropertiesBeanTest {

    private AppPropertiesBean appPropertiesBean = new AppPropertiesBean();

    @Test
    void shouldReturnSetValue() {

        //given
        Units expected = Units.MILES;
        appPropertiesBean.setUnits(expected);

        //when
        Units result = appPropertiesBean.getCurrentUnit();

        //then
        assertEquals(expected, result);
    }
}