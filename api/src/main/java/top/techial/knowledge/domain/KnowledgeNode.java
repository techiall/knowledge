package top.techial.knowledge.domain;

import lombok.Data;
import lombok.experimental.Accessors;
import org.neo4j.ogm.annotation.*;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author techial
 */
@Data
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

    private boolean isParentNode = false;

    @Relationship(value = "parent-child-relation")
    private List<KnowledgeNode> childNodes;

}
