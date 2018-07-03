package com.yiyanf.fang.eventbus;

/**
 * 区域 event
 * Created by Hition on 2017/12/18.
 */
public class AreaEvent {

    private int areaId;

    private String areaName;

    private int categoryId;

    private String categoryName;

    public AreaEvent(int areaId,String areaName) {
        this.areaId = areaId;
        this.areaName = areaName;
    }

    public int getAreaId() {
        return areaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
