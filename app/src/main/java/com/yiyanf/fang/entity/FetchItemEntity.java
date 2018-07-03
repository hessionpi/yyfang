package com.yiyanf.fang.entity;

import java.io.Serializable;

/**
 * 直播 回放列表item 实体
 *
 * Created by Hition on 2017/10/20.
 */

public class FetchItemEntity implements Serializable {

    private long liveid;

    private String userid;

    private String groupid;

    private String playurl;

    private int videoWidth;

    private int videoHeight;

    private String shareurl;

    private long videoSize;

    // 直播标题
    private String title;

    // 直播描述
    private String description;

    // 用户类型    0 普通用户      1 已认证经纪人用户
    private int type;

    private String coverpic;

    private int areaId;

    private String area;

    private String building;

    private int buildingId;

    private int hasevent;

    private String eventid;

    private String nickname;

    private String headpic;

    private int attention;

    private int favorite;
    // 0 直播    1 回放
    private int flag;

    // 回放视频时长
    private long duration;

    // 直播中最大在线观看人数
    private int maxviewercount;

    // 回放视频评论人数
    private int discusscount;

    // 播放次数
    private int playtimes;

    // 回放视频文件id
    private long videoid;

    //  直播结束时间
    private long endTime;

    // 回放发布时间
    private String publishTime;
    //收藏次数
    private int  favoritecount;

    private int orientation;

    public long getLiveid() {
        return liveid;
    }

    public void setLiveid(long liveid) {
        this.liveid = liveid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public String getPlayurl() {
        return playurl;
    }

    public void setPlayurl(String playurl) {
        this.playurl = playurl;
    }

    public String getShareurl() {
        return shareurl;
    }

    public int getVideoWidth() {
        return videoWidth;
    }

    public void setVideoWidth(int videoWidth) {
        this.videoWidth = videoWidth;
    }

    public int getVideoHeight() {
        return videoHeight;
    }

    public void setVideoHeight(int videoHeight) {
        this.videoHeight = videoHeight;
    }

    public void setShareurl(String shareurl) {
        this.shareurl = shareurl;
    }

    public long getVideoSize() {
        return videoSize;
    }

    public void setVideoSize(long videoSize) {
        this.videoSize = videoSize;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCoverpic() {
        return coverpic;
    }

    public void setCoverpic(String coverpic) {
        this.coverpic = coverpic;
    }

    public int getAreaId() {
        return areaId;
    }

    public void setAreaId(int areaId) {
        this.areaId = areaId;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public int getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(int buildingId) {
        this.buildingId = buildingId;
    }

    public boolean isEvent() {
        return 1 == hasevent;
    }

    public void setHasevent(int hasevent) {
        this.hasevent = hasevent;
    }

    public String getEventid() {
        return eventid;
    }

    public void setEventid(String eventid) {
        this.eventid = eventid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHeadpic() {
        return headpic;
    }

    public void setHeadpic(String headpic) {
        this.headpic = headpic;
    }

    public boolean isAttention() {
        return 1 == attention;
    }

    public boolean isFavorite() {
        return 1 == favorite;
    }

    public void setFavorite(int favorite) {
        this.favorite = favorite;
    }

    public void setAttention(int attention) {
        this.attention = attention;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public int getMaxviewercount() {
        return maxviewercount;
    }

    public void setMaxviewercount(int maxviewercount) {
        this.maxviewercount = maxviewercount;
    }

    public int getDiscusscount() {
        return discusscount;
    }

    public void setDiscusscount(int discusscount) {
        this.discusscount = discusscount;
    }

    public int getPlaytimes() {
        return playtimes;
    }

    public void setPlaytimes(int playtimes) {
        this.playtimes = playtimes;
    }

    public long getVideoid() {
        return videoid;
    }

    public void setVideoid(long videoid) {
        this.videoid = videoid;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public int getFavoritecount() {
        return favoritecount;
    }

    public void setFavoritecount(int favoritecount) {
        this.favoritecount = favoritecount;
    }

    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }
}
