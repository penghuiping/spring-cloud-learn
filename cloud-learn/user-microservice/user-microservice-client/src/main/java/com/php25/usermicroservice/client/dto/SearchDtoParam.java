package com.php25.usermicroservice.client.dto;

import com.php25.common.core.specification.Operator;
import com.php25.common.flux.web.BaseDto;

/**
 * @author: penghuiping
 * @date: 2019/7/15 21:00
 * @description:
 */
public class SearchDtoParam extends BaseDto {

    private String fieldName;
    private Object value;
    private Operator operator;

    public SearchDtoParam() {
    }

    public SearchDtoParam(String fieldName, Object value, Operator operator) {
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
