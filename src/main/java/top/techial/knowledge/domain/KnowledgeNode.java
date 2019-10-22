package top.techial.knowledge.domain;

import lombok.Data;
import lombok.experimental.Accessors;
import org.neo4j.ogm.annotation.*;

import java.util.Collection;
import java.util.Map;

/**
 * @author techial
 */
@Data
@NodeEntity
@Accessors(chain = true)
public class KnowledgeNode {
    @Id
    private Long id;

    @Labels
    private Collection<String> labels;

    @Index(unique = true)
    private String name;

    @Properties(allowCast = true)
    private Map<String, String> property;
}
