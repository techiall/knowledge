package top.techial.knowledge.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;

/**
 * @author techial
 */
@Data
@Accessors(chain = true)
public class SearchDTO {

    private String user;

    private String text;

    private NodeInfoDTO node;

    private Map<String, List<NodeBaseDTO>> info;

}
