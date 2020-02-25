package top.techial.knowledge.web.rest.vm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

/**
 * @author techial
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserVM implements Serializable {

    private static final long serialVersionUID = 1L;

    private String nickName;

    private String image;
}
