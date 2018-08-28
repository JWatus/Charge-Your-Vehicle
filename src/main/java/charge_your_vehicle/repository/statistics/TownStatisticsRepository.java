package charge_your_vehicle.repository.statistics;

import charge_your_vehicle.model.entity.statistics.TownStatistics;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@Repository
@Transactional
public class TownStatisticsRepository implements StatisticsRepository {

    private EntityManager entityManager;

    public TownStatisticsRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void save(TownStatistics town) {
        entityManager.persist(town);
    }

    public TownStatistics findByName(String city) {
        return entityManager.find(TownStatistics.class, city);
    }

    public List<TownStatistics> findAllOrderByNumberOfVisitsDesc() {
        final Query query = entityManager.createQuery("SELECT ts FROM TownStatistics ts ORDER BY numberOfVisits DESC");
        return query.getResultList();
    }

    @Override
    public void addToStatistics(String town) {
        TownStatistics townStatistics = findByName(town);
        if (townStatistics != null) {
            Long currentNumberOfVisits = townStatistics.getNumberOfVisits();
            townStatistics.setNumberOfVisits(currentNumberOfVisits + 1);
        } else {
            save(new TownStatistics(town, 1L));
        }
    }
}
