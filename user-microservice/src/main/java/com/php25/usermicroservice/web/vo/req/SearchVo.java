package com.php25.usermicroservice.web.vo.req;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import java.util.List;

/**
 * @author: penghuiping
 * @date: 2019/8/16 22:29
 * @description:
 */
@Setter
@Getter
public class SearchVo {

    @Min(1)
    private Integer pageNum;

    @Min(5)
    private Integer pageSize;

    private List<SearchParamVo> searchParamVoList;
}
