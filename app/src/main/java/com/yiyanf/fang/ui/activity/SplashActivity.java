package com.yiyanf.fang.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import com.yiyanf.fang.R;
import com.yiyanf.fang.util.SPUtils;

/**
 * Created by Hition on 2016/12/20.
 */

public class SplashActivity extends Activity  {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable(){
            public void run() {
                init();
            }
        }, 1200);
    }

    private void init() {
        boolean is_first = (boolean) SPUtils.get("is_first",true);
        if(is_first){
            //第一次进入，进入导航页，is_first改成false
            GuideActivity.startActivity(this);
            SPUtils.put( "is_first", false);
        }else{
            //第二次进入，进入主页
            MainActivity.startActivity(this);
        }

       // MainActivity.startActivity(this);
        finish();
    }

}