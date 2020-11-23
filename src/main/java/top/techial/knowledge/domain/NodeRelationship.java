package top.techial.knowledge.domain;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import top.techial.knowledge.service.converter.PropertyConverter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * @author techial
 */
@Entity
@IdClass(NodeRelationshipPK.class)
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Table(name = "node_relationship", indexes = {
        @Index(name = "distance_index", columnList = "distance")
})
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

    public Long getAncestor() {
        return ancestor;
    }

    public void setAncestor(Long ancestor) {
        this.ancestor = ancestor;
    }

    public Long getDescendant() {
        return descendant;
    }

    public void setDescendant(Long descendant) {
        this.descendant = descendant;
    }

    public Long getDistance() {
        return distance;
    }

    public void setDistance(Long distance) {
        this.distance = distance;
    }

    public Property getProperty() {
        return property;
    }

    public void setProperty(Property property) {
        this.property = property;
    }

    public Instant getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Instant createTime) {
        this.createTime = createTime;
    }

    public Instant getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Instant updateTime) {
        this.updateTime = updateTime;
    }
}
