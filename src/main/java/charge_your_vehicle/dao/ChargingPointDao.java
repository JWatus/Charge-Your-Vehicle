package charge_your_vehicle.dao;

import charge_your_vehicle.model.ChargingPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@Repository
public interface ChargingPointDao extends JpaRepository<ChargingPoint, Long> {


    List<ChargingPoint> findByAddressInfo_Town(String town);
    List<ChargingPoint> findByAddressInfo_Country(String country);

//    private EntityManager entityManager;
//
//    public ChargingPointDao(EntityManager entityManager) {
//        this.entityManager = entityManager;
//    }
//
//    public List<ChargingPoint> findByTown(String town) {
//        final Query query = entityManager.createQuery("SELECT cp FROM ChargingPoint cp JOIN AddressInfo ai ON " +
//                "cp.addressInfo = ai.id WHERE ai.town = :town");
//        query.setParameter("town", town);
//        return query.getResultList();
//    }
//
//    public List<ChargingPoint> findByCountry(String country) {
//        final Query query = entityManager.createQuery("SELECT cp FROM ChargingPoint cp JOIN AddressInfo ai ON " +
//                "cp.addressInfo = ai JOIN Country c ON ai.country = c.id WHERE c.title = :title");
//        query.setParameter("title", country);
//        return query.getResultList();
//    }
//
//    public List<ChargingPoint> findAll() {
//        final Query query = entityManager.createQuery("SELECT cp FROM ChargingPoint cp");
//        return query.getResultList();
//    }
//
//    public ChargingPoint update(ChargingPoint cp) {
//        return entityManager.merge(cp);
//    }
//
//    public int save(ChargingPoint cp) {
//        entityManager.persist(cp);
//        return cp.getId();
//    }
//
//    public void delete(int id) {
//        final ChargingPoint cp = entityManager.find(ChargingPoint.class, id);
//        if (cp != null) {
//            entityManager.remove(cp);
//        }
//    }
//
//    public void deleteAll() {
//        final Query query = entityManager.createQuery("DELETE FROM ChargingPoint cp");
//        query.executeUpdate();
//    }
}