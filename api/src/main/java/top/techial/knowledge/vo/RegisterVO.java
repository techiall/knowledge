package top.techial.knowledge.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.experimental.Accessors;
import top.techial.knowledge.domain.User;

import java.io.Serializable;

/**
 * @author techial
 */
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class RegisterVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String userName;

    private String password;

    public User toUser() {
        return new User().setUserName(userName).setPassword(this.password);
    }

}
