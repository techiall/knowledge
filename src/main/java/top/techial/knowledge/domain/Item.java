package top.techial.knowledge.domain;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * @author techial
 */
@Entity
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
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

    @CreationTimestamp
    private Instant createTime;

    @UpdateTimestamp
    private Instant updateTime;

    public Integer getId() {
        return id;
    }

    public Item setId(Integer id) {
        this.id = id;
        return this;
    }

    public User getAuthor() {
        return author;
    }

    public Item setAuthor(User author) {
        this.author = author;
        return this;
    }

    public Boolean getShare() {
        return share;
    }

    public Item setShare(Boolean share) {
        this.share = share;
        return this;
    }

    public Node getRootNode() {
        return rootNode;
    }

    public Item setRootNode(Node rootNode) {
        this.rootNode = rootNode;
        return this;
    }

    public String getName() {
        return name;
    }

    public Item setName(String name) {
        this.name = name;
        return this;
    }

    public String getImage() {
        return image;
    }

    public Item setImage(String image) {
        this.image = image;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Item setDescription(String description) {
        this.description = description;
        return this;
    }

    public Instant getCreateTime() {
        return createTime;
    }

    public Item setCreateTime(Instant createTime) {
        this.createTime = createTime;
        return this;
    }

    public Instant getUpdateTime() {
        return updateTime;
    }

    public Item setUpdateTime(Instant updateTime) {
        this.updateTime = updateTime;
        return this;
    }
}
