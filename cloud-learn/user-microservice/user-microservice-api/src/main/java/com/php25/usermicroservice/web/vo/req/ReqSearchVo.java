package com.php25.usermicroservice.web.vo.req;

import com.php25.common.flux.web.BaseDto;
import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * @author: penghuiping
 * @date: 2019/7/15 20:49
 * @description:
 */
public class ReqSearchVo  {

    private List<SearchParamVo> searchParams;

    private Integer pageNum;

    private Integer pageSize;

    private Sort.Direction direction;

    private String property;

    public ReqSearchVo() {
    }

    public ReqSearchVo(List<SearchParamVo> searchParams, Integer pageNum, Integer pageSize, Sort.Direction direction, String property) {
        this.searchParams = searchParams;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.direction = direction;
        this.property = property;
    }

    public List<SearchParamVo> getSearchParams() {
        return searchParams;
    }

    public void setSearchParams(List<SearchParamVo> searchParams) {
        this.searchParams = searchParams;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Sort.Direction getDirection() {
        return direction;
    }

    public void setDirection(Sort.Direction direction) {
        this.direction = direction;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }
}
