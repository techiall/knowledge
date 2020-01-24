package top.techial.knowledge.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.Instant;

/**
 * @author techial
 */
@Data
@Accessors(chain = true)
public class UserDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String userName;

    private String nickName;

    private String images;

    private Instant createTime;

    private Instant updateTime;
}
