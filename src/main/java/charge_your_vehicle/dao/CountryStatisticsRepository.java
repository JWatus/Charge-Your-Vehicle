package charge_your_vehicle.dao;

import charge_your_vehicle.model.CountryStatistics;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@Repository
@Transactional
public class CountryStatisticsRepository {

    private EntityManager entityManager;

    public CountryStatisticsRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void save(CountryStatistics country) {
        entityManager.persist(country);
    }

    public CountryStatistics update(CountryStatistics c) {
        return entityManager.merge(c);
    }

    public CountryStatistics findByName(String country) {
        return entityManager.find(CountryStatistics.class, country);
    }

    public List<CountryStatistics> findAll() {
        final Query query = entityManager.createQuery("SELECT cs FROM CountryStatistics cs");
        return query.getResultList();
    }

    public List<CountryStatistics> findAllOrderByNumberOfVisitsDesc() {
        final Query query = entityManager.createQuery("SELECT cs FROM CountryStatistics cs ORDER BY numberOfVisits DESC");
        return query.getResultList();
    }

    public List<CountryStatistics> findMostChecked() {
        final Query query = entityManager.createQuery("SELECT cs FROM CountryStatistics c WHERE numberOfVisits=(SELECT max(numberOfVisits) FROM CountryStatistics)");
        return query.getResultList();
    }

    public void addToStatistics(String country) {
        final Query query = entityManager.createNativeQuery(
                "INSERT INTO COUNTRY_STATISTICS (name, number_of_visits) " +
                        "VALUES(:country, 1 ) " ); //+
                       // "ON DUPLICATE KEY UPDATE number_of_visits = number_of_visits +1");
        query.setParameter("country", country.toUpperCase());
        query.executeUpdate();
    }
}
