package top.techial.knowledge.service.dto;

import java.io.Serializable;
import java.util.List;


/**
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

    public PageDTO setContent(List<?> content) {
        this.content = content;
        return this;
    }

    public long getTotalElements() {
        return this.totalElements;
    }

    public PageDTO setTotalElements(long totalElements) {
        this.totalElements = totalElements;
        return this;
    }

    public int getPageNum() {
        return this.pageNum;
    }

    public PageDTO setPageNum(int pageNum) {
        this.pageNum = pageNum;
        return this;
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public PageDTO setPageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public Integer getTotalPages() {
        return this.totalPages;
    }

    public PageDTO setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
        return this;
    }

    public String toString() {
        return "PageDTO(content=" + this.getContent() + ", totalElements=" + this.getTotalElements() + ", pageNum=" + this.getPageNum() + ", pageSize=" + this.getPageSize() + ", totalPages=" + this.getTotalPages() + ")";
    }
}
