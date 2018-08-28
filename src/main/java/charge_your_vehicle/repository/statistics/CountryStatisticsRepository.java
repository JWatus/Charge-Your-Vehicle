package charge_your_vehicle.repository.statistics;

import charge_your_vehicle.model.entity.statistics.CountryStatistics;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@Repository
@Transactional
public class CountryStatisticsRepository implements StatisticsRepository {

    private EntityManager entityManager;

    public CountryStatisticsRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void save(CountryStatistics country) {
        entityManager.persist(country);
    }

    public CountryStatistics findByName(String country) {
        return entityManager.find(CountryStatistics.class, country);
    }

    public List<CountryStatistics> findAllOrderByNumberOfVisitsDesc() {
        final Query query = entityManager.createQuery("SELECT cs FROM CountryStatistics cs ORDER BY numberOfVisits DESC");
        return query.getResultList();
    }

    @Override
    public void addToStatistics(String country) {
        CountryStatistics countryStatistics = findByName(country);
        if (countryStatistics != null) {
            Long currentNumberOfVisits = countryStatistics.getNumberOfVisits();
            countryStatistics.setNumberOfVisits(currentNumberOfVisits + 1);
        } else {
            save(new CountryStatistics(country, 1L));
        }
    }
}
