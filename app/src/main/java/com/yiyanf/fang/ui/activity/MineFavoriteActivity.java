package com.yiyanf.fang.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.tencent.rtmp.TXLiveConstants;
import com.yiyanf.fang.FangConstants;
import com.yiyanf.fang.R;
import com.yiyanf.fang.api.model.ResFetchVodList;
import com.yiyanf.fang.api.model.VODinfo;
import com.yiyanf.fang.entity.BaseResponse;
import com.yiyanf.fang.model.BaseListener;
import com.yiyanf.fang.presenter.imp.VODPresenter;
import com.yiyanf.fang.presenter.imp.VideoPresenter;
import com.yiyanf.fang.ui.adapter.MineFavoriteAdapter;
import com.yiyanf.fang.ui.adapter.recycleadapter.XMBaseAdapter;
import com.yiyanf.fang.ui.widget.MyItemDecoration;
import com.yiyanf.fang.ui.widget.ToolbarView;
import com.yiyanf.fang.util.LogUtil;
import com.yiyanf.fang.util.NetWorkUtil;
import com.yiyanf.fang.view.IView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 我的收藏
 */
public class MineFavoriteActivity extends BaseActivity implements IView, XMBaseAdapter.OnLoadMoreListener {

    @Bind(R.id.titlebar)
    ToolbarView titlebar;
    @Bind(R.id.rv_mine_favorite)
    RecyclerView rvMineFavorite;
    MineFavoriteAdapter adapter;
    VODPresenter presenter;
    int pageno;
    long videoId;
    // private int favoriteAction;
    private VideoPresenter videoPresenter;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, MineFavoriteActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_favorite);
        ButterKnife.bind(this);
        presenter = new VODPresenter(this);
        videoPresenter = new VideoPresenter(this);
        initView();
    }

    private void loadData() {
        if (NetWorkUtil.isNetworkConnected(this)) {

            presenter.getMineFavoriteVod(pageno, FangConstants.FETCH_LIVE_PAGE_SIZE_DEFAULT);
        } else {
            showToast(R.string.no_network);
        }
    }

    ImageView ivFavorite;
    VODinfo voDinfo;

    private void initView() {
        // favoriteSP = FangApplication.getApplication().getSharedPreferences("favorite", Context.MODE_PRIVATE);
        // favoriteAction = 1;
        MyItemDecoration decoration = new MyItemDecoration(this, LinearLayoutManager.HORIZONTAL, 1, R.color.colorLine);
        rvMineFavorite.addItemDecoration(decoration);
        LinearLayoutManager yiyanLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvMineFavorite.setLayoutManager(yiyanLayoutManager);
        adapter = new MineFavoriteAdapter(this);
        rvMineFavorite.setAdapter(adapter);
        adapter.setMore(R.layout.view_recyclerview_more, this);
        adapter.setNoMore(R.layout.view_recyclerview_nomore);
        adapter.setError(R.layout.view_recyclerview_error);
        adapter.setOnItemClickListener(new XMBaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                VODinfo videoInfo = adapter.getItem(position);
                VideoPlayActivity.startActivity(MineFavoriteActivity.this, videoInfo.getVideoid(), videoInfo.getFrontcover());
            }
        });
        adapter.setFavoriteClickListener(new MineFavoriteAdapter.OnFavoriteClickListener() {
            @Override
            public void onFavoriteClick(ImageView iv_favorite, VODinfo data) {
                voDinfo = data;
                videoId = data.getVideoid();
                ivFavorite = iv_favorite;
                // if (favoriteAction == 0) {
                iv_favorite.setImageDrawable(ContextCompat.getDrawable(MineFavoriteActivity.this, R.drawable.ic_favorite));
                //  favoriteAction = 1;
              /*  } else {
                    iv_favorite.setImageDrawable(ContextCompat.getDrawable(MineFavoriteActivity.this, R.drawable.ic_favorite_not));
                    favoriteAction = 0;
                }*/
                videoPresenter.favorite(String.valueOf(videoId), 0, new OnFavoriteListener());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.clear();
        pageno = 1;
        loadData();
    }

    List<VODinfo> voDinfos;

    @Override
    public void fillData(BaseResponse data, int flag) {
        if (0 == data.getReturnValue()) {
            ResFetchVodList fetchVodList = (ResFetchVodList) data.getReturnData();

            if (fetchVodList != null) {
                int totalPage = fetchVodList.getTotalpage();
                if (pageno <= totalPage) {
                    voDinfos = fetchVodList.getVodlist();

                    if (null != voDinfos && !voDinfos.isEmpty()) {
                        adapter.addAll(voDinfos);
                    } else {
                        if (pageno == 1) {

                            rvMineFavorite.setVisibility(View.GONE);
                        }
                    }
                } else {
                    adapter.stopMore();
                }
            } else {
                if (pageno == 1) {
                    rvMineFavorite.setVisibility(View.GONE);
                }
                adapter.stopMore();
                LogUtil.e("hition", data.getReturnMsg() + data.getReturnValue());
            }
        } else {
            LogUtil.e("hition", data.getReturnMsg() + data.getReturnValue());
        }
    }

    @Override
    public void showFailedView(int flag) {

    }

    @Override
    public void onLoadMore() {
        pageno++;
        loadData();
    }

    private class OnFavoriteListener implements BaseListener {

        @Override
        public void onSuccess(BaseResponse data, int flag) {
            if (0 == data.getReturnValue()) {
                /*if (1 == favoriteAction) {
                    ivFavorite.setImageDrawable(ContextCompat.getDrawable(MineFavoriteActivity.this, R.drawable.ic_favorite));


                    //  ToastUtils.show(MineFavoriteActivity.this, "收藏成功");
                } else {*/
                // ToastUtils.show(MineFavoriteActivity.this, "取消收藏");
                //  ivFavorite.setImageDrawable(ContextCompat.getDrawable(MineFavoriteActivity.this, R.drawable.ic_favorite_not));
                //   presenter.getMineFavoriteVod(1, FangConstants.FETCH_LIVE_PAGE_SIZE_DEFAULT);
                adapter.remove(voDinfo);
                adapter.notifyDataSetChanged();
                voDinfos.remove(voDinfo);
                if (voDinfos.size() == 0) {
                    rvMineFavorite.setVisibility(View.GONE);
                }
                //}

               /* SharedPreferences.Editor favoriteEditor = favoriteSP.edit();
                favoriteEditor.putInt(String.valueOf(videoId), favoriteAction);
                favoriteEditor.commit();*/
            } else {
                LogUtil.e("hition==", "收藏失败啦 " + data.getReturnValue() + " " + data.getReturnMsg());
            }
        }

        @Override
        public void onFailed(Throwable e, int flag) {

        }
    }

}
