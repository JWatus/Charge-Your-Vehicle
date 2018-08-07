package service.data_filters;

import charge_your_vehicle.service.data_filters.DistanceCalculator;
import charge_your_vehicle.service.properties.AppProperties;
import charge_your_vehicle.service.properties.Units;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DistanceCalculatorTest {

    @Test
    void distanceBetweenTwoPointsOne() {

        //given
        AppProperties.getInstance().setUnits(Units.KILOMETERS);

        //when
        double testDistance = Math.round(DistanceCalculator.distanceBetweenTwoPoints(14, 56, 35, 11));

        //then
        assertEquals(5101, testDistance);
    }

    @Test
    void distanceBetweenTwoPointsTwo() {

        //given
        AppProperties.getInstance().setUnits(Units.KILOMETERS);

        //when
        double testDistance = Math.round(DistanceCalculator.distanceBetweenTwoPoints(89, 167, 67, 189));

        //then
        assertEquals(8503, testDistance);
    }

    @Test
    void distanceBetweenTwoPointsThree() {

        //given
        AppProperties.getInstance().setUnits(Units.KILOMETERS);

        //when
        double testDistance = Math.round(DistanceCalculator.distanceBetweenTwoPoints(11, 14, 18, 22));

        //then
        assertEquals(548, testDistance);
    }

    @Test
    void distanceBetweenTwoPointsFour() {

        //given
        AppProperties.getInstance().setUnits(Units.KILOMETERS);

        //when
        double testDistance = Math.round(DistanceCalculator.distanceBetweenTwoPoints(114, 156, 135, 111));

        //then
        assertEquals(4968, testDistance);
    }
}