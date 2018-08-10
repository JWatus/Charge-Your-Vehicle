package charge_your_vehicle.dao;

import charge_your_vehicle.model.ChargingPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChargingPointRepository extends JpaRepository<ChargingPoint, Long> {

    List<ChargingPoint> findByAddressInfo_Town(String town);

    List<ChargingPoint> findByAddressInfo_Country(String country);

}