package com.yiyanf.fang.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yiyanf.fang.FangConstants;
import com.yiyanf.fang.R;
import com.yiyanf.fang.api.model.ResGetHotSearch;
import com.yiyanf.fang.api.model.ResGetSearchResult;
import com.yiyanf.fang.api.model.VODinfo;
import com.yiyanf.fang.entity.BaseResponse;
import com.yiyanf.fang.presenter.imp.SearchPresenter;
import com.yiyanf.fang.ui.adapter.HistorySearchAdapter;
import com.yiyanf.fang.ui.adapter.HotSearchAdapter;
import com.yiyanf.fang.ui.adapter.SearchVideoAdapter;
import com.yiyanf.fang.ui.adapter.recycleadapter.XMBaseAdapter;
import com.yiyanf.fang.ui.widget.ClearEditText;
import com.yiyanf.fang.ui.widget.GridDividerItemDecoration;
import com.yiyanf.fang.util.NetWorkUtil;
import com.yiyanf.fang.util.SaveSearchHistoryUtils;
import com.yiyanf.fang.util.StringUtil;
import com.yiyanf.fang.view.IView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SearchActivity extends BaseActivity implements View.OnClickListener, IView, XMBaseAdapter.OnLoadMoreListener {

    @Bind(R.id.cet_search)
    ClearEditText cetSearch;
    @Bind(R.id.tv_cancel)
    TextView tvCancel;
    @Bind(R.id.search_history)
    RecyclerView searchHistory;
    @Bind(R.id.search_hot)
    RecyclerView searchHot;
    @Bind(R.id.rv_search_video)
    RecyclerView rvSearchVideo;


    @Bind(R.id.ll_search_history)
    LinearLayout llSearchHistory;
    @Bind(R.id.ll_search_result)
    LinearLayout llSearchResult;
    @Bind(R.id.iv_search_delete)
    ImageView ivSearchDelete;
    @Bind(R.id.ll_history_search)
    LinearLayout llHistorySearch;
    @Bind(R.id.tv_search_result)
    TextView tvSearchResult;

    @Bind(R.id.history_search)
    LinearLayout historySearch;
    @Bind(R.id.search_empty)
    LinearLayout searchEmpty;
    @Bind(R.id.no_search_vedio)
    TextView noSearchVedio;
    HistorySearchAdapter historySearchAdapter;
    HotSearchAdapter hotSearchAdapter;
    SearchVideoAdapter searchVideoAdapter;

    SearchPresenter presenter;
    String input;
    List<VODinfo> fetchVideos;
    int page;
    List<String> historyList;
    ResGetHotSearch hotSearch;


    public static void startActivity(Context context) {
        Intent intent = new Intent(context, SearchActivity.class);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        presenter = new SearchPresenter(this);
        initView();
    }

    /**
     * 调用接口获取搜索结果
     */
    private void loadSearchResult() {
        if (NetWorkUtil.isNetworkConnected(this)) {
            presenter.getSearchResult(input, page, 10);
        } else {
            showToast(R.string.no_network);
        }
    }

    private void initView() {
        presenter.getHotSearch();
        tvCancel.setOnClickListener(this);
        ivSearchDelete.setOnClickListener(this);

        GridDividerItemDecoration itemDecoration = new GridDividerItemDecoration(1, ContextCompat.getColor(this, R.color.colorLine));
        searchHistory.addItemDecoration(itemDecoration);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        searchHistory.setLayoutManager(gridLayoutManager);
        historySearchAdapter = new HistorySearchAdapter(this);
        searchHistory.setAdapter(historySearchAdapter);
        historySearchAdapter.setOnItemClickListener(new XMBaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                llSearchHistory.setVisibility(View.GONE);
                input = cetSearch.getText().toString();
                SaveSearchHistoryUtils.saveSearchHistory(input);
                page = 1;
                loadSearchResult();
            }
        });
        historyList = SaveSearchHistoryUtils.getHistoryList();
        historyList.remove("");
        if (historyList != null && historyList.size() > 0) {
            llHistorySearch.setVisibility(View.VISIBLE);
            historySearchAdapter.setData(historyList);
        } else {
            llHistorySearch.setVisibility(View.GONE);
            historySearchAdapter.clear();
        }
        historySearchAdapter.setOnItemClickListener(new XMBaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                searchVideoAdapter.clear();
                llSearchHistory.setVisibility(View.GONE);
                input = historyList.get(position);
                cetSearch.setText(input);
                SaveSearchHistoryUtils.saveSearchHistory(input);
                page = 1;
                loadSearchResult();
                tvSearchResult.setText(input + "  相关视频");
            }
        });
        GridDividerItemDecoration itemDecoration2 = new GridDividerItemDecoration(1, ContextCompat.getColor(this, R.color.colorLine));
        searchHot.addItemDecoration(itemDecoration2);
        GridLayoutManager gridLayoutManager2 = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        searchHot.setLayoutManager(gridLayoutManager2);
        hotSearchAdapter = new HotSearchAdapter(this);
        searchHot.setAdapter(hotSearchAdapter);
        hotSearchAdapter.setOnItemClickListener(new XMBaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                searchVideoAdapter.clear();
                llSearchHistory.setVisibility(View.GONE);
                input = hotSearch.getHotsearchlist().get(position);
                cetSearch.setText(input);
                SaveSearchHistoryUtils.saveSearchHistory(input);
                page = 1;
                loadSearchResult();
                tvSearchResult.setText(input + "  相关视频");
            }
        });

        LinearLayoutManager layoutmanager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvSearchVideo.setLayoutManager(layoutmanager);
        searchVideoAdapter = new SearchVideoAdapter(this);
        rvSearchVideo.setAdapter(searchVideoAdapter);
        searchVideoAdapter.setMore(R.layout.view_recyclerview_more, this);
        searchVideoAdapter.setNoMore(R.layout.view_recyclerview_nomore);
        searchVideoAdapter.setError(R.layout.view_recyclerview_error);
        searchVideoAdapter.setOnItemClickListener(new XMBaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

            }
        });


        cetSearch.setOnEditorActionListener(new TextView.OnEditorActionListener()

    {
        @Override
        public boolean onEditorAction (TextView v,int actionId, KeyEvent event){
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            searchVideoAdapter.clear();
            llSearchHistory.setVisibility(View.GONE);
            input = cetSearch.getText().toString();

            SaveSearchHistoryUtils.saveSearchHistory(input);
            page = 1;
            loadSearchResult();
            tvSearchResult.setText(input + "  相关视频");
            return true;
        }
        return false;
    }
    });


}


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_search_delete:
                SaveSearchHistoryUtils.clearHistorySearch();
                historySearchAdapter.clear();
                historySearchAdapter.notifyDataSetChanged();
                llHistorySearch.setVisibility(View.GONE);
                break;
            case R.id.tv_cancel:
                finish();
                break;
        }
    }


    @Override
    public void fillData(BaseResponse data, int flag) {
        int returnValue = data.getReturnValue();
        switch (flag) {
            case FangConstants.HOT_REGION:
                if (FangConstants.RETURN_VALUE_OK == returnValue) {
                    hotSearch = (ResGetHotSearch) data.getReturnData();
                    if (hotSearch != null) {
                        List<String> hotsearchlist = hotSearch.getHotsearchlist();
                        if (hotsearchlist != null && hotsearchlist.size() > 0) {
                            hotSearchAdapter.setData(hotsearchlist);
                            hotSearchAdapter.notifyDataSetChanged();
                        }
                    }
                }
                break;
            case FangConstants.SEARCH_RESULT:
                if (FangConstants.RETURN_VALUE_OK == returnValue) {
                    ResGetSearchResult searchResult = (ResGetSearchResult) data.getReturnData();

                    if (searchResult != null) {
                        if (page <= searchResult.getTotalpage()) {
                            fetchVideos = searchResult.getVodlist();
                            if (fetchVideos != null && fetchVideos.size() > 0) {
                                searchVideoAdapter.addAll(fetchVideos);
                                // searchVideoAdapter.notifyDataSetChanged();
                            } else {
                                if (page == 1) {
                                    emptySearchVedio();
                                }
                            }
                        } else {
                            searchVideoAdapter.stopMore();
                        }
                    } else {
                        searchVideoAdapter.stopMore();
                        if (page == 1) {
                            emptySearchVedio();
                        }
                    }
                }else{
                    emptySearchVedio();
                }

                break;
        }
    }

    @Override
    public void showFailedView(int flag) {
        emptySearchVedio();
    }

    public static boolean isEmojiCharacter(char codePoint) {
        return !((codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA) || (codePoint == 0xD) || ((codePoint >= 0x20) && codePoint <= 0xD7FF)) || ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) || ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
    }

    private void emptySearchVedio() {
        llSearchHistory.setVisibility(View.VISIBLE);
        historySearch.setVisibility(View.GONE);
        searchEmpty.setVisibility(View.VISIBLE);
        String text = "抱歉，没有找到“" + input + " ”相关的视频";
        noSearchVedio.setText(text);
        SpannableString highlightText = StringUtil.highlight(this, text, input, R.color.red, 0, 0);
        noSearchVedio.setText(highlightText);
    }

    @Override
    public void onLoadMore() {
        page++;
        loadSearchResult();
    }
}
