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
public class ItemDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private Boolean share;

    private String name;

    private String description;

    private Instant createTime;

    private Instant updateTime;

    private UserDTO author;
}
