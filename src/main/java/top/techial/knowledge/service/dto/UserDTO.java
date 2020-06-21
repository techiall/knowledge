package top.techial.knowledge.service.dto;

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

    public UserDTO setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getUserName() {
        return userName;
    }

    public UserDTO setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public String getNickName() {
        return nickName;
    }

    public UserDTO setNickName(String nickName) {
        this.nickName = nickName;
        return this;
    }

    public String getImages() {
        return images;
    }

    public UserDTO setImages(String images) {
        this.images = images;
        return this;
    }

    public Instant getCreateTime() {
        return createTime;
    }

    public UserDTO setCreateTime(Instant createTime) {
        this.createTime = createTime;
        return this;
    }

    public Instant getUpdateTime() {
        return updateTime;
    }

    public UserDTO setUpdateTime(Instant updateTime) {
        this.updateTime = updateTime;
        return this;
    }
}
