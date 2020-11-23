package top.techial.knowledge.web.rest.vm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.lang.Nullable;

import java.io.Serializable;

/**
 * @author techial
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserVM implements Serializable {

    private static final long serialVersionUID = 1L;

    private String nickName;

    @Nullable
    private String image;

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    @Nullable
    public String getImage() {
        return image;
    }

    public void setImage(@Nullable String image) {
        this.image = image;
    }
}
