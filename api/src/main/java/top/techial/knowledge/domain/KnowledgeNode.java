package top.techial.knowledge.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.neo4j.ogm.annotation.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author techial
 */
@Getter
@Setter
@ToString
@NodeEntity
@Accessors(chain = true)
public class KnowledgeNode implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @Labels
    private Set<String> labels;

    @Index(unique = true)
    private String name;

    @Properties(allowCast = true)
    private Map<String, String> property;

    @Property
    private Boolean isParentNode = false;

    @Relationship(value = "parent-child-relation")
    private Set<KnowledgeNode> childNodes = new HashSet<>();


    @Override
    public int hashCode() {
        return Math.toIntExact(id);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof KnowledgeNode) {
            return this.id.equals(((KnowledgeNode) o).getId());
        }
        return false;
    }
}
