package com.yiyanf.fang.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yiyanf.fang.ui.activity.BaseActivity;
import com.yiyanf.fang.util.CustomToast;
import com.yiyanf.fang.util.StatisticalTools;

import butterknife.ButterKnife;


/**
 * 父类Fragment
 * Created by Hition on 2016/12/21.
 */

public abstract class BaseFragment extends Fragment {

    protected BaseActivity activity;
    protected boolean mIsFirstVisible = true;
    protected String statistical = "";
    protected LayoutInflater mInflater;

    protected boolean isInit = false;
    protected boolean isLoad = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.mInflater = inflater;
        View mView = inflater.inflate(loadViewLayout(),container,false);
        ButterKnife.bind(this,mView);
        return mView;
    }

    /**
     * 加载fragment布局文件
     * @return layoutId
     */
    protected abstract int loadViewLayout();

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity = (BaseActivity) getActivity();
        setStatistical();
        initView();
        /*boolean isVis = isHidden() || getUserVisibleHint();
        if (isVis && mIsFirstVisible) {
            lazyLoad();
            mIsFirstVisible = false;
        }*/

        isInit = true;
        /**初始化的时候去加载数据**/
        isCanLoadData();
    }

    /**
     * 初始化界面
     */
    private void initView() {
        processLogic();
        setListener();
    }

    /**
     * 处理View显示，填充数据等
     */
    protected abstract void processLogic();

    /**
     * 设置监听
     */
    protected abstract void setListener();

    /**
     * 取消订阅RXJava，防止内存泄漏
     */
    protected abstract void unsubscribe ();

    /*@Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            onVisible();
        } else {
            onInVisible();
        }
    }*/
    @Override
    public void onResume() {
        super.onResume();
        StatisticalTools.fragmentOnResume(statistical);
    }

    @Override
    public void onPause() {
        super.onPause();
        StatisticalTools.fragmentOnPause(statistical);
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        /*if (isVisibleToUser) {
            onVisible();
        } else {
            onInVisible();
        }*/

        isCanLoadData();
    }

    /**
     * 是否可以加载数据
     * 可以加载数据的条件：
     * 1.视图已经初始化
     * 2.视图对用户可见
     */
    private void isCanLoadData() {
        if (!isInit) {
            return;
        }

        if (getUserVisibleHint() && isResumed()) {
            lazyLoad();
            isLoad = true;
        } else {
            if (isLoad) {
                stopLoad();
            }
        }
    }

    /**
     * 当视图已经对用户不可见并且加载过数据，如果需要在切换到其他页面时停止加载数据，可以覆写此方法
     */
    protected void stopLoad() {

    }

    /**
     * 当界面可见时的操作
     */
    protected void onVisible() {
        if (mIsFirstVisible && isResumed()) {
            lazyLoad();
            mIsFirstVisible = false;
        }
    }

    /**
     * 数据懒加载
     */
    protected void lazyLoad() {

    }

    /**
     * 当界面不可见时的操作
     */
    protected void onInVisible() {

    }

    /**
     * 给统计赋值
     */
    public abstract void setStatistical();

    protected void showToast(String msg) {
        activity.showToast(msg);
    }

    protected void showNetworkToast(int msgResId){
        activity.showNetworkToast(msgResId);
    }

    protected void showLoadingDialog() {
        activity.showLoadingView();
    }

    protected void dismissLoading() {
        activity.dismissLoading();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isInit = false;
        isLoad = false;
        ButterKnife.unbind(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unsubscribe();
    }

}
