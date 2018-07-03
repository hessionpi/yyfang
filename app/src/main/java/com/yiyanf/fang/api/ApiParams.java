package com.yiyanf.fang.api;


import android.text.TextUtils;

import com.yiyanf.fang.FangConstants;
import com.yiyanf.fang.model.UserInfoCenter;
import com.yiyanf.fang.util.SignTools;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Random;

/**
 * 请求参数封装
 *
 * Created by Hition on 2017/10/10.
 */

public final class ApiParams extends JSONObject {

    public ApiParams with(String key, String value) throws JSONException {
        put(key, value);
        return this;
    }

    public ApiParams with(String key, int value) throws JSONException {
        put(key, value);
        return this;
    }

    public ApiParams with(String key, Object value) throws JSONException {
        put(key, value);
        return this;
    }

    public ApiParams withSign(String apiVerson) throws JSONException {
        if(TextUtils.isEmpty(apiVerson)){
            withNoSign();
        }else{
            withApiVersion(apiVerson);
        }

        put(FangConstants.USERID_KEY, UserInfoCenter.getInstance().getUserId());
        long currenttime = System.currentTimeMillis();
        int random = new Random(Integer.MAX_VALUE).nextInt();
        put(FangConstants.CURRENTTIME_KEY,currenttime);
        put(FangConstants.EXPIRETIME_KEY,FangConstants.EXPIRETIME);
        put(FangConstants.RANDOM_KEY,random);
        String sign = SignTools.createSign(FangConstants.PLATFORM_VALUE,currenttime,FangConstants.EXPIRETIME,random);
        put(FangConstants.SIGN_KEY,sign);
        return this;
    }

    public ApiParams withNoSign() throws JSONException {
        put(FangConstants.VERSION_KEY, FangConstants.API_VERSION_VALUE_DEFAULT);
        put(FangConstants.PLATFORM_KEY,FangConstants.PLATFORM_VALUE);
        return this;
    }

    public ApiParams withApiVersion(String apiVerson) throws JSONException {
        put(FangConstants.VERSION_KEY, apiVerson);
        put(FangConstants.PLATFORM_KEY,FangConstants.PLATFORM_VALUE);
        return this;
    }






}
