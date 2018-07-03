package com.yiyanf.fang.entity;

/**
 * Created by Hition on 2017/12/19.
 */

public class LiveFilter {



    private int areaId;

    private String areaName;

    private int buildingId;

    private String buildingName;
    private int useridcond;
    private  int  livevideotype;
    private  int  sort;


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

    public int getLivevideotype() {
        return livevideotype;
    }

    public void setLivevideotype(int livevideotype) {
        this.livevideotype = livevideotype;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getUseridcond() {
        return useridcond;
    }

    public void setUseridcond(int useridcond) {
        this.useridcond = useridcond;
    }
}
