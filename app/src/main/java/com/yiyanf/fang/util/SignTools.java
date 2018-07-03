package com.yiyanf.fang.util;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * 用于API加密工具类
 *
 * Created by Hition on 2017/10/10.
 */

public final class SignTools {

    private static final String HMAC_ALGORITHM = "HmacSHA1";
    private static final String CONTENT_CHARSET = "UTF-8";
    private static final String m_strSecKey = "emR5eWZhbmdAODQ3MjY4ODY=";

    /**
     * 计算sig
     *
     * @return sign
     */
    /*public static String createSign(String platform,long currenttime, int expiretime, int random) {
        String sign;
        String contextStr = "";
        try {
            contextStr += "platform=" + java.net.URLEncoder.encode(platform, "utf8");
            contextStr += "&currenttime=" + currenttime;
            contextStr += "&expiretime=" + expiretime;
            contextStr += "&random=" + random;

            Mac mac = Mac.getInstance(HMAC_ALGORITHM);
            SecretKeySpec secretKey = new SecretKeySpec(m_strSecKey.getBytes(CONTENT_CHARSET), mac.getAlgorithm());
            mac.init(secretKey);
            byte[] hash = mac.doFinal(contextStr.getBytes(CONTENT_CHARSET));

            byte[] sigBuf = new byte[hash.length + contextStr.getBytes("utf8").length];
            System.arraycopy(hash, 0, sigBuf, 0, hash.length);
            System.arraycopy(contextStr.getBytes("utf8"), 0, sigBuf, hash.length, contextStr.getBytes("utf8").length);

            sign = new String(Base64.encode(sigBuf).getBytes());
            sign = sign.replace(" ", "").replace("\n", "").replace("\r", "");
        } catch (Exception e) {
            LogUtil.e(e.getMessage());
            return null;
        }
        return sign;
    }*/

    public static String createSign(String platform,long currenttime, int expiretime, int random) {
        String sign ;
        String contextStr = "";
        try {
            contextStr += "platform=" + java.net.URLEncoder.encode(platform, "utf8");
            contextStr += "&currenttime=" + currenttime;
            contextStr += "&expiretime=" + expiretime;
            contextStr += "&random=" + random;

            Mac mac = Mac.getInstance(HMAC_ALGORITHM);
            SecretKeySpec secretKey = new SecretKeySpec(m_strSecKey.getBytes(CONTENT_CHARSET), mac.getAlgorithm());
            mac.init(secretKey);
            byte[] hash = mac.doFinal(contextStr.getBytes(CONTENT_CHARSET));
            sign = new String(Base64.encode(hash).getBytes());
            sign = sign.replace(" ", "").replace("\n", "").replace("\r", "");
        } catch (Exception e) {
            LogUtil.e(e.getMessage());
            return null;
        }
        return sign;
    }





}
