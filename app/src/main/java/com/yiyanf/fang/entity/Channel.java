package com.yiyanf.fang.entity;


import java.io.Serializable;

/**
 * Created by Administrator on 2017/1/5 0005.
 */

public class Channel  implements Serializable {
    public static final int TYPE_MY = 1;
    public static final int TYPE_OTHER = 2;
    public static final int TYPE_MY_CHANNEL = 3;
    public static final int TYPE_OTHER_CHANNEL = 4;
    public String Title;
    public int TitleCode;
    public int itemType;


    public int getItemType() {
        return this.itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public Channel(String title, int titleCode) {
        this(TYPE_MY_CHANNEL, title, titleCode);
    }

    public Channel(int type, String title, int titleCode) {
        Title = title;
        TitleCode = titleCode;
        itemType = type;
    }
}
