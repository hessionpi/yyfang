package com.yiyanf.fang.util;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Hition on 2017/12/19.
 */

public class StringUtil {

    /**
     * 关键字高亮显示
     *
     * @param context       上下文
     * @param text          需要显示的文字
     * @param target        需要高亮的关键字
     * @param colorResId   高亮颜色
     * @param start        头部增加高亮文字个数
     * @param end          尾部增加高亮文字个数
     * @return 处理完后的结果
     */
    public static SpannableString highlight(Context context, String text, String target,int colorResId, int start, int end) {
        SpannableString spannableString = new SpannableString(text);
        if(!TextUtils.isEmpty(target)){
            Pattern pattern = Pattern.compile(target);
            Matcher matcher = pattern.matcher(text);
            while (matcher.find()) {
                ForegroundColorSpan span = new ForegroundColorSpan(ContextCompat.getColor(context,colorResId));
                spannableString.setSpan(span, matcher.start() - start, matcher.end() + end,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return spannableString;
    }

}
