package top.techial.knowledge.service.dto;

import java.io.Serializable;
import java.time.Instant;

/**
 * @author techial
 */
public class ItemDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private Boolean share;

    private String name;

    private String image;

    private String description;

    private Instant createTime;

    private Instant updateTime;

    private UserDTO author;

    public Integer getId() {
        return id;
    }

    public ItemDTO setId(Integer id) {
        this.id = id;
        return this;
    }

    public Boolean getShare() {
        return share;
    }

    public ItemDTO setShare(Boolean share) {
        this.share = share;
        return this;
    }

    public String getName() {
        return name;
    }

    public ItemDTO setName(String name) {
        this.name = name;
        return this;
    }

    public String getImage() {
        return image;
    }

    public ItemDTO setImage(String image) {
        this.image = image;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ItemDTO setDescription(String description) {
        this.description = description;
        return this;
    }

    public Instant getCreateTime() {
        return createTime;
    }

    public ItemDTO setCreateTime(Instant createTime) {
        this.createTime = createTime;
        return this;
    }

    public Instant getUpdateTime() {
        return updateTime;
    }

    public ItemDTO setUpdateTime(Instant updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public UserDTO getAuthor() {
        return author;
    }

    public ItemDTO setAuthor(UserDTO author) {
        this.author = author;
        return this;
    }
}
