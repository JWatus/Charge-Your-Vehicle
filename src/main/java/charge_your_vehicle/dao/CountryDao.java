package charge_your_vehicle.dao;

import charge_your_vehicle.model.Country;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@Repository
public class CountryDao {

    private EntityManager entityManager;

    public CountryDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<Country> findAll() {
        final Query query = entityManager.createQuery("SELECT c FROM Country c");
        return query.getResultList();
    }

    public Country update(Country c) {
        return entityManager.merge(c);
    }

    public int save(Country c) {
        entityManager.persist(c);
        return c.getId();
    }

    public void delete(int id) {
        final Country c = entityManager.find(Country.class, id);
        if (c != null) {
            entityManager.remove(c);
        }
    }

    public void deleteAll() {
        final Query query = entityManager.createQuery("DELETE FROM Country c");
        query.executeUpdate();
    }
}
