package com.yiyanf.fang.model;


import com.yiyanf.fang.entity.BaseResponse;

/**
 * Created by Hition on 2016/12/9.
 */

public interface BaseListener<T> {

    // 获取数据或提交成功
    void onSuccess(BaseResponse<T> data,int flag);

    // 获取数据失败或提交失败
    void onFailed(Throwable e,int flag);

}
