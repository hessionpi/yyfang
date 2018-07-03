package com.yiyanf.fang.entity;


import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * 版权：壹眼房 版权所有
 *
 * 作者：hition
 *
 * 创建时间：2018/1/12
 *
 * 功能描述：realm数据库对象，主要用于存储本地上传失败的视频对象
 *
 * 修订记录：
 */

public class Video extends RealmObject {

    @PrimaryKey
    private long videoid;

    // 视频标题
    private String title;
    // 视频文件路径
    private String videoPath;
    // 视频封面
    private String coverPath;
    // 封面宽
    private int coverwidth;
    // 封面高
    private int coverheight;
    // 发布者用户id
    private String publisherid;
    // 发布者昵称
    private String publishername;
    // 发布者头像
    private String publisheravatar;

    // 视频文件大小
    /*private long fileSize;

    // 视频时长
    private long duration;*/

    //创建时间
    private long createTime;

    // 上传视频所需要的签名
    private String signature;

    public Video(){}

    public Video(long videoid, String title) {
        this.videoid = videoid;
        this.title = title;
    }

    public long getVideoid() {
        return videoid;
    }

    public void setVideoid(long videoid) {
        this.videoid = videoid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public String getCoverPath() {
        return coverPath;
    }

    public void setCoverPath(String coverPath) {
        this.coverPath = coverPath;
    }

    /*public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }*/

    public int getCoverwidth() {
        return coverwidth;
    }

    public void setCoverwidth(int coverwidth) {
        this.coverwidth = coverwidth;
    }

    public int getCoverheight() {
        return coverheight;
    }

    public void setCoverheight(int coverheight) {
        this.coverheight = coverheight;
    }

    public String getPublisherid() {
        return publisherid;
    }

    public void setPublisherid(String publisherid) {
        this.publisherid = publisherid;
    }

    public String getPublishername() {
        return publishername;
    }

    public void setPublishername(String publishername) {
        this.publishername = publishername;
    }

    public String getPublisheravatar() {
        return publisheravatar;
    }

    public void setPublisheravatar(String publisheravatar) {
        this.publisheravatar = publisheravatar;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
