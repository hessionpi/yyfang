package com.yiyanf.fang.eventbus;

/**
 * 样板间event
 *
 * Created by Hition on 2017/12/18.
 */

public class ShowRoomEvent {

    private int buildingId;

    private String buildingName;

    private int areaId;

    private String areaName;

    private int categoryId;

    private String categoryName;

    private int showroomId;

    private String showroom;

    public ShowRoomEvent(int showroomId, String showroom) {
        this.showroomId = showroomId;
        this.showroom = showroom;
    }

    public int getShowroomId() {
        return showroomId;
    }

    public String getShowroom() {
        return showroom;
    }

    public int getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(int buildingId) {
        this.buildingId = buildingId;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public int getAreaId() {
        return areaId;
    }

    public void setAreaId(int areaId) {
        this.areaId = areaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
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
