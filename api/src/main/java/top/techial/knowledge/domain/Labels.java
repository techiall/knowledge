package top.techial.knowledge.domain;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Set;

/**
 * @author techial
 */
@Data
@Accessors(chain = true)
public class Labels implements Serializable {

    private static final long serialVersionUID = 1L;

    private Set<String> labels;
}
