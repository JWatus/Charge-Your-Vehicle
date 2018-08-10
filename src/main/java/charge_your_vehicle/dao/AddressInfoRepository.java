package charge_your_vehicle.dao;

import charge_your_vehicle.model.AddressInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressInfoRepository extends JpaRepository<AddressInfo, Long> {

}
