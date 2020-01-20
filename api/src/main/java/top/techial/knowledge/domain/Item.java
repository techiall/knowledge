package top.techial.knowledge.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;
import java.util.Set;

/**
 * @author techial
 */
@Entity
@Data
@EqualsAndHashCode(exclude = {"author", "share"})
@ToString(exclude = {"author", "share"})
@Accessors(chain = true)
public class Item {
    @Id
    @GeneratedValue
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    private User author;

    @ManyToMany(mappedBy = "item")
    private Set<User> share;

    private String name;

    private String description;

    @CreationTimestamp
    private Instant createTime;

    @UpdateTimestamp
    private Instant updateTime;
}
