package com.yiyanf.fang.entity;

import android.graphics.Bitmap;

import com.yiyanf.fang.api.model.SimpleVideo;

/**
 * Created by Hition on 2018/4/11.
 */

public class SimpleVideoInfo extends SimpleVideo {

    private Status status = Status.IDLE;

    private int progress;

    private Bitmap coverImg;

    // 上传视频所需要的签名
    private String signature;

    // 视频文件路径
    private String videoPath;

    public enum Status{
        START,UPLOADING,UPLOADING_AGAIN,IDLE,FAILED
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public Bitmap getCoverImg() {
        return coverImg;
    }

    public void setCoverImg(Bitmap coverImg) {
        this.coverImg = coverImg;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }
}
