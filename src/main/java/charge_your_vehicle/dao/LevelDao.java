package charge_your_vehicle.dao;

import charge_your_vehicle.model.Level;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LevelDao extends JpaRepository<Level, Long> {

//    private EntityManager entityManager;
//
//    public LevelDao(EntityManager entityManager) {
//        this.entityManager = entityManager;
//    }
//
//    public List<Level> findAll() {
//        final Query query = entityManager.createQuery("SELECT l FROM Level l");
//        return query.getResultList();
//    }
//
//    public Level update(Level l) {
//        return entityManager.merge(l);
//    }
//
//    public int save(Level l) {
//        entityManager.persist(l);
//        return l.getId();
//    }
//
//    public void delete(int id) {
//        final Level l = entityManager.find(Level.class, id);
//        if (l != null) {
//            entityManager.remove(l);
//        }
//    }
//
//    public void deleteAll() {
//        final Query query = entityManager.createQuery("DELETE FROM Level l");
//        query.executeUpdate();
//    }
}