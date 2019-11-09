package top.techial.knowledge.domain;

import lombok.Data;
import lombok.experimental.Accessors;
import org.neo4j.ogm.annotation.*;

import java.io.Serializable;

/**
 * @author techial
 */
@Data
@Accessors(chain = true)
@RelationshipEntity(value = "parent-child-relation")
public class ParentChildRelation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @StartNode
    private KnowledgeNode startNode;

    @EndNode
    private KnowledgeNode endNode;

}
