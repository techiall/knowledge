package top.techial.knowledge.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author techial
 */
@Data
@Accessors(chain = true)
public class SearchDTO {

    private String user;

    private String text;

    private NodeInfoDTO node;

}
