package com.yiyanf.fang.util;

import android.text.TextUtils;

import java.util.ArrayList;

import static com.yiyanf.fang.FangConstants.FANG_SEARCH_HISTORY;

/**
 * Created by Administrator on 2017/12/18.
 */

public class SaveSearchHistoryUtils {
  static   ArrayList<String> historyList  = new ArrayList<>();
    /**
     * 保存搜索记录
     *
     * @param inputText 输入的历史记录
     */
    public static void saveSearchHistory(String inputText) {

        if (TextUtils.isEmpty(inputText)) {
            return;
        }
        String longHistory = (String) SPUtils.get(FANG_SEARCH_HISTORY, "");        //获取之前保存的历史记录
        String[] tmpHistory = longHistory.split(",");                            //逗号截取 保存在数组中
       // historyList = new ArrayList<>(Arrays.asList(tmpHistory));
        historyList.clear();
        for (int i=0;i<tmpHistory.length;i++){
            historyList.add(tmpHistory[i]);
        }
        //将改数组转换成ArrayList
        if (historyList.size() > 0) {
            //移除之前重复添加的元素
            for (int i = 0; i < historyList.size(); i++) {
                if (inputText.equals(historyList.get(i))) {
                    historyList.remove(i);
                    break;
                }
            }

            historyList.add(0, inputText);                           //将新输入的文字添加集合的第0位也就是最前面

            if (historyList.size() > 10) {
                historyList.remove(historyList.size() - 1);         //最多保存10条搜索记录 删除最早搜索的那一项
            }
            //逗号拼接
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < historyList.size(); i++) {
                sb.append(historyList.get(i) + ",");
            }
            //保存到sp
            SPUtils.put(FANG_SEARCH_HISTORY, sb.toString());
        } else {
            //之前未添加过
            SPUtils.put(FANG_SEARCH_HISTORY, inputText + ",");
        }
    }

    public static ArrayList<String> getHistoryList() {
        historyList.clear();
        String longHistory = (String) SPUtils.get(FANG_SEARCH_HISTORY, "");        //获取之前保存的历史记录
        String[] tmpHistory = longHistory.split(",");                            //逗号截取 保存在数组中
        for (int i=0;i<tmpHistory.length;i++){
            historyList.add(tmpHistory[i]);
        }
        return historyList;
    }
    public static void clearHistorySearch(){
        SPUtils.remove(FANG_SEARCH_HISTORY);
        historyList.clear();

    }
}
