package charge_your_vehicle.dao;

import charge_your_vehicle.model.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@Repository
public class UsersDao {

    private EntityManager entityManager;

    public UsersDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<User> findAll() {
        final Query query = entityManager.createQuery("SELECT u FROM User u");
        return query.getResultList();
    }

    public User update(User u) {
        return entityManager.merge(u);
    }

    public String save(User u) {
        entityManager.persist(u);
        return u.getUserId();
    }

    public void delete(Long id) {
        final User u = entityManager.find(User.class, id);
        if (u != null) {
            entityManager.remove(u);
        }
    }

    public User findById(String id) {
        return entityManager.find(User.class, id);
    }
}
