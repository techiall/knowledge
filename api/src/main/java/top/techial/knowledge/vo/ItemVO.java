package top.techial.knowledge.vo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author techial
 */
@Data
@Accessors(chain = true)
public class ItemVO {

    private boolean share;

    private String name;

    private String description;
}
