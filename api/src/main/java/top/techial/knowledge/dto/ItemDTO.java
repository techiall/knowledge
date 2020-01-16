package top.techial.knowledge.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.Instant;

/**
 * @author techial
 */
@Data
@Accessors(chain = true)
public class ItemDTO {
    private Integer id;

    private boolean share;

    private String name;

    private String description;

    private Instant createTime;

    private Instant updateTime;
}
