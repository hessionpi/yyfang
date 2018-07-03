package com.yiyanf.fang.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tencent.TIMConversationType;
import com.umeng.socialize.ShareAction;
import com.yiyanf.fang.FangConstants;
import com.yiyanf.fang.R;
import com.yiyanf.fang.api.model.ResGetPublishVideos;
import com.yiyanf.fang.api.model.ResGetVideoPublisherInfo;
import com.yiyanf.fang.api.model.Respsbuilding;
import com.yiyanf.fang.api.model.SimpleVideo;
import com.yiyanf.fang.entity.BaseResponse;
import com.yiyanf.fang.model.BaseListener;
import com.yiyanf.fang.presenter.imp.BuildingPresenter;
import com.yiyanf.fang.presenter.imp.UserPresenter;
import com.yiyanf.fang.presenter.imp.VideoPresenter;
import com.yiyanf.fang.ui.adapter.VideoPublishAdapter;
import com.yiyanf.fang.ui.adapter.recycleadapter.XMBaseAdapter;
import com.yiyanf.fang.ui.widget.SpacesItemDecoration;
import com.yiyanf.fang.ui.widget.dialog.DialogManager;
import com.yiyanf.fang.ui.widget.dialog.ShareDialog;
import com.yiyanf.fang.ui.widget.imgloader.ImageLoader;
import com.yiyanf.fang.util.LogUtil;
import com.yiyanf.fang.util.NetWorkUtil;
import com.yiyanf.fang.util.ToastUtils;
import com.yiyanf.fang.view.IView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PersonalCenterActivity extends BaseActivity implements IView, View.OnClickListener, XMBaseAdapter.OnLoadMoreListener {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.iv_avatar)
    ImageView ivAvatar;
    @Bind(R.id.tv_nickname)
    TextView tvNickname;
    @Bind(R.id.tv_attention)
    TextView tvAttention;
    @Bind(R.id.iv_message)
    ImageView ivMessage;
    @Bind(R.id.iv_phone)
    ImageView ivPhone;
    @Bind(R.id.ll_login)
    LinearLayout llLogin;

    @Bind(R.id.favorite_count)
    TextView favoriteCount;
    @Bind(R.id.ll_mine_favorite)
    LinearLayout llMineFavorite;
    @Bind(R.id.attention_count)
    TextView attentionCount;
    @Bind(R.id.ll_mine_attention)
    LinearLayout llMineAttention;
    @Bind(R.id.fans_count)
    TextView fansCount;
    @Bind(R.id.ll_mine_fans)
    LinearLayout llMineFans;
    @Bind(R.id.rv_recommend)
    RecyclerView rvRecommend;

    @Bind(R.id.iv_share)
    ImageView ivShare;

    @Bind(R.id.tv_data)
    TextView tvData;


    @Bind(R.id.tv_video_count)
    TextView tvVideoCount;

    @Bind(R.id.tv_title)
    TextView tvTitle;
    boolean isUnfold = false;
    BuildingPresenter buildingPresenter;
    UserPresenter presenter;
    String nickName;
    String headPic;
    String userid;
    int isu;
    int isAttention;
    boolean isAgent;
    @Bind(R.id.ll_video_empty)
    LinearLayout llVideoEmpty;

    // List<BuildingGroup> respsbuildings;
    int pageno;
    @Bind(R.id.tv_signature)
    TextView tvSignature;
    private VideoPresenter videoPresenter;
    VideoPublishAdapter adapter;
    String number;
String faceUrl;
    List<Respsbuilding> resGetBuildings;
    // 友盟社会化分享
    private ShareAction mShareAction = new ShareAction(this);

    public static void startActivity(Context context, String id, int isu) {
        Intent intent = new Intent(context, PersonalCenterActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("isu", isu);
        context.startActivity(intent);
    }

    public static void startActivityForResult(Activity activity, String id, int isu) {
        Intent intent = new Intent(activity, PersonalCenterActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("isu", isu);
        activity.startActivityForResult(intent, 1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_center);
        ButterKnife.bind(this);
        buildingPresenter = new BuildingPresenter(this);
        presenter = new UserPresenter(this);
        videoPresenter = new VideoPresenter(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        initView();
        initData();
      /*  LoginModel model=UserInfoCenter.getInstance().getLoginModel();
        if (model !=null){
            tvNickname.setText(model.getNickname());
            ImageLoader.loadTransformImage(this, model.getThumbnail(), R.drawable.icon_man, R.drawable.icon_man, ivAvatar, 0);
        }*/
    }

    //获取视频发布者信息
    private void getVideoPublisherInfo(String publisherid) {
        if (NetWorkUtil.isNetworkConnected(this)) {
            presenter.getVideoPublisherInfo(publisherid);
        } else {
            showToast(R.string.no_network);
        }
    }

    //获取视频发布者信息
    private void getPublishVideos(String publisherid, int pageno) {
        if (NetWorkUtil.isNetworkConnected(this)) {
            presenter.getPublishVideos(publisherid, pageno, 10);
        } else {
            showToast(R.string.no_network);
        }
    }


    private void attentionUser(int flag) {
        if (NetWorkUtil.isNetworkConnected(this)) {
            videoPresenter.attention(userid + "", flag, new OnPublisherAttentionListener());
        } else {
            ToastUtils.show(this, R.string.no_network);
        }
    }

    void initView() {
        userid = getIntent().getStringExtra("id");
        isu = getIntent().getIntExtra("isu", 0);


        ivBack.setOnClickListener(this);
        ivShare.setOnClickListener(this);
        tvAttention.setOnClickListener(this);
        ivMessage.setOnClickListener(this);
        ivPhone.setOnClickListener(this);
        tvData.setOnClickListener(this);
ivAvatar.setOnClickListener(this);
    }


    void initData() {
      /*  GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false);
        rvMineBuilding.setLayoutManager(gridLayoutManager);
        buildingAdapter = new MineBuildingAdapter(this);
        rvMineBuilding.setAdapter(buildingAdapter);*/

        StaggeredGridLayoutManager staggeredLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rvRecommend.setLayoutManager(staggeredLayoutManager);
        rvRecommend.setPadding(0,0,0,0);
        rvRecommend.addItemDecoration(new SpacesItemDecoration(4,2,false));
        adapter = new VideoPublishAdapter(this);
        rvRecommend.setAdapter(adapter);
        rvRecommend.setNestedScrollingEnabled(false);
        adapter.setMore(R.layout.view_recyclerview_more, this);
        adapter.setNoMore(R.layout.view_recyclerview_nomore);
        adapter.setError(R.layout.view_recyclerview_error);
        getVideoPublisherInfo(userid);
        pageno = 1;
        getPublishVideos(userid, pageno);
        adapter.setOnItemClickListener(new XMBaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // 开始播放
                SimpleVideo simpleVideo = adapter.getItem(position);
                VideoPlayActivity.startActivity(PersonalCenterActivity.this, simpleVideo.getVideoid(), simpleVideo.getFrontcover());
            }
        });
    }


    @Override
    public void fillData(BaseResponse data, int flag) {
        switch (flag) {
            case FangConstants.VIDEO_PUBLISHER_INFO:
                if (0 == data.getReturnValue()) {
                    ResGetVideoPublisherInfo publisherInfo = (ResGetVideoPublisherInfo) data.getReturnData();
                    if (publisherInfo != null) {
                        faceUrl=publisherInfo.getAvatar();

                        ImageLoader.loadTransformImage(this, publisherInfo.getThumbnail(), R.drawable.icon_man, R.drawable.icon_man, ivAvatar, 0);
                        nickName = publisherInfo.getUsername();
                        headPic = publisherInfo.getAvatar();
                        tvTitle.setText(nickName + "的房产视频小站");
                        tvNickname.setText(nickName);
                        // number = publisherInfo.getMobile();
                        attentionCount.setText(publisherInfo.getAttentions() + "");
                        fansCount.setText(publisherInfo.getFans() + "");
                        favoriteCount.setText(publisherInfo.getFavourite() + "");
                        String signature= publisherInfo.getSignature();
                        if (signature==null||"".equals(signature)){
                            tvSignature.setText("可以在此介绍下自己，让大家更了解你。");
                            tvSignature.setTextColor(ContextCompat.getColor(this,R.color.cl_999999));
                        }else{
                            tvSignature.setText(signature);
                            tvSignature.setTextColor(ContextCompat.getColor(this,R.color.cl_555555));
                        }
                        if (isu == 1) {//自己
                            tvAttention.setVisibility(View.GONE);
                            tvData.setVisibility(View.VISIBLE);
                            ivMessage.setVisibility(View.GONE);
                            ivPhone.setVisibility(View.GONE);
                        } else {
                            ivMessage.setVisibility(View.VISIBLE);
                            tvAttention.setVisibility(View.VISIBLE);
                            tvData.setVisibility(View.GONE);
                            number = publisherInfo.getVisiblemobile();
                            if (publisherInfo.getIsattention()) {
                                tvAttention.setText("已关注");
                                tvAttention.setBackground(ContextCompat.getDrawable(PersonalCenterActivity.this, R.drawable.shape_mine_gray_bg));
                                isAttention = 0;
                            } else {
                                tvAttention.setText("关注");
                                tvAttention.setBackground(ContextCompat.getDrawable(PersonalCenterActivity.this, R.drawable.shape_mine_red_bg));
                                isAttention = 1;
                            }
                            if (number != null && number != "") {
                                ivPhone.setVisibility(View.VISIBLE);
                            } else {
                                ivPhone.setVisibility(View.GONE);
                            }

                        }
                      /*  if (isAgent) {
                          //  ivCertification.setVisibility(View.VISIBLE);
                            llBuilding.setVisibility(View.VISIBLE);
                           // ivMessage.setVisibility(View.VISIBLE);
                            if (publisherInfo.getIsrealname()) {
                                tvIdCertification.setVisibility(View.VISIBLE);
                            } else {
                                tvIdCertification.setVisibility(View.GONE);

                            }
                            if (publisherInfo.getIsprofessional()) {
                                tvProfession.setVisibility(View.VISIBLE);
                            } else {
                                tvProfession.setVisibility(View.GONE);

                            }
                            if (publisherInfo.getIsagency()) {
                                tvMessage.setVisibility(View.VISIBLE);
                            } else {
                                tvMessage.setVisibility(View.GONE);

                            }
                        } else {
                          //  ivCertification.setVisibility(View.GONE);
                            tvIdCertification.setVisibility(View.GONE);
                            tvProfession.setVisibility(View.GONE);
                            tvMessage.setVisibility(View.GONE);
                            ivPhone.setVisibility(View.GONE);
                            llBuilding.setVisibility(View.GONE);
                        }*/
                    }
                }
                break;

            case FangConstants.PUBLISHER_VIDEOS:
                if (0 == data.getReturnValue()) {
                    ResGetPublishVideos publishVideos = (ResGetPublishVideos) data.getReturnData();
                    if (publishVideos != null) {
                        List<SimpleVideo> simpleVideos = publishVideos.getVideolist();
                        if (simpleVideos != null && simpleVideos.size() > 0) {
                            if (pageno <= publishVideos.getTotalpage()) {
                                adapter.addAll(simpleVideos);
                                adapter.notifyDataSetChanged();
                                tvVideoCount.setText("Ta的视频（" + publishVideos.getAmount() + "）");
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

        }
    }

    @Override
    public void showFailedView(int flag) {

    }

    Dialog dialog;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_share:
                ShareDialog dialog = new ShareDialog();
                String SHARE_TITLE;
                String SHARE_SUMMARY;
                if (isAgent) {
                    String buildingsName = "";
                    if (resGetBuildings != null) {
                        for (int i = 0; i < resGetBuildings.size(); i++) {
                            buildingsName = buildingsName + "," + resGetBuildings.get(i).getBuildingname();
                        }
                    }
                    SHARE_TITLE = nickName + "的房产视频小站";
                    SHARE_SUMMARY = "壹眼房置业顾问：" + nickName + "，主要销售楼盘:" + buildingsName + "，更多视频等资料欢迎光临小站。";
                } else {
                    SHARE_TITLE = nickName + "的房产视频小站";
                    SHARE_SUMMARY = nickName + "的房产视频，更多视频等资料欢迎光临小站。";
                }

                dialog.showShareboard(this, mShareAction, ShareDialog.SHARE_VIDEO, FangConstants.USER + userid + "&isu=" + isu, SHARE_TITLE, SHARE_SUMMARY, headPic);
                break;
            case R.id.iv_avatar:
                ImageViewActivity.startActivity(this, faceUrl);
                break;
            case R.id.tv_attention:
                attentionUser(isAttention);
                break;
            case R.id.iv_message:
                ChatActivity.navToChat(this, userid + "", TIMConversationType.C2C);
                break;
            case R.id.iv_phone:
                if (number != null) {
                    DialogManager.showSelectDialog(PersonalCenterActivity.this, number, R.string.call, R.string.cancel, false, new DialogManager.DialogListener() {
                        @Override
                        public void onPositive() {
                            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));
                            if (ActivityCompat.checkSelfPermission(PersonalCenterActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                return;
                            }
                            startActivity(intent);
                        }

                        @Override
                        public void onNegative() {

                        }
                    });

                }
                break;
            case R.id.tv_data:
                UserinfoDetailActivity.startActivity(this);

                break;
     /*       case R.id.iv_footer:
                if (resGetBuildings != null && resGetBuildings.size() > 0) {
                    if (isUnfold) {
                        isUnfold = false;
                        buildingAdapter.setData(resGetBuildings);
                        buildingAdapter.notifyDataSetChanged();
                        ivFooter.setImageDrawable(ContextCompat.getDrawable(PersonalCenterActivity.this, R.drawable.grzy_icon_shouqi));
                    } else {
                        isUnfold = true;
                        buildingAdapter.setData(resGetBuildings.subList(0, 2));
                        buildingAdapter.notifyDataSetChanged();
                        ivFooter.setImageDrawable(ContextCompat.getDrawable(PersonalCenterActivity.this, R.drawable.grzy_icon_zhankai));
                    }
                }
                break;*/
        }
    }

    @Override
    public void onLoadMore() {
        pageno++;
        //   LogUtil.e("+++++++++++++++++++++"+pageno);
        getPublishVideos(userid, pageno);
    }


    private class OnPublisherAttentionListener implements BaseListener {
        @Override
        public void onSuccess(BaseResponse data, int flag) {
            if (0 == data.getReturnValue()) {
                //ToastUtils.show(PersonalCenterActivity.this, "操作成功");
                Intent intent = new Intent();
                intent.putExtra("attention", isAttention);

                if (isAttention == 0) {
                    isAttention = 1;
                    tvAttention.setText("关注");
                    tvAttention.setBackground(ContextCompat.getDrawable(PersonalCenterActivity.this, R.drawable.shape_mine_red_bg));
                    ToastUtils.show(PersonalCenterActivity.this, "取消关注成功");
                } else {
                    isAttention = 0;
                    tvAttention.setText("已关注");
                    tvAttention.setBackground(ContextCompat.getDrawable(PersonalCenterActivity.this, R.drawable.shape_mine_gray_bg));
                    ToastUtils.show(PersonalCenterActivity.this, "关注成功");
                }

                setResult(100, intent);
            } else {
                ToastUtils.show(PersonalCenterActivity.this, "关注失败：" + data.getReturnMsg());
                LogUtil.e("hition==", data.getReturnMsg() + ":" + data.getReturnValue());
            }
        }

        @Override
        public void onFailed(Throwable e, int flag) {

        }
    }

}
