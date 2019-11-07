package top.techial.spring.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;


/**
 * Created by dean on @Date: 2019-09-11
 *
 * @author techial
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public
class PageDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * list
     */
    private List<?> content;

    /**
     * length
     */
    private long totalElements;

    /**
     * page
     */
    private int pageNum;

    /**
     * size
     */
    private int pageSize;

    /**
     * total pages
     */
    private Integer totalPages;

}
