package top.techial.knowledge.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author techial
 */
@Data
@Accessors(chain = true)
public class NewNodeDTO {
    private Long id;

    private String title;
}
