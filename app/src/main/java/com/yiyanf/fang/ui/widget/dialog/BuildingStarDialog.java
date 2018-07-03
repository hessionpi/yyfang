package com.yiyanf.fang.ui.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.tencent.TIMConversationType;
import com.yiyanf.fang.FangConstants;
import com.yiyanf.fang.R;
import com.yiyanf.fang.api.model.Counselor;
import com.yiyanf.fang.api.model.ResGetCounselorList;
import com.yiyanf.fang.entity.BaseResponse;
import com.yiyanf.fang.model.BaseListener;
import com.yiyanf.fang.presenter.imp.BuildingPresenter;
import com.yiyanf.fang.ui.activity.ChatActivity;
import com.yiyanf.fang.ui.adapter.BuildingStarAdapter;
import com.yiyanf.fang.ui.adapter.recycleadapter.XMBaseAdapter;
import com.yiyanf.fang.util.LogUtil;
import com.yiyanf.fang.util.NetWorkUtil;
import com.yiyanf.fang.util.ToastUtils;
import com.yiyanf.fang.view.IView;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/12/13.
 */

public class BuildingStarDialog extends Dialog implements IView {
    BuildingPresenter buildingPresenter;
    int page = 1;
    Context context;
    BuildingStarAdapter buildingStarAdapter;

    public BuildingStarDialog(@NonNull Context context) {
        super(context);
        this.context = context;
        buildingPresenter = new BuildingPresenter(this);
    }

    /**
     * 请求置业明星
     */
    private void loadCounselorList() {
        if (NetWorkUtil.isNetworkConnected(context)) {
            //  buildingPresenter.getCounselorList(2, info.getBuildingid(), 1, 1);
            buildingPresenter.getCounselorList(2, 1, 1, 1);
        } else {
            ToastUtils.show(context,R.string.no_network);
        }
    }

    RecyclerView buildingStar;

    //楼盘，区域置业明星
    public Dialog showBuildingStarDialog(final int objectType, final int objectId) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.dialog_building_star, null);
        final Dialog dialog = new Dialog(context);
        dialog.setCanceledOnTouchOutside(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(layout);
        dialog.show();

        Window dialogWindow = dialog.getWindow();//获取window
        dialogWindow.setWindowAnimations(R.style.predict_factor_dialog_anim);
        dialogWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogWindow.setGravity(Gravity.CENTER);//
        dialogWindow.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        ImageView ivClose = ButterKnife.findById(layout, R.id.iv_close);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        buildingStar = ButterKnife.findById(layout, R.id.rv_building_star);
        LinearLayoutManager layoutmanager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        buildingStar.setLayoutManager(layoutmanager);
        buildingStarAdapter = new BuildingStarAdapter(context);
        buildingStarAdapter.setMore(R.layout.view_recyclerview_more, new XMBaseAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                page++;
                buildingPresenter.getCounselorList(objectType, objectId, page, FangConstants.PAGE_SIZE_DEFAULT);
            }
        });
        buildingStarAdapter.setNoMore(R.layout.view_recyclerview_nomore);
        buildingStarAdapter.setError(R.layout.view_recyclerview_error);
        buildingStarAdapter.setOnReserveListener(new BuildingStarAdapter.OnConsultantListener() {
            @Override
            public void onAttentionClick(String pusherId, int flag) {
                buildingPresenter.attentionOrCancel(0,pusherId,flag,new OnResonseListener());
            }

            @Override
            public void onSendMsg(String consultantUid) {
                ChatActivity.navToChat(context, consultantUid, TIMConversationType.C2C);
            }
        });
        buildingStar.setAdapter(buildingStarAdapter);
        page = 1;
        buildingPresenter.getCounselorList(objectType, objectType, page, FangConstants.PAGE_SIZE_DEFAULT);
        return dialog;
    }

    @Override
    public void fillData(BaseResponse data, int flag) {
        int returnValue = data.getReturnValue();
        if (FangConstants.RETURN_VALUE_OK == returnValue) {
            ResGetCounselorList resGetCounselorList = (ResGetCounselorList) data.getReturnData();
            if (resGetCounselorList != null) {
                List<Counselor> CounselorList = resGetCounselorList.getCouncilors();
                if (CounselorList != null && CounselorList.size() > 0) {
                    buildingStarAdapter.addAll(CounselorList);
                    buildingStarAdapter.notifyDataSetChanged();
                } else {
                    if (page == 1)
                        buildingStar.setVisibility(View.GONE);
                    buildingStarAdapter.stopMore();
                }
            } else {
                if (page == 1)
                    buildingStar.setVisibility(View.GONE);
                buildingStarAdapter.stopMore();
            }
        }
    }

    @Override
    public void showFailedView(int flag) {

    }
    private class OnResonseListener implements BaseListener {
        @Override
        public void onSuccess(BaseResponse data, int flag) {
            if(0 == data.getReturnValue()){
                ToastUtils.show(context,"操作成功");
            }else{
                LogUtil.e("hition===",data.getReturnMsg()+data.getReturnValue());
            }
        }

        @Override
        public void onFailed(Throwable e, int flag) {

        }
    }
}
