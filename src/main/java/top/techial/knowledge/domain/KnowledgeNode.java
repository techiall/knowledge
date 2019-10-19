package top.techial.knowledge.domain;

import lombok.Data;
import lombok.experimental.Accessors;
import org.neo4j.ogm.annotation.Labels;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Properties;
import org.neo4j.ogm.annotation.Relationship;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

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

    @Indexed
    private String name;

    @Properties(allowCast = true)
    private Map<String, String> property;

    @Relationship
    private Set<KnowledgeNode> relations;
}
