package charge_your_vehicle.service.data_filters;

import charge_your_vehicle.model.entity.charging_points_data.ChargingPoint;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class DataFilter {

    public List<ChargingPoint> findChargingStationAtTown(List<ChargingPoint> points, String town) {
        return points.stream()
                .filter(p -> p.getAddressInfo().getTown().toUpperCase().equals(town.toUpperCase()))
                .collect(toList());
    }

    public ChargingPoint findClosestChargingStation(List<ChargingPoint> points, double longitude, double latitude) {
        ChargingPoint chargingPoint = null;
        double distance;
        double closest = Double.MAX_VALUE;
        for (ChargingPoint p : points) {
            distance = DistanceCalculator.distanceBetweenTwoPoints(p.getAddressInfo().getLatitude(), latitude,
                    p.getAddressInfo().getLongitude(), longitude);
            if (distance < closest) {
                closest = distance;
                chargingPoint = p;
            }
        }
        return chargingPoint;
    }

    public List<ChargingPoint> findChargingStationAtArea(List<ChargingPoint> points, double longitude, double latitude, double radius) {
        return points.stream()
                .filter(p -> DistanceCalculator.distanceBetweenTwoPoints(p.getAddressInfo().getLatitude(), latitude,
                        p.getAddressInfo().getLongitude(), longitude) < radius)
                .collect(toList());
    }
}

