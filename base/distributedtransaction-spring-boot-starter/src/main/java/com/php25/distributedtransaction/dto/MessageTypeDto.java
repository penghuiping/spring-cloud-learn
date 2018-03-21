package com.php25.distributedtransaction.dto;

import java.io.Serializable;

/**
 * Created by penghuiping on 2017/9/21.
 */
public class MessageTypeDto implements Serializable{

    private String className;

    private String classMethod;

    private TypeReferenceDto classMethodParam;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassMethod() {
        return classMethod;
    }

    public void setClassMethod(String classMethod) {
        this.classMethod = classMethod;
    }

    public TypeReferenceDto getClassMethodParam() {
        return classMethodParam;
    }

    public void setClassMethodParam(TypeReferenceDto classMethodParam) {
        this.classMethodParam = classMethodParam;
    }
}
