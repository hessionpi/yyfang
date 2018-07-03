package com.yiyanf.fang.entity;

/**
 * Created by Hition on 2017/12/19.
 */

public enum VideoSort{

    SORT_DEFAULT(0),
    SORT_PUBLISH_TIME(1),
    SORT_PLAY_COUNT(2),
    SORT_DISCUSS(3);

    private int value;

    VideoSort(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
