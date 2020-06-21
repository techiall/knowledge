package top.techial.knowledge.domain;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * @author techial
 */
@Entity
@Table(indexes = {
        @Index(columnList = "nodeId")
})
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Record implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Long nodeId;

    /**
     * operator
     */
    private String operator;

    private String message;

    @OneToOne
    private User user;

    @CreationTimestamp
    private Instant createTime;

    public Integer getId() {
        return id;
    }

    public Record setId(Integer id) {
        this.id = id;
        return this;
    }

    public Long getNodeId() {
        return nodeId;
    }

    public Record setNodeId(Long nodeId) {
        this.nodeId = nodeId;
        return this;
    }

    public String getOperator() {
        return operator;
    }

    public Record setOperator(String operator) {
        this.operator = operator;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public Record setMessage(String message) {
        this.message = message;
        return this;
    }

    public User getUser() {
        return user;
    }

    public Record setUser(User user) {
        this.user = user;
        return this;
    }

    public Instant getCreateTime() {
        return createTime;
    }

    public Record setCreateTime(Instant createTime) {
        this.createTime = createTime;
        return this;
    }
}
