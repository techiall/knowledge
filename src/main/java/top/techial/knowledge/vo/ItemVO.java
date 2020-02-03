package top.techial.knowledge.vo;

import lombok.Data;
import lombok.experimental.Accessors;
import top.techial.knowledge.valid.Insert;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author techial
 */
@Data
@Accessors(chain = true)
public class ItemVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Boolean share;

    @NotNull(groups = Insert.class)
    @NotEmpty(groups = Insert.class)
    private String name;

    private String description;

    private String image;
}
