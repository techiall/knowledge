package top.techial.knowledge.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author techial
 */
@Data
@Accessors(chain = true)
public class ItemVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Boolean share;

    private String name;

    private String description;

    private String image;
}
