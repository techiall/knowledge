package top.techial.knowledge.dao;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;
import top.techial.knowledge.domain.Weather;

import java.util.Optional;

/**
 * @author techial
 */
@Repository
public interface WeatherRepository extends Neo4jRepository<Weather, Long> {
    /**
     * @param title
     * @return
     */
    Optional<Weather> findByTitle(String title);
}
