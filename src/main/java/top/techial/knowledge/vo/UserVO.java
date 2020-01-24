package top.techial.knowledge.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * @author techial
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserVO {
    private String nickName;

    private String image;
}
