package com.yiyanf.fang.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.yiyanf.fang.R;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 直播结束，显示直播详情信息
 * <p>
 * Created by Hition on 2018/1/31.
 */

public class LiveFinishShowDetailActivity extends BaseActivity {


    @Bind(R.id.tv_time)
    TextView tvTime;
    @Bind(R.id.tv_members)
    TextView tvMembers;
    @Bind(R.id.tv_admires)
    TextView tvAdmires;
    @Bind(R.id.btn_cancel)
    Button btnCancel;


    private String liveTimeLength;
    private String heartCount;
    private String totalMemberCount;

    public static void startActivity(Context context, String timeLength, String heartCount, String totalMemberCount) {
        Intent intent = new Intent(context, LiveFinishShowDetailActivity.class);
        intent.putExtra("time", timeLength);
        intent.putExtra("heartCount", heartCount);
        intent.putExtra("totalMemberCount", totalMemberCount);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_finish_show_detail);
        ButterKnife.bind(this);
        parseIntent();
        initView();
    }

    private void parseIntent() {
        liveTimeLength = getIntent().getStringExtra("time");
        heartCount = getIntent().getStringExtra("heartCount");
        totalMemberCount = getIntent().getStringExtra("totalMemberCount");
    }

    private void initView() {
        tvTime.setText(liveTimeLength);
        tvAdmires.setText(heartCount);
        tvMembers.setText(totalMemberCount);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
