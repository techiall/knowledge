package top.techial.knowledge.domain;

import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.Instant;

/**
 * @author techial
 */
@Entity
@Data
@Accessors(chain = true)
public class Item {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private User user;

    private String name;

    private String description;

    @CreationTimestamp
    private Instant createTime;

    @UpdateTimestamp
    private Instant updateTime;
}
