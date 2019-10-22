package top.techial.knowledge.domain;

import lombok.Data;
import lombok.experimental.Accessors;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.Labels;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Properties;
import org.springframework.data.annotation.Id;

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
    private Long id;

    @Labels
    private Set<String> labels;

    @Index(unique = true)
    private String name;

    @Properties(allowCast = true)
    private Map<String, String> property;
}
