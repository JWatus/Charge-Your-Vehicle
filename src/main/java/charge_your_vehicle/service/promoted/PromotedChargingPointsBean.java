package charge_your_vehicle.service.promoted;

import charge_your_vehicle.dao.PromotedChargingPointRepository;
import charge_your_vehicle.model.ChargingPoint;
import charge_your_vehicle.model.PromotedChargingPoint;
import org.springframework.stereotype.Service;

import javax.inject.Named;
import java.util.Optional;

@Service
@Named
public class PromotedChargingPointsBean {

    private PromotedChargingPointRepository promotedChargingPointRepository;

    public PromotedChargingPointsBean(PromotedChargingPointRepository promotedChargingPointRepository) {
        this.promotedChargingPointRepository = promotedChargingPointRepository;
    }

    public PromotedChargingPointsBean() {
    }

    public boolean isPromoted(ChargingPoint chargingPoint) {
        return isPromoted(chargingPoint.getId());
    }

    public boolean isPromoted(int chargingPointId) {
        Optional<PromotedChargingPoint> promotedChargingPoint = promotedChargingPointRepository.findById(Long.valueOf(chargingPointId));
        return promotedChargingPoint.isPresent();
    }

    public void addToPromoted(ChargingPoint chargingPoint) {
        addToPromoted(chargingPoint.getId());
    }

    public void addToPromoted(int chargingPointId) {
        addToPromoted(new PromotedChargingPoint(chargingPointId));
    }

    private void addToPromoted(PromotedChargingPoint promotedChargingPoint) {
        promotedChargingPointRepository.save(promotedChargingPoint);
    }

    public void removeFromPromoted(ChargingPoint chargingPoint) {
        removeFromPromoted(chargingPoint.getId());
    }

    public void removeFromPromoted(int chargingPointId) {
        Optional<PromotedChargingPoint> promotedChargingPoint = promotedChargingPointRepository.findById(Long.valueOf(chargingPointId));
        promotedChargingPointRepository.delete(promotedChargingPoint.get());
    }
}
