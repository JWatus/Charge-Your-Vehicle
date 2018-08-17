package charge_your_vehicle.dao;

import charge_your_vehicle.model.PromotedChargingPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PromotedChargingPointRepository extends JpaRepository<PromotedChargingPoint, Long> {

}

