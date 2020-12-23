package cc.techial.knowledge.service.dto;

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

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getShare() {
        return share;
    }

    public void setShare(Boolean share) {
        this.share = share;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Instant createTime) {
        this.createTime = createTime;
    }

    public Instant getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Instant updateTime) {
        this.updateTime = updateTime;
    }

    public UserDTO getAuthor() {
        return author;
    }

    public void setAuthor(UserDTO author) {
        this.author = author;
    }
}
