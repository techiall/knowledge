package top.techial.knowledge.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.neo4j.ogm.annotation.*;

import java.io.Serializable;
import java.util.Map;

/**
 * @author techial
 */
@Getter
@Setter
@ToString
@RelationshipEntity(type = "RELATION")
@Accessors(chain = true)
public class NodeRelation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @StartNode
    private KnowledgeNode startNode;

    @EndNode
    private KnowledgeNode endNode;

    @Properties
    private Map<String, String> property;

    @Override
    public int hashCode() {
        return Math.toIntExact(id);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof NodeRelation) {
            return this.id.equals(((NodeRelation) o).getId());
        }
        return false;
    }
}
