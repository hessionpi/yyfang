package com.yiyanf.fang.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yiyanf.fang.FangConstants;
import com.yiyanf.fang.R;
import com.yiyanf.fang.api.model.MineVideoInfo;
import com.yiyanf.fang.api.model.ResGetLoginUserinfo;
import com.yiyanf.fang.api.model.ResGetMyVideos;
import com.yiyanf.fang.entity.BaseResponse;
import com.yiyanf.fang.entity.LoginModel;
import com.yiyanf.fang.model.UserInfoCenter;
import com.yiyanf.fang.presenter.imp.UserPresenter;
import com.yiyanf.fang.ui.adapter.MineVideoAdapter;
import com.yiyanf.fang.ui.adapter.recycleadapter.XMBaseAdapter;
import com.yiyanf.fang.ui.widget.SpacesItemDecoration;
import com.yiyanf.fang.ui.widget.imgloader.ImageLoader;
import com.yiyanf.fang.util.NetWorkUtil;
import com.yiyanf.fang.view.IView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MineActivity extends BaseActivity implements View.OnClickListener, IView, XMBaseAdapter.OnLoadMoreListener {
    @Bind(R.id.iv_avatar)
    ImageView mAvatar;
    @Bind(R.id.tv_nickname)
    TextView mNickName;
    /*  @Bind(R.id.ll_mine)
      LinearLayout llMine;
      @Bind(R.id.tv_mine_guide)
      TextView tvMineGuide;
      @Bind(R.id.ll_mine_vedio)
      LinearLayout llMineVedio;*/
  /*  @Bind(R.id.ll_mine_favorite)
    LinearLayout llMineFavorite;*/
    /*  @Bind(R.id.ll_mine_guide)
      LinearLayout llMineGuide;*/
    @Bind(R.id.ll_mine_attention)
    LinearLayout llMineAttention;
    @Bind(R.id.ll_mine_fans)
    LinearLayout llMineFans;
/*    @Bind(R.id.ll_settings)
    LinearLayout mSettings;*/

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.ll_not_login)
    LinearLayout llNotLogin;
    @Bind(R.id.ll_login)
    LinearLayout llLogin;
    @Bind(R.id.favorite_count)
    TextView favoriteCount;
    @Bind(R.id.attention_count)
    TextView attentionCount;
    @Bind(R.id.fans_count)
    TextView fansCount;
    @Bind(R.id.my_home)
    TextView myHome;
  /*  @Bind(R.id.textView3)
    TextView textView3;*/


    LoginModel userInfo;
    UserPresenter presenter;
    String thumbnail;
    int flag;
    @Bind(R.id.tv_signature)
    TextView tvSignature;
    @Bind(R.id.iv_setting)
    ImageView ivSetting;
    @Bind(R.id.ll_mine_favorite)
    LinearLayout llMineFavorite;
    @Bind(R.id.tv_video_count)
    TextView tvVideoCount;
    @Bind(R.id.rv_recommend)
    RecyclerView rvRecommend;
    @Bind(R.id.ll_video_empty)
    LinearLayout llVideoEmpty;

    MineVideoAdapter adapter;
    int pageno;
    String userid;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, MineActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        presenter = new UserPresenter(this);
        initView();
    }

    private void loadData() {
        if (NetWorkUtil.isNetworkConnected(this)) {
            presenter.getLoginUserinfo();
        } else {
            showToast(R.string.no_network);
        }
    }

    //获取视频发布者信息
    private void getPublishVideos(int pageno) {
        if (NetWorkUtil.isNetworkConnected(this)) {
            presenter.getMyVideos(pageno, 10);
        } else {
            showToast(R.string.no_network);
        }
    }

    private void initView() {

        mAvatar.setOnClickListener(this);
        llNotLogin.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        // llMineVedio.setOnClickListener(this);
        llMineFavorite.setOnClickListener(this);
        // llMineGuide.setOnClickListener(this);
        llMineAttention.setOnClickListener(this);
        llMineFans.setOnClickListener(this);
        //  mSettings.setOnClickListener(this);
        myHome.setOnClickListener(this);
        ivSetting.setOnClickListener(this);

        StaggeredGridLayoutManager staggeredLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rvRecommend.setLayoutManager(staggeredLayoutManager);
        rvRecommend.setPadding(0, 0, 0, 0);
        rvRecommend.addItemDecoration(new SpacesItemDecoration(4, 2, false));
        adapter = new MineVideoAdapter(this);
        rvRecommend.setAdapter(adapter);
        rvRecommend.setNestedScrollingEnabled(false);
        adapter.setMore(R.layout.view_recyclerview_more, this);
        adapter.setNoMore(R.layout.view_recyclerview_nomore);
        adapter.setError(R.layout.view_recyclerview_error);

        adapter.setOnItemClickListener(new XMBaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // 开始播放
                MineVideoInfo simpleVideo = adapter.getItem(position);
                VideoPlayActivity.startActivity(MineActivity.this, simpleVideo.getVideoid(), simpleVideo.getFrontcover());
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        userInfo = UserInfoCenter.getInstance().getLoginModel();
        if (userInfo == null) {
            llNotLogin.setVisibility(View.VISIBLE);
            llLogin.setVisibility(View.GONE);
            tvSignature.setVisibility(View.GONE);
            favoriteCount.setText("0");
            attentionCount.setText("0");
            fansCount.setText("0");
            llVideoEmpty.setVisibility(View.VISIBLE);

        } else {
            llLogin.setVisibility(View.VISIBLE);
            llNotLogin.setVisibility(View.GONE);
            tvSignature.setVisibility(View.VISIBLE);
            llVideoEmpty.setVisibility(View.GONE);
            //userid = userInfo.getUserid();
           /* ImageLoader.loadTransformImage(this, userInfo.getThumbnail(), R.drawable.icon_man, R.drawable.icon_man, mAvatar, 0);
            mNickName.setText(userInfo.getNickname());
            favoriteCount.setText(String.valueOf(userInfo.getFavorites()));
            attentionCount.setText(String.valueOf(userInfo.getAttentions()));
            fansCount.setText(String.valueOf(userInfo.getFans()));
            tvSignature.setText(userInfo.getSignature());*/
            loadData();
            adapter.clear();
            pageno = 1;
            getPublishVideos(pageno);

        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.ll_mine_favorite:
                if (userInfo == null) {
                    LoginActivity.startActivity(this);
                } else {
                    MineFavoriteActivity.startActivity(this);
                }
                break;

            case R.id.ll_mine_attention:
                if (userInfo == null) {
                    LoginActivity.startActivity(this);
                } else {
                    MineAttentionFansActivity.startActivity(this, 0);
                }
                break;
            case R.id.ll_mine_fans:
                if (userInfo == null) {
                    LoginActivity.startActivity(this);
                } else {
                    MineAttentionFansActivity.startActivity(this, 1);
                }
                break;

            case R.id.iv_setting:
                SettingsActivity.startActivity(this, thumbnail);
                break;
            case R.id.iv_back:
                finish();
                break;

            case R.id.ll_not_login:
                LoginActivity.startActivity(this);
                break;
            case R.id.iv_avatar:
                UserinfoDetailActivity.startActivity(this);
                break;
            case R.id.my_home:
                PersonalCenterActivity.startActivity(this, userInfo.getUserid(), 1);
                break;
        }

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventShowroom(String event) {
        if (!TextUtils.isEmpty(event)) {
            if(event.equals("logout")){
                // 退出登录需要我的个人中心页面变回登陆之前
                llNotLogin.setVisibility(View.VISIBLE);
                llLogin.setVisibility(View.GONE);
                favoriteCount.setText(String.valueOf(0));
                attentionCount.setText(String.valueOf(0));
                fansCount.setText(String.valueOf(0));
                tvSignature.setVisibility(View.GONE);
                llVideoEmpty.setVisibility(View.VISIBLE);
                adapter.clear();
                tvVideoCount.setText("我的视频");
            }else if(event.equals("login")){
                if (userInfo == null) {
                    userInfo = UserInfoCenter.getInstance().getLoginModel();
                }
                // 登录需要我的个人中心页面
                llLogin.setVisibility(View.VISIBLE);
                llNotLogin.setVisibility(View.GONE);
            }
        }

    }

    @Override
    public void fillData(BaseResponse data, int flag) {
        switch (flag) {
            case FangConstants.MY_VIDEOS:
                if (0 == data.getReturnValue()) {
                    ResGetMyVideos publishVideos = (ResGetMyVideos) data.getReturnData();
                    if (publishVideos != null) {
                        List<MineVideoInfo> simpleVideos = publishVideos.getVideolist();
                        if (simpleVideos != null && simpleVideos.size() > 0) {
                            if (pageno <= publishVideos.getTotalpage()) {
                                adapter.addAll(simpleVideos);
                                adapter.notifyDataSetChanged();
                                tvVideoCount.setText("我的视频（" + publishVideos.getAmount() + "）");
                            } else {
                                adapter.stopMore();
                            }
                        } else {
                            if (pageno == 1)
                                llVideoEmpty.setVisibility(View.VISIBLE);
                            adapter.stopMore();
                        }
                    } else {
                        if (pageno == 1)
                            llVideoEmpty.setVisibility(View.VISIBLE);
                        adapter.stopMore();

                    }
                }
                break;
            case FangConstants.ATTENTION_LIST:
                if (0 == data.getReturnValue()) {
                    ResGetLoginUserinfo resGetLoginUserinfo = (ResGetLoginUserinfo) data.getReturnData();
                    if (resGetLoginUserinfo != null) {
                        if (userInfo == null) {
                            userInfo = new LoginModel();
                        }
                        thumbnail = resGetLoginUserinfo.getThumbnail();
                        userInfo.setThumbnail(thumbnail);
                        userInfo.setHeadpic(resGetLoginUserinfo.getHeadpic());
                        int attentioncount = resGetLoginUserinfo.getAttentioncount();
                        int fanscount = resGetLoginUserinfo.getFanscount();
                        int collectcount = resGetLoginUserinfo.getCollectcount();
                        int mSex = resGetLoginUserinfo.getSex();
                        String nickname = resGetLoginUserinfo.getNickname();
                        ImageLoader.loadTransformImage(this, resGetLoginUserinfo.getThumbnail(), R.drawable.icon_man, R.drawable.icon_man, mAvatar, 0);
                        mNickName.setText(nickname);
                        favoriteCount.setText(collectcount + "");
                        attentionCount.setText(attentioncount + "");
                        fansCount.setText(fanscount + "");
                       String signature= resGetLoginUserinfo.getSignature();
                       if (signature==null||"".equals(signature)){
                           tvSignature.setText("可以在此介绍下自己，让大家更了解你。");
                           tvSignature.setTextColor(ContextCompat.getColor(this,R.color.cl_999999));
                       }else{
                           tvSignature.setText(signature);
                           tvSignature.setTextColor(ContextCompat.getColor(this,R.color.cl_555555));
                       }


                        userInfo.setAttentions(attentioncount);
                        userInfo.setFans(fanscount);
                        userInfo.setFavorites(collectcount);
                        userInfo.setSex(mSex);
                        userInfo.setCompany(resGetLoginUserinfo.getCompany());
                        userInfo.setSignature(resGetLoginUserinfo.getSignature());
                        userInfo.setMobile(resGetLoginUserinfo.getMobile());
                        userInfo.setNickname(nickname);
                        userInfo.setUsersign(resGetLoginUserinfo.getUsersign());
                        userInfo.setFangId(resGetLoginUserinfo.getFangid());
                        userInfo.setOpenPhone(resGetLoginUserinfo.getMobilevisible());
                        UserInfoCenter.getInstance().setLoginBean(userInfo);
                /*ImageLoader.loadTransformImage(this, thumbnail, R.drawable.icon_man, R.drawable.icon_man, mAvatar, 0);
                mNickName.setText(nickname);
                favoriteCount.setText(String.valueOf(collectcount) + "");
                attentionCount.setText(String.valueOf(attentioncount) + "");
                fansCount.setText(String.valueOf(fanscount) + "");*/
                    }
                }
                break;
        }
    }

    @Override
    public void showFailedView(int flag) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onLoadMore() {
        pageno++;
        //   LogUtil.e("+++++++++++++++++++++"+pageno);
        getPublishVideos(pageno);
    }
}
