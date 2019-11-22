package top.techial.knowledge.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.neo4j.ogm.annotation.*;

import java.io.Serializable;

/**
 * @author techial
 */
@Getter
@Setter
@ToString
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

    @Override
    public int hashCode() {
        return Math.toIntExact(id);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ParentChildRelation) {
            return this.id.equals(((ParentChildRelation) o).getId());
        }
        return false;
    }
}
