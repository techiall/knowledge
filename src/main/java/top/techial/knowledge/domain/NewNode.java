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
@NodeEntity(label = "NewNode")
@Accessors(chain = true)
@Data
public class NewNode {
    private Long id;

    @Index(unique = true)
    @Property(name = "title")
    private String title;

    @Relationship(type = "RELATION", direction = Relationship.UNDIRECTED)
    private Iterable<HuDongItem> huDongItems;
}
