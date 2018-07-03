package com.yiyanf.fang.entity;

import java.io.Serializable;

/**
 * Created by Hition on 2017/9/19.
 *
 * 返回响应数据实体
 */

public class BaseResponse<T> implements Serializable {
    /**
     * 错误码，0表示成功，其他表示失败
     */
    private int returnValue;
    /**
     * 错误码的描述
     */
    private String returnMsg;
    /**
     * 返回的数据，json格式
      */
    private T returnData;

    public int getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(int returnValue) {
        this.returnValue = returnValue;
    }

    public String getReturnMsg() {
        return returnMsg;
    }

    public void setReturnMsg(String returnMsg) {
        this.returnMsg = returnMsg;
    }

    public T getReturnData() {
        return returnData;
    }

    public void setReturnData(T returnData) {
        this.returnData = returnData;
    }
}
