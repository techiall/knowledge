package cc.techial.knowledge.service.dto;

import java.io.Serializable;
import java.time.Instant;

/**
 * @author techial
 */
public class UserDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String userName;

    private String nickName;

    private String images;

    private Instant createTime;

    private Instant updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
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
}
