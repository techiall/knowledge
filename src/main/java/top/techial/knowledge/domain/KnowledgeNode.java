package top.techial.knowledge.domain;

import lombok.Data;
import lombok.experimental.Accessors;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.Labels;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Properties;

import java.util.Collection;
import java.util.Map;

/**
 * @author techial
 */
@Data
@Accessors(chain = true)
@NodeEntity
public class KnowledgeNode {
    private Long id;

    @Labels
    private Collection<String> labels;

    @Index(unique = true)
    private String name;

    @Properties(allowCast = true)
    private Map<String, String> property;
}
