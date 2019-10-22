package top.techial.knowledge.domain;

import lombok.Data;
import lombok.experimental.Accessors;
import org.neo4j.ogm.annotation.*;

import java.util.Map;

/**
 * @author techial
 */
@Data
@RelationshipEntity(type = "RELATION")
@Accessors(chain = true)
public class KnowledgeNodeRelation {
    @Id
    private Long id;

    @StartNode
    private KnowledgeNode startNode;

    @EndNode
    private KnowledgeNode endNode;

    @Properties
    private Map<String, String> property;
}
