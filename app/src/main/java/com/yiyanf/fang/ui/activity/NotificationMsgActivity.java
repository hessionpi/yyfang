package com.yiyanf.fang.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.yiyanf.fang.R;
import com.yiyanf.fang.api.model.FangNotification;
import com.yiyanf.fang.api.model.ResGetNotifications;
import com.yiyanf.fang.entity.BaseResponse;
import com.yiyanf.fang.presenter.imp.NotificationPresenter;
import com.yiyanf.fang.ui.adapter.NotificationAdapter;
import com.yiyanf.fang.ui.adapter.recycleadapter.XMBaseAdapter;
import com.yiyanf.fang.ui.widget.pull2refresh.PullToRefreshView;
import com.yiyanf.fang.util.NetWorkUtil;
import com.yiyanf.fang.view.IView;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 通知消息
 * <p>
 * Created by Hition on 2017/12/22.
 */

public class NotificationMsgActivity extends BaseActivity implements IView<ResGetNotifications>,XMBaseAdapter.OnLoadMoreListener {

    @Bind(R.id.refresh_view)
    PullToRefreshView mRefreshView;
    @Bind(R.id.rv_notification_msg)
    RecyclerView mRvNotification;

    NotificationAdapter notifAdapter;
    NotificationPresenter presenter;
    private int pageNo =1 ;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, NotificationMsgActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifation_msg);
        ButterKnife.bind(this);
        initView();
        presenter = new NotificationPresenter(this);
        getNotifications();
    }

    private void getNotifications(){
        if(NetWorkUtil.isNetworkConnected(this)){
            presenter.getNotifications(pageNo);
        }else{
            showToast(R.string.no_network);
        }
    }

    private void initView() {
        mRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageNo = 1;
                getNotifications();
            }
        });
        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRvNotification.setLayoutManager(verticalLayoutManager);
        notifAdapter = new NotificationAdapter(this);
        mRvNotification.setAdapter(notifAdapter);

        notifAdapter.setMore(R.layout.view_recyclerview_more,this);
        notifAdapter.setError(R.layout.view_recyclerview_error);

        notifAdapter.setOnItemClickListener(new XMBaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // 区分通知类型
                FangNotification notification = notifAdapter.getItem(position);
                switch (notification.getType()){
                    case 1: // 认证经纪人

                        break;

                    case 2:// 视频上传

                        break;

                    case 3:// 直播

                        break;

                }
            }
        });
    }

    @Override
    public void onLoadMore() {
        pageNo++;
        getNotifications();
    }

    @Override
    public void fillData(BaseResponse<ResGetNotifications> data, int flag) {
        if(mRefreshView.isRefreshing()){
            mRefreshView.setRefreshing(false);
        }

        if(0 == data.getReturnValue()){
            ResGetNotifications notifications = data.getReturnData();
            if(null != notifications){
                int totalPage = notifications.getTotalpage();
                if(pageNo <= totalPage){
                    List<FangNotification> notificationList = notifications.getNotificationlist();
                    if(1 == pageNo && notifAdapter.getCount() >0){
                        notifAdapter.clear();
                    }
                    notifAdapter.addAll(notificationList);
                }else{
                    notifAdapter.stopMore();
                }
            }
        }
    }

    @Override
    public void showFailedView(int flag) {
        if(mRefreshView.isRefreshing()){
            mRefreshView.setRefreshing(false);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onUnsubscribe();
    }


}
