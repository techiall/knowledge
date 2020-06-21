package top.techial.knowledge.domain;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.elasticsearch.annotations.Document;
import top.techial.knowledge.service.converter.LabelsConverter;
import top.techial.knowledge.service.converter.PropertyConverter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * @author techial
 */
@Entity
@Table(name = "node", indexes = {
        @Index(name = "item_id_index", columnList = "item_id"),
        @Index(name = "name_index", columnList = "name")
})
@Document(indexName = "nodes", type = "_doc")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Node implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "name")
    private String name;

    @Convert(converter = LabelsConverter.class)
    @Lob
    private Labels labels;

    @Convert(converter = PropertyConverter.class)
    @Lob
    private Property property;

    @Column(name = "item_id")
    private Integer itemId;

    @Transient
    private String text;

    @CreationTimestamp
    private Instant createTime;

    @UpdateTimestamp
    private Instant updateTime;

    public Long getId() {
        return id;
    }

    public Node setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Node setName(String name) {
        this.name = name;
        return this;
    }

    public Labels getLabels() {
        return labels;
    }

    public Node setLabels(Labels labels) {
        this.labels = labels;
        return this;
    }

    public Property getProperty() {
        return property;
    }

    public Node setProperty(Property property) {
        this.property = property;
        return this;
    }

    public Integer getItemId() {
        return itemId;
    }

    public Node setItemId(Integer itemId) {
        this.itemId = itemId;
        return this;
    }

    public String getText() {
        return text;
    }

    public Node setText(String text) {
        this.text = text;
        return this;
    }

    public Instant getCreateTime() {
        return createTime;
    }

    public Node setCreateTime(Instant createTime) {
        this.createTime = createTime;
        return this;
    }

    public Instant getUpdateTime() {
        return updateTime;
    }

    public Node setUpdateTime(Instant updateTime) {
        this.updateTime = updateTime;
        return this;
    }
}
