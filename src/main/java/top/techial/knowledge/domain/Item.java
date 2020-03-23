package top.techial.knowledge.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.Version;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * @author techial
 */
@Entity
@Data
@EqualsAndHashCode(exclude = {"author"})
@ToString(exclude = {"author"})
@Accessors(chain = true)
public class Item implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Integer id;

    @ManyToOne
    private User author;

    private Boolean share;

    @OneToOne
    private Node rootNode;

    private String name;

    private String image;

    private String description;

    @Version
    private Long version;

    @CreationTimestamp
    private Instant createTime;

    @UpdateTimestamp
    private Instant updateTime;
}
