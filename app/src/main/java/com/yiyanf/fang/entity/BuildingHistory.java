package com.yiyanf.fang.entity;


import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * 版权：壹眼房 版权所有
 *
 * 作者：hition
 *
 * 创建时间：2018/3/26
 *
 * 功能描述：realm数据库对象，主要用于存储本地上传失败的视频对象
 *
 * 修订记录：
 */

public class BuildingHistory extends RealmObject implements Serializable{

    @PrimaryKey
    private int buildingid;

    // 楼盘名称
    private String buildingname;

    // 城市id
    private int cityId;

    //创建时间
    private long createTime;

    public BuildingHistory() { }

    public BuildingHistory(int buildingid, String buildingname) {
        this.buildingid = buildingid;
        this.buildingname = buildingname;
    }

    public int getBuildingid() {
        return buildingid;
    }

    public void setBuildingid(int buildingid) {
        this.buildingid = buildingid;
    }

    public String getBuildingname() {
        return buildingname;
    }

    public void setBuildingname(String buildingname) {
        this.buildingname = buildingname;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
}
