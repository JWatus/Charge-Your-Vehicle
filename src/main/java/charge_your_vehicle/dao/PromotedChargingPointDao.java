package charge_your_vehicle.dao;

import charge_your_vehicle.model.PromotedChargingPoint;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@Repository
public class PromotedChargingPointDao {

    private EntityManager entityManager;

    public PromotedChargingPointDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public int save(PromotedChargingPoint pcp) {
        entityManager.persist(pcp);
        return pcp.getChargingPointId();
    }

    public void delete(int chargingPointId) {
        final PromotedChargingPoint pcp = entityManager.find(PromotedChargingPoint.class, chargingPointId);
        if (pcp != null) {
            entityManager.remove(pcp);
        }
    }

    public PromotedChargingPoint findById(Integer id) {
        return entityManager.find(PromotedChargingPoint.class, id);
    }

    public List<PromotedChargingPoint> findAll() {
        final Query query = entityManager.createQuery("SELECT p FROM PromotedChargingPoint p");
        return query.getResultList();
    }
}

