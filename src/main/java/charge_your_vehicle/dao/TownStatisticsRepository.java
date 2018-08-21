package charge_your_vehicle.dao;

import charge_your_vehicle.model.TownStatistics;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@Repository
@Transactional
public class TownStatisticsRepository {

    private EntityManager entityManager;

    public TownStatisticsRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void save(TownStatistics city) {
        entityManager.persist(city);
    }

    public TownStatistics update(TownStatistics c) {
        return entityManager.merge(c);
    }

    public TownStatistics findByName(String city) {
        return entityManager.find(TownStatistics.class, city);
    }

    public List<TownStatistics> findAll() {
        final Query query = entityManager.createQuery("SELECT ts FROM TownStatistics ts");
        return query.getResultList();
    }

    public List<TownStatistics> findAllOrderByNumberOfVisitsDesc() {
        final Query query = entityManager.createQuery("SELECT ts FROM TownStatistics ts ORDER BY numberOfVisits DESC");
        return query.getResultList();
    }

    public List<TownStatistics> findMostChecked() {
        final Query query = entityManager.createQuery("SELECT ts FROM TownStatistics ts WHERE numberOfVisits=(SELECT max(numberOfVisits) FROM TownStatistics)");
        return query.getResultList();
    }

    public void addToStatistics(String town) {
        final Query query = entityManager.createNativeQuery(
                "INSERT INTO TOWN_STATISTICS (name, number_of_visits) " +
                        "VALUES(:town, 1 ) " ); // +
                   //    "ON DUPLICATE KEY UPDATE number_of_visits = number_of_visits + 1");
        query.setParameter("town", town.toUpperCase());
        query.executeUpdate();
    }
}
