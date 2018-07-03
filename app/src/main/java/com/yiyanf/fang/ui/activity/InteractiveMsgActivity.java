package com.yiyanf.fang.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.yiyanf.fang.R;
import com.yiyanf.fang.api.model.InteractiveMsg;
import com.yiyanf.fang.api.model.ResGetInteractiveMsgs;
import com.yiyanf.fang.entity.BaseResponse;
import com.yiyanf.fang.presenter.imp.NotificationPresenter;
import com.yiyanf.fang.ui.adapter.InteractiveAdapter;
import com.yiyanf.fang.ui.adapter.recycleadapter.XMBaseAdapter;
import com.yiyanf.fang.ui.widget.MyItemDecoration;
import com.yiyanf.fang.ui.widget.pull2refresh.PullToRefreshView;
import com.yiyanf.fang.util.NetWorkUtil;
import com.yiyanf.fang.view.IView;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 互动消息
 * <p>
 * Created by Hition on 2017/12/22.
 */

public class InteractiveMsgActivity extends BaseActivity implements
        XMBaseAdapter.OnItemClickListener, IView{

    @Bind(R.id.refresh_view)
    PullToRefreshView mRefreshView;
    @Bind(R.id.rv_msg_interactive)
    RecyclerView mRvInteractive;

    private int pageNo = 1;
    private InteractiveAdapter adapter;
    private NotificationPresenter interactivePresenter;


    public static void startActivity(Context context) {
        Intent intent = new Intent(context, InteractiveMsgActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interactive_msg);
        ButterKnife.bind(this);
        initView();
        interactivePresenter = new NotificationPresenter(this);
        getInteractiveMsg();
    }

    private void initView() {
        mRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageNo = 1;
                getInteractiveMsg();
            }
        });

        MyItemDecoration itemDecoration = new MyItemDecoration(this, LinearLayoutManager.HORIZONTAL,
                1, R.color.cl_dddddd);
        mRvInteractive.addItemDecoration(itemDecoration);
        LinearLayoutManager layoutmanager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRvInteractive.setLayoutManager(layoutmanager);
        adapter = new InteractiveAdapter(this);
        mRvInteractive.setAdapter(adapter);

        adapter.setMore(R.layout.view_recyclerview_more,new OnLoadMoreDataListener());
        adapter.setNoMore(R.layout.view_recyclerview_nomore);
        adapter.setError(R.layout.view_recyclerview_error);

        adapter.setOnItemClickListener(this);
    }

    private void getInteractiveMsg(){
        if(NetWorkUtil.isNetworkConnected(this)){
            interactivePresenter.getInteractiveMsgs(pageNo);
        }else{
            showToast(R.string.no_network);
        }
    }

    @Override
    public void onItemClick(int position) {
        // 互动消息item点击事件,若是视频则点击去到视频详情页，若是楼盘区域圈子，则去楼盘或者区域详情



    }

    /**
     * 上拉加载更多
     */
    private class OnLoadMoreDataListener implements XMBaseAdapter.OnLoadMoreListener{

        @Override
        public void onLoadMore() {
            pageNo++;
            getInteractiveMsg();
        }
    }

    @Override
    public void fillData(BaseResponse data, int flag) {
        if(mRefreshView.isRefreshing()){
            mRefreshView.setRefreshing(false);
        }

        if(0 == data.getReturnValue()){
            ResGetInteractiveMsgs interactiveMsgsData = (ResGetInteractiveMsgs) data.getReturnData();
            if(null!=interactiveMsgsData){
                int totalPage = interactiveMsgsData.getTotalpage();
                if(pageNo <= totalPage){
                    List<InteractiveMsg> interactiveMsgs = interactiveMsgsData.getInteractivelist();
                    if(null!=interactiveMsgs && !interactiveMsgs.isEmpty()){
                        if(1 == pageNo && adapter.getCount() >0){
                            adapter.clear();
                        }
                        adapter.addAll(interactiveMsgs);
                    }
                }else{
                    adapter.stopMore();
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
        interactivePresenter.onUnsubscribe();
    }


}
