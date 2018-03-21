package com.joinsoft.distributedtransaction.dto;

import java.io.Serializable;

/**
 * Created by penghuiping on 2017/9/22.
 */
public class TypeReferenceDto implements Serializable {
    private String className;

    private String genericTypeName;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getGenericTypeName() {
        return genericTypeName;
    }

    public void setGenericTypeName(String genericTypeName) {
        this.genericTypeName = genericTypeName;
    }
}
