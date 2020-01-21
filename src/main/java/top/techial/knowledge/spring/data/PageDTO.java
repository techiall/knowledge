package top.techial.knowledge.spring.data;

import java.io.Serializable;
import java.util.List;


/**
 * Created by dean on @Date: 2019-09-11
 *
 * @author techial
 */
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

    public List<?> getContent() {
        return this.content;
    }

    public long getTotalElements() {
        return this.totalElements;
    }

    public int getPageNum() {
        return this.pageNum;
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public Integer getTotalPages() {
        return this.totalPages;
    }

    public PageDTO setContent(List<?> content) {
        this.content = content;
        return this;
    }

    public PageDTO setTotalElements(long totalElements) {
        this.totalElements = totalElements;
        return this;
    }

    public PageDTO setPageNum(int pageNum) {
        this.pageNum = pageNum;
        return this;
    }

    public PageDTO setPageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public PageDTO setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
        return this;
    }

    public String toString() {
        return "PageDTO(content=" + this.getContent() + ", totalElements=" + this.getTotalElements() + ", pageNum=" + this.getPageNum() + ", pageSize=" + this.getPageSize() + ", totalPages=" + this.getTotalPages() + ")";
    }
}
