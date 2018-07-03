package com.yiyanf.fang.entity;

/**
 * Created by Hition on 2017/12/19.
 */

public class VideoFilter {

    private int cityGroupId;

    private int areaId;

    private String areaName;

    private int buildingId;

    private String buildingName;

    private int housetypeId;

    private int categoryid;

    private int userid;
    public int getCityGroupId() {
        return cityGroupId;
    }

    public void setCityGroupId(int cityGroupId) {
        this.cityGroupId = cityGroupId;
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

    public int getHousetypeId() {
        return housetypeId;
    }

    public void setHousetypeId(int housetypeId) {
        this.housetypeId = housetypeId;
    }

    public int getCategoryid() {
        return categoryid;
    }

    public void setCategoryid(int categoryid) {
        this.categoryid = categoryid;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }
}
