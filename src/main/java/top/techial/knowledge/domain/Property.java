package top.techial.knowledge.domain;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author techial
 */
@Data
@Accessors(chain = true)
public class Property implements Serializable {

    private static final long serialVersionUID = 1L;

    private Map<String, List<String>> property;
}
