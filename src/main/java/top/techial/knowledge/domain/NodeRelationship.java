package top.techial.knowledge.domain;

import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import top.techial.knowledge.service.converter.PropertyConverter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * @author techial
 */
@Data
@Accessors(chain = true)
@Entity
@Table(name = "node_relationship")
@IdClass(NodeRelationshipPK.class)
public class NodeRelationship implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 祖先节点
     */
    @Id
    @Column(name = "ancestor")
    private Long ancestor;

    /**
     * 后代节点
     */
    @Id
    @Column(name = "descendant")
    private Long descendant;

    /**
     * 祖先距离后代的距离
     */
    private Long distance;

    @Convert(converter = PropertyConverter.class)
    @Lob
    private Property property;

    @CreationTimestamp
    private Instant createTime;

    @UpdateTimestamp
    private Instant updateTime;
}
