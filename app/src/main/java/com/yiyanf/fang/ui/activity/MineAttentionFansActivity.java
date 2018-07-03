package com.yiyanf.fang.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.yiyanf.fang.FangConstants;
import com.yiyanf.fang.R;
import com.yiyanf.fang.api.model.AttentionUser;
import com.yiyanf.fang.api.model.ResGetAttentionList;
import com.yiyanf.fang.api.model.ResGetFansList;
import com.yiyanf.fang.entity.BaseResponse;
import com.yiyanf.fang.presenter.imp.UserPresenter;
import com.yiyanf.fang.ui.adapter.AttentionUserAdapter;
import com.yiyanf.fang.ui.adapter.FansAdapter;
import com.yiyanf.fang.ui.adapter.recycleadapter.XMBaseAdapter;
import com.yiyanf.fang.ui.widget.MyItemDecoration;
import com.yiyanf.fang.util.NetWorkUtil;
import com.yiyanf.fang.view.IView;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;

public class MineAttentionFansActivity extends BaseActivity implements View.OnClickListener, IView, XMBaseAdapter.OnLoadMoreListener {
    @Bind(R.id.iv_back)
    ImageView ivBack;
    /*    @Bind(R.id.rb_attention)
        RadioButton rbAttention;
        @Bind(R.id.rb_fans)
        RadioButton rbFans;
        @Bind(R.id.rg_tab_attention)
        RadioGroup rgTabAttention;*/
    @Bind(R.id.sv_attention)
    FrameLayout svAttention;
    @Bind(R.id.ll_fans)
    FrameLayout llFans;
    @Bind(R.id.tv_attention)
    TextView tvAttention;
    @Bind(R.id.view_attention)
    View viewAttention;
    @Bind(R.id.rl_attention)
    RelativeLayout rlAttention;
    @Bind(R.id.tv_fans)
    TextView tvFans;
    @Bind(R.id.view_fans)
    View viewFans;
    @Bind(R.id.rl_fans)
    RelativeLayout rlFans;
    @Bind(R.id.rv_attention_user)
    RecyclerView rvAttentionUser;
    @Bind(R.id.rv_fans)
    RecyclerView rvFans;

    AttentionUserAdapter attentionUserAdapter;

    private static final int MINE_ATTENTION = 0;    // 关注
    private static final int MINE_FANS = 1; // 粉丝

    FansAdapter fansAdapter;
    UserPresenter presenter;
    int flag;

    List<AttentionUser> attentionUsers;
    List<AttentionUser> fansList;
    int pageno;
 /*   @Bind(R.id.ll_empty)
    LinearLayout llEmpty;*/
    @Bind(R.id.ll_attention_empty)
    LinearLayout llAttentionEmpty;
    @Bind(R.id.ll_fans_empty)
    LinearLayout llFansEmpty;

    public static void startActivity(Context context, int flag) {
        Intent intent = new Intent(context, MineAttentionFansActivity.class);
        intent.putExtra("flag", flag);
        context.startActivity(intent);

    }

    private void loadAttentionData() {
        if (NetWorkUtil.isNetworkConnected(this)) {
            presenter.getAttentionList(1, 1, 1);
        } else {
            showToast(R.string.no_network);
        }
    }

    private void loadFansData() {
        if (NetWorkUtil.isNetworkConnected(this)) {
            presenter.getFansList(pageno, FangConstants.PAGE_SIZE_DEFAULT);
        } else {
            showToast(R.string.no_network);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_attention_fans);
        ButterKnife.bind(this);
        presenter = new UserPresenter(this);
        initView();
    }

    private void initView() {
        // rgTabAttention.setId(R.id.rb_fans);
        // rgTabAttention.setOnCheckedChangeListener(this);
        ivBack.setOnClickListener(this);

        pageno = 1;
         loadFansData();

        flag = getIntent().getIntExtra("flag", 0);
        switchTab(flag);
        rlAttention.setOnClickListener(this);
        rlFans.setOnClickListener(this);

        MyItemDecoration decoration = new MyItemDecoration(this, LinearLayoutManager.HORIZONTAL, 1, R.color.colorLine);
        rvAttentionUser.addItemDecoration(decoration);
        rvFans.addItemDecoration(decoration);

        LinearLayoutManager yiyanLayoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvAttentionUser.setLayoutManager(yiyanLayoutManager2);
        rvAttentionUser.setNestedScrollingEnabled(false);
        attentionUserAdapter = new AttentionUserAdapter(this);
        rvAttentionUser.setAdapter(attentionUserAdapter);
        attentionUserAdapter.setOnItemClickListener(new XMBaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                //去个人主页
                AttentionUser user = (AttentionUser) attentionUserAdapter.getItem(position);
              /*  String url = user.getHomepageurl();
                if (url != null) {
                    WebActivity.startActivity(MineAttentionFansActivity.this, url, "个人主页");
                }*/
                PersonalCenterActivity.startActivity(MineAttentionFansActivity.this,String.valueOf(user.getUserid()),0);
               // WebActivity.startActivity(MineAttentionFansActivity.this, FangConstants.USER + user.getUserid() + "&isu=0", "个人主页",user.getUserid());
            }
        });


        LinearLayoutManager yiyanLayoutManager3 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvFans.setLayoutManager(yiyanLayoutManager3);
        fansAdapter = new FansAdapter(this);
        rvFans.setAdapter(fansAdapter);
        fansAdapter.setMore(R.layout.view_recyclerview_more, this);
        fansAdapter.setNoMore(R.layout.view_recyclerview_nomore);
        fansAdapter.setError(R.layout.view_recyclerview_error);
        fansAdapter.setOnItemClickListener(new XMBaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                //去个人主页
                AttentionUser user = (AttentionUser) fansAdapter.getItem(position);
                PersonalCenterActivity.startActivity(MineAttentionFansActivity.this, String.valueOf(user.getUserid()),0);
               // WebActivity.startActivity(MineAttentionFansActivity.this, FangConstants.USER + user.getUserid() + "&isu=0", "个人主页",user.getUserid());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        attentionUserAdapter.clear();
        loadAttentionData();
    }

    private void switchTab(int tabIndex) {
        clearAttentionFans();
       // llEmpty.setVisibility(View.GONE);
        switch (tabIndex) {

            case MINE_ATTENTION:
                svAttention.setVisibility(View.VISIBLE);
                llFans.setVisibility(View.GONE);
                tvAttention.setTextColor(ContextCompat.getColor(this, R.color.black));
                viewAttention.setVisibility(View.VISIBLE);
                //loadAttentionData();
                break;

            case MINE_FANS:
                svAttention.setVisibility(View.GONE);
                llFans.setVisibility(View.VISIBLE);
                tvFans.setTextColor(ContextCompat.getColor(this, R.color.black));
                viewFans.setVisibility(View.VISIBLE);
                pageno = 1;
              //  loadFansData();
                break;
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_attention:
                switchTab(MINE_ATTENTION);
                break;
            case R.id.rl_fans:
                switchTab(MINE_FANS);
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }

    private void clearAttentionFans() {
        tvAttention.setTextColor(ContextCompat.getColor(this, R.color.cl_888888));
        tvFans.setTextColor(ContextCompat.getColor(this, R.color.cl_888888));
        viewAttention.setVisibility(View.GONE);
        viewFans.setVisibility(View.GONE);

    }

    @Override
    public void fillData(BaseResponse data, int flag) {
        switch (flag) {
            case FangConstants.ATTENTION_LIST:
                if (0 == data.getReturnValue()) {

                    ResGetAttentionList getAttentionList = (ResGetAttentionList) data.getReturnData();
                    attentionUsers = getAttentionList.getAttentionusers();
                    if (attentionUsers != null) {
                        attentionUserAdapter.setData(attentionUsers);
                        attentionUserAdapter.notifyDataSetChanged();
                    }
                    if (getAttentionList == null) {
                        llAttentionEmpty.setVisibility(View.VISIBLE);
                    } else {
                        if (attentionUsers == null || attentionUsers.isEmpty()) {
                            llAttentionEmpty.setVisibility(View.VISIBLE);
                        }
                    }
                }

                break;
            case FangConstants.FANS_LIST:
                if (0 == data.getReturnValue()) {
                    ResGetFansList getFansList = (ResGetFansList) data.getReturnData();
                    if (pageno <= getFansList.getTotalpage()) {
                        if (getFansList.getAttentionusers() != null) {
                            fansList = getFansList.getAttentionusers();
                            fansAdapter.addAll(fansList);
                            fansAdapter.notifyDataSetChanged();
                        } else {
                            if (pageno == 1)
                                llFansEmpty.setVisibility(View.VISIBLE);
                        }
                    } else {
                        fansAdapter.stopMore();
                    }
                    if (getFansList.getTotalpage() == 0) {
                        llFansEmpty.setVisibility(View.VISIBLE);
                    }
                } else {
                    fansAdapter.stopMore();
                }

                break;
        }
    }

    @Override
    public void showFailedView(int flag) {

    }

    @Override
    public void onLoadMore() {
        pageno++;
        loadFansData();
    }
}
