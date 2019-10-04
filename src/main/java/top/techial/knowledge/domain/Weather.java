package top.techial.knowledge.domain;

import lombok.Data;
import lombok.experimental.Accessors;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

/**
 * @author techial
 */
@NodeEntity(label = "Weather")
@Data
@Accessors(chain = true)
public class Weather {
    private Long id;

    @Index(unique = true)
    @Property(name = "title")
    private String title;

    @Relationship(type = "CityWeather", direction = Relationship.UNDIRECTED)
    private Iterable<NewNode> newNodes;

    @Relationship(type = "CityWeather", direction = Relationship.UNDIRECTED)
    private Iterable<HuDongItem> huDongItems;
}
