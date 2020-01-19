package top.techial.knowledge.domain;

import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import top.techial.knowledge.converter.LabelsConverter;
import top.techial.knowledge.converter.PropertyConverter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * @author techial
 */
@Entity
@Data
@Accessors(chain = true)
public class Node implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @Convert(converter = LabelsConverter.class)
    @Lob
    private Labels labels;

    @Convert(converter = PropertyConverter.class)
    @Lob
    private Property property;

    @OneToOne(fetch = FetchType.LAZY)
    private Item item;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private String text;

    @CreationTimestamp
    private Instant createTime;

    @UpdateTimestamp
    private Instant updateTime;
}
