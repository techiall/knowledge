package top.techial.spring.data;

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
public class PageDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * content
     */
    private List<?> content;

    /**
     * total elements
     */
    private long totalElements;

    /**
     * page num
     */
    private int pageNum;

    /**
     * page size
     */
    private int pageSize;

    /**
     * total pages
     */
    private Integer totalPages;

}
