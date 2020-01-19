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

    @Convert(converter = LabelsConverter.class)
    @Lob
    private Labels labels;

    @Convert(converter = PropertyConverter.class)
    @Lob
    private Property property;

    @Lob
    private String text;

    @CreationTimestamp
    private Instant createTime;

    @UpdateTimestamp
    private Instant updateTime;
}
