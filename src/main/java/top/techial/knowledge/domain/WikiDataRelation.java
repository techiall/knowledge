package top.techial.knowledge.domain;

import lombok.Data;
import lombok.experimental.Accessors;
import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

/**
 * @author techial
 */
@RelationshipEntity
@Data
@Accessors(chain = true)
public class WikiDataRelation {
    private Long id;

    @StartNode
    private HuDongItem startNode;

    @EndNode
    private HuDongItem endNode;

    private String relation;
}
