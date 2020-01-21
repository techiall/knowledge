package top.techial.knowledge.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author techial
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserVO {
    @NotBlank
    private String nickName;

    @NotBlank
    private String image;
}
