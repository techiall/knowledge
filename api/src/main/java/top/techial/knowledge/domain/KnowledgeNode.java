package top.techial.knowledge.domain;

import lombok.Data;
import lombok.experimental.Accessors;
import org.neo4j.ogm.annotation.*;

import java.util.Map;
import java.util.Set;

/**
 * @author techial
 */
@Data
@NodeEntity
@Accessors(chain = true)
public class KnowledgeNode {
    @Id
    @GeneratedValue
    private Long id;

    @Labels
    private Set<String> labels;

    @Index(unique = true)
    private String name;

    @Properties(allowCast = true)
    private Map<String, String> property;
}
