package service.data_filters;

import charge_your_vehicle.model.entity.charging_points_data.AddressInfo;
import charge_your_vehicle.model.entity.charging_points_data.ChargingPoint;
import charge_your_vehicle.service.data_filters.DataFilter;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


class DataFilterTest {

    @Test
    void findChargingStationAtTownCheck() {

        //given
        List<ChargingPoint> chargingPointList = new ArrayList<>();

        ChargingPoint pointOne = new ChargingPoint();
        AddressInfo addressInfoOne = new AddressInfo();
        addressInfoOne.setTown("Wroclaw");
        pointOne.setAddressInfo(addressInfoOne);
        chargingPointList.add(pointOne);

        ChargingPoint pointTwo = new ChargingPoint();
        AddressInfo addressInfoTwo = new AddressInfo();
        addressInfoTwo.setTown("Wroclaw");
        pointTwo.setAddressInfo(addressInfoTwo);
        chargingPointList.add(pointTwo);

        //when
        List<ChargingPoint> chargPoints = new DataFilter().findChargingStationAtTown(chargingPointList,
                "Wroclaw");

        //then
        assertEquals("Wroclaw", chargPoints.get(0).getAddressInfo().getTown(), "Town: ");
        assertEquals("Wroclaw", chargPoints.get(1).getAddressInfo().getTown(), "Town: ");
    }

    @Test
    void findClosestChargingStationCheck() {

        //given
        List<ChargingPoint> chargingPointList = new ArrayList<>();

        ChargingPoint pointOne = new ChargingPoint();
        AddressInfo addressInfoOne = new AddressInfo();
        addressInfoOne.setLatitude(15);
        addressInfoOne.setLongitude(30);
        addressInfoOne.setId(1111);
        pointOne.setAddressInfo(addressInfoOne);
        chargingPointList.add(pointOne);

        ChargingPoint pointTwo = new ChargingPoint();
        AddressInfo addressInfoTwo = new AddressInfo();
        addressInfoTwo.setLatitude(87);
        addressInfoTwo.setLongitude(82);
        addressInfoTwo.setId(2222);
        pointTwo.setAddressInfo(addressInfoTwo);
        chargingPointList.add(pointTwo);

        ChargingPoint pointThree = new ChargingPoint();
        AddressInfo addressInfoThree = new AddressInfo();
        addressInfoThree.setLatitude(18);
        addressInfoThree.setLongitude(47);
        addressInfoThree.setId(3333);
        pointThree.setAddressInfo(addressInfoThree);
        chargingPointList.add(pointThree);

        //when
        ChargingPoint returnedPoint = new DataFilter().findClosestChargingStation(chargingPointList,
                31, 16);

        //then
        assertEquals(1111, returnedPoint.getAddressInfo().getId());
    }

    @Test
    void findChargingStationAtAreaCheck() {

        //given
        List<ChargingPoint> chargingPointList = new ArrayList<>();

        ChargingPoint pointOne = new ChargingPoint();
        AddressInfo addressInfoOne = new AddressInfo();
        addressInfoOne.setLatitude(15);
        addressInfoOne.setLongitude(30);
        addressInfoOne.setId(1111);
        pointOne.setAddressInfo(addressInfoOne);
        chargingPointList.add(pointOne);

        ChargingPoint pointTwo = new ChargingPoint();
        AddressInfo addressInfoTwo = new AddressInfo();
        addressInfoTwo.setLatitude(87);
        addressInfoTwo.setLongitude(82);
        addressInfoTwo.setId(2222);
        pointTwo.setAddressInfo(addressInfoTwo);
        chargingPointList.add(pointTwo);

        ChargingPoint pointThree = new ChargingPoint();
        AddressInfo addressInfoThree = new AddressInfo();
        addressInfoThree.setLatitude(16);
        addressInfoThree.setLongitude(30);
        addressInfoThree.setId(3333);
        pointThree.setAddressInfo(addressInfoThree);
        chargingPointList.add(pointThree);

        ChargingPoint pointFour = new ChargingPoint();
        AddressInfo addressInfoFour = new AddressInfo();
        addressInfoFour.setLatitude(88);
        addressInfoFour.setLongitude(88);
        addressInfoFour.setId(4444);
        pointFour.setAddressInfo(addressInfoFour);
        chargingPointList.add(pointFour);

        //when
        List<ChargingPoint> returnedList = new DataFilter().findChargingStationAtArea(chargingPointList,
                30, 15, 200);

        //then
        assertEquals(2, returnedList.size());
        assertEquals(1111, returnedList.get(0).getAddressInfo().getId());
        assertEquals(3333, returnedList.get(1).getAddressInfo().getId());
    }
}