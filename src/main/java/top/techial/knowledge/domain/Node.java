package top.techial.knowledge.domain;

import lombok.Data;
import lombok.experimental.Accessors;
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
@Data
@Table(name = "node", indexes = {
        @Index(name = "item_id_index", columnList = "item_id"),
        @Index(name = "name_index", columnList = "name")
})
@Accessors(chain = true)
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

    private String text;

    @Version
    private Long version;

    @CreationTimestamp
    private Instant createTime;

    @UpdateTimestamp
    private Instant updateTime;
}
