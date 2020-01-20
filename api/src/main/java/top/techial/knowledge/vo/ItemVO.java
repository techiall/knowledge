package top.techial.knowledge.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

/**
 * @author techial
 */
@Data
@Accessors(chain = true)
public class ItemVO {

    private Boolean share;

    @NotBlank
    private String name;

    @NotBlank
    private String description;
}
