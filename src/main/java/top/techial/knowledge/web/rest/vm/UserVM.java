package top.techial.knowledge.web.rest.vm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * @author techial
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserVM implements Serializable {

    private static final long serialVersionUID = 1L;

    private String nickName;

    private String image;

    public String getNickName() {
        return nickName;
    }

    public UserVM setNickName(String nickName) {
        this.nickName = nickName;
        return this;
    }

    public String getImage() {
        return image;
    }

    public UserVM setImage(String image) {
        this.image = image;
        return this;
    }
}
