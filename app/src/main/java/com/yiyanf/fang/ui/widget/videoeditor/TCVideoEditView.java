package com.yiyanf.fang.ui.widget.videoeditor;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tencent.rtmp.TXLog;
import com.tencent.ugc.TXVideoEditConstants;
import com.yiyanf.fang.R;
import com.yiyanf.fang.util.DateUtil;
import com.yiyanf.fang.util.LogUtil;

import butterknife.ButterKnife;

import static com.yiyanf.fang.ui.widget.videoeditor.RangeSlider.TYPE_LEFT;

public class TCVideoEditView extends RelativeLayout implements RangeSlider.OnRangeChangeListener {


    private String TAG = TCVideoEditView.class.getSimpleName();

    private Context mContext;

    private TextView mTvTip;
    private RecyclerView mRecyclerView;
    private RangeSlider mRangeSlider;

    private long mVideoDuration;
    private long mVideoStartPos;
    private long mVideoEndPos;

    private TCVideoEditerAdapter mAdapter;
    int lastItemPosition;
    int firstItemPosition;
    private OnCutChangeListener mRangeChangeListener;

    public TCVideoEditView(Context context) {
        super(context);

        init(context);
    }

    public TCVideoEditView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    public TCVideoEditView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        init(context);
    }
public void setChildrenEnabled(boolean enabled){
    mRangeSlider.setEnabled(enabled);
}
    private void init(Context context) {
        mContext = context;

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_edit_view, this, true);

        mTvTip = ButterKnife.findById(view, R.id.tv_tip); //(TextView); findViewById(R.id.tv_tip);
        mRangeSlider = ButterKnife.findById(view, R.id.range_slider); //(TextView); findViewById(R.id.tv_tip);
        mRecyclerView = ButterKnife.findById(view, R.id.recycler_view); //(TextView); findViewById(R.id.tv_tip);

        // mRangeSlider = (RangeSlider) findViewById(R.id.range_slider);
        mRangeSlider.setRangeChangeListener(this);

        // mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(manager);

        mAdapter = new TCVideoEditerAdapter(mContext, mVideoDuration);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //获取最后一个可见view的位置
                lastItemPosition = manager.findLastVisibleItemPosition();
                //获取第一个可见view的位置
                firstItemPosition = manager.findFirstVisibleItemPosition();
                LogUtil.e(firstItemPosition + "++++++++++++++++++++++++++++++++++++" + lastItemPosition);
                mVideoStartPos = firstItemPosition * 18000;
                    mVideoEndPos = mVideoStartPos + maxTime;

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

            }
        });


    }

    /**
     * 设置裁剪Listener
     *
     * @param listener
     */
    public void setCutChangeListener(OnCutChangeListener listener) {
        mRangeChangeListener = listener;

    }

    public int getSegmentFrom() {
        return (int) mVideoStartPos;
    }

    public int getSegmentTo() {
        return (int) mVideoEndPos;
    }
    int maxTime;
    public void setMediaFileInfo(TXVideoEditConstants.TXVideoInfo videoInfo,int minTime,int maxTime) {
        if (videoInfo == null) {
            return;
        }
       this.maxTime=maxTime;
        mVideoDuration = videoInfo.duration;
        mRangeSlider.setDuration(videoInfo.duration,minTime,maxTime);
        mVideoStartPos = 0;
        long duration=0;
        if (mVideoDuration<=maxTime){
            duration=mVideoDuration;
        }else{
            duration=maxTime;
        }
        mVideoEndPos = duration;
        mTvTip.setText("截取" + String.format(DateUtil.duration(duration)) + "s");
       // onKeyUp();
    }

    public void addBitmap(int index, Bitmap bitmap) {
        mAdapter.add(index, bitmap);
    }

    @Override
    public void onKeyDown(int type) {
        if (mRangeChangeListener != null) {
            mRangeChangeListener.onCutChangeKeyDown();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mAdapter != null) {
            TXLog.i(TAG, "onDetachedFromWindow: 清除所有bitmap");
            mAdapter.recycleAllBitmap();
        }
    }

    @Override
    public void onKeyUp(int type, int leftPinIndex, int rightPinIndex) {
        long duration=0;
        if (mVideoDuration<=maxTime){
            duration=mVideoDuration;
        }else{
            duration=maxTime;
        }
        int cutLeftTime = (int) (duration * leftPinIndex / 100); //ms
        int cutRightTime = (int) (duration * rightPinIndex / 100);
        LogUtil.e(leftPinIndex + "_________________________________________" + rightPinIndex);
        int leftTime = (int) (firstItemPosition * (maxTime/10)+(duration*leftPinIndex/100));
        int time = cutRightTime - cutLeftTime;
        if (type == TYPE_LEFT) {
            mVideoStartPos = leftTime;
        } else {
            mVideoEndPos = leftTime + time;
        }
        if (mRangeChangeListener != null) {
            mRangeChangeListener.onCutChangeKeyUp((int) mVideoStartPos, (int) mVideoEndPos);
        }

        mTvTip.setText("截取" + String.format(DateUtil.duration(time)) + "s");
    }

    public interface OnCutChangeListener {
        void onCutChangeKeyDown();

        void onCutChangeKeyUp(int startTime, int endTime);
    }


}
