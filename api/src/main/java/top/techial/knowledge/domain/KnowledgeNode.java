package top.techial.knowledge.domain;

import lombok.Data;
import lombok.experimental.Accessors;
import org.neo4j.ogm.annotation.Labels;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
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

    private String name;

    @Properties(allowCast = true)
    private Map<String, String> property;

    @org.neo4j.ogm.annotation.Property
    private Long parentNodeId;

    @org.neo4j.ogm.annotation.Property
    private Integer itemId;

    @Relationship(value = "parent-child-relation", direction = Relationship.INCOMING)
    private Set<KnowledgeNode> childNodes = new HashSet<>();

    @Property
    private String resourceId;

    @CreatedDate
    private Instant createTime;

    @LastModifiedDate
    private Instant updateTime;
}
