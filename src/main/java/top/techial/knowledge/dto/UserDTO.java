package top.techial.knowledge.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.Instant;

/**
 * @author techial
 */
@Data
@Accessors(chain = true)
public class UserDTO {
    private Integer id;

    private String userName;

    private String nickName;

    private String images;

    private Instant createTime;

    private Instant updateTime;
}
