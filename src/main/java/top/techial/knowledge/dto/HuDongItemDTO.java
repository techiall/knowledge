package top.techial.knowledge.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author techial
 */
@Data
@Accessors(chain = true)
public class HuDongItemDTO {
    private Long id;

    private String title;

    private String image;

    private String detail;

    private String url;

    private String openTypeList;

    private String baseInfoKeyList;

    private String baseInfoValueList;

}
