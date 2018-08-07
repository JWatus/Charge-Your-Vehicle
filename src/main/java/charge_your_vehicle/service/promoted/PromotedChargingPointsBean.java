package charge_your_vehicle.service.promoted;

import charge_your_vehicle.dao.PromotedChargingPointDao;
import charge_your_vehicle.model.ChargingPoint;
import charge_your_vehicle.model.PromotedChargingPoint;
import org.springframework.stereotype.Service;

import javax.inject.Named;

@Service
@Named
public class PromotedChargingPointsBean {

    private PromotedChargingPointDao promotedChargingPointDao;

    public PromotedChargingPointsBean(PromotedChargingPointDao promotedChargingPointDao) {
        this.promotedChargingPointDao = promotedChargingPointDao;
    }

    public PromotedChargingPointsBean() {
    }

    public boolean isPromoted(ChargingPoint chargingPoint) {
        return isPromoted(chargingPoint.getId());
    }

    public boolean isPromoted(int chargingPointId) {

        PromotedChargingPoint promotedChargingPoint = promotedChargingPointDao.findById(chargingPointId);

        return promotedChargingPoint != null;
    }

    public void addToPromoted(ChargingPoint chargingPoint) {
        addToPromoted(chargingPoint.getId());
    }

    public void addToPromoted(int chargingPointId) {
        addToPromoted(new PromotedChargingPoint(chargingPointId));
    }

    private void addToPromoted(PromotedChargingPoint promotedChargingPoint) {
        promotedChargingPointDao.save(promotedChargingPoint);
    }

    public void removeFromPromoted(ChargingPoint chargingPoint) {
        removeFromPromoted(chargingPoint.getId());
    }

    public void removeFromPromoted(int chargingPointId) {
        promotedChargingPointDao.delete(chargingPointId);
    }
}
