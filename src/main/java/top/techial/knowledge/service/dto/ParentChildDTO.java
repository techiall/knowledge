package top.techial.knowledge.service.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ParentChildDTO {
    private Long descendant;
    private Long ancestor;
}
