package charge_your_vehicle.dao;

import charge_your_vehicle.model.Connection;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@Repository
public class ConnectionDao {

    private EntityManager entityManager;

    public ConnectionDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<Connection> findAll() {
        final Query query = entityManager.createQuery("SELECT c FROM Connection c");
        return query.getResultList();
    }

    public Connection update(Connection ai) {
        return entityManager.merge(ai);
    }

    public int save(Connection c) {
        entityManager.persist(c);
        return c.getId();
    }

    public void delete(int id) {
        final Connection c = entityManager.find(Connection.class, id);
        if (c != null) {
            entityManager.remove(c);
        }
    }

    public void deleteAll() {
        final Query query = entityManager.createQuery("DELETE FROM Connection c");
        query.executeUpdate();
    }
}
