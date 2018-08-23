package charge_your_vehicle.service.upload;

import charge_your_vehicle.repository.*;
import charge_your_vehicle.model.entity.charging_points_data.AddressInfo;
import charge_your_vehicle.model.entity.charging_points_data.ChargingPoint;
import charge_your_vehicle.model.entity.charging_points_data.Country;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
class UploadProcessor {

    @Autowired
    private ChargingPointRepository chargingPointRepository;
    @Autowired
    private CountryRepository countryRepository;
    @Autowired
    private AddressInfoRepository addressInfoRepository;

    void saveChargingPoints(List<ChargingPoint> chargingPointList) {

        Set<Country> countries = new HashSet<>();
        chargingPointList.forEach(c -> countries.add(c.getAddressInfo().getCountry()));
        countries.forEach(c -> countryRepository.save(c));

        Set<AddressInfo> addressInfos = new HashSet<>();
        chargingPointList.forEach(c -> {
            if (!addressInfos.stream().anyMatch(a -> a.getId() == c.getAddressInfo().getId())) {
                addressInfos.add(c.getAddressInfo());
            }
        });
        addressInfos.forEach(c -> addressInfoRepository.save(c));

        Set<ChargingPoint> chargingPoints = new HashSet<>();
        chargingPointList.forEach(c -> {
            if (!chargingPoints.stream().anyMatch(q -> q.getId() == c.getId())) {
                chargingPoints.add(c);
            }
        });
        chargingPoints.forEach(c -> {
            chargingPointRepository.save(c);
        });
    }

    void clearTables() {
        chargingPointRepository.deleteAll();
        addressInfoRepository.deleteAll();
        countryRepository.deleteAll();
    }
}
