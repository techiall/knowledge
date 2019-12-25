package top.techial.knowledge.domain;

import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * @author techial
 */
@Data
@Accessors(chain = true)
@Entity
public class Record implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * neo4j node id
     */
    private Long nodeId;

    /**
     * operator
     */
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Operator operator;


    @ManyToMany(mappedBy = "records")
    private List<User> users = new ArrayList<>();

    /**
     * time
     */
    @CreationTimestamp
    private Instant createTime;

    @UpdateTimestamp
    private Instant updateTime;

}
