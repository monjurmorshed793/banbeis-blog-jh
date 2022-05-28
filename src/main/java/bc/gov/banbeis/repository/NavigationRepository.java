package bc.gov.banbeis.repository;

import bc.gov.banbeis.domain.Navigation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Navigation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NavigationRepository extends MongoRepository<Navigation, String> {}
