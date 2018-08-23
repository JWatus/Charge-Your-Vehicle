package charge_your_vehicle.repository;

import charge_your_vehicle.model.entity.charging_points_data.AddressInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressInfoRepository extends JpaRepository<AddressInfo, Long> {

}
