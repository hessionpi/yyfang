package com.yiyanf.fang.entity;


import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * 版权：壹眼房 版权所有
 *
 * 作者：hition
 *
 * 创建时间：2018/3/23
 *
 * 功能描述：realm数据库对象，主要用于存储本地上传失败的视频对象
 *
 * 修订记录：
 */

public class CityHistory extends RealmObject implements Serializable{

    @PrimaryKey
    private int cityid;
    // 城市名
    private String cityname;

    // 操作时间
    private long createTime;

    public CityHistory(){}

    public CityHistory(int cityid, String cityname) {
        this.cityid = cityid;
        this.cityname = cityname;
    }

    public int getCityid() {
        return cityid;
    }

    public void setCityid(int cityid) {
        this.cityid = cityid;
    }

    public String getCityname() {
        return cityname;
    }

    public void setCityname(String cityname) {
        this.cityname = cityname;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
}
