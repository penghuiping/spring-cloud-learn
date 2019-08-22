package com.php25.usermicroservice.web.vo.req;

import com.php25.common.core.specification.Operator;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author: penghuiping
 * @date: 2019/7/15 21:00
 * @description:
 */
public class SearchParamVo {

    @NotBlank
    private String fieldName;

    @NotNull
    private Object value;

    @NotNull
    private Operator operator;

    public SearchParamVo() {
    }

    public SearchParamVo(String fieldName, Object value, Operator operator) {
        this.fieldName = fieldName;
        this.value = value;
        this.operator = operator;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }
}
