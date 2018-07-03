package com.yiyanf.fang.view;


import com.yiyanf.fang.entity.BaseResponse;

/**
 * Created by Hition on 2016/12/9.
 *
 *  适用于获取数据填充页面的activity
 */

public interface IView<T> {

    /**
     * @param data        返回数据源
     */
    void fillData(BaseResponse<T> data,int flag);

    /**
     * 显示数据获取失败view
     */
    void showFailedView(int flag);


}
