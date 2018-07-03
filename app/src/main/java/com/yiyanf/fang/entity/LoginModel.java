package com.yiyanf.fang.entity;

import java.io.Serializable;

/**
 * 用户信息实体，用于序列化
 * <p>
 * Created by Hition on 2017/12/29.
 */

public class LoginModel implements Serializable {

    private String userid;
    // 壹眼房专属id
    private String fangId;
    // 0普通用户     1 经纪人用户
    private int role;
    // 用户头像原图地址
    private String headpic;
    // 用户头像，缩略图地址
    private String thumbnail;
    // 用户昵称
    private String nickname;
    // 用户签名，用于云通信
    private String usersign;
    // 用户手机号
    private String mobile;
    // 用户性别
    private int sex;
    // 所在公司
    private String company;
    // 个性签名
    private String signature;
    // 用户所在地区
    private String location;
    // 用户收藏总数
    private int favorites;
    // 用户总关注数
    private int attentions;
    // 用户总粉丝数
    private int fans;
    //认证状态
    private int certstatus;
    //是否已实名认证
    private int isIdCertification;
    //是否专业资格认证
    private int isProfession;
    //是否职业信息认证
    private int isMessage;
    // 视频上传最短时长
    private long videominduration;
    // 视频上传最大时长
    private long videomaxduration;
private boolean isOpenPhone;
    public LoginModel() { }

    public LoginModel(String userid, String headpic, String thumbnail, String nickname) {
        this.userid = userid;
        this.headpic = headpic;
        this.thumbnail = thumbnail;
        this.nickname = nickname;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public int getCertstatus() {
        return certstatus;
    }

    public void setCertstatus(int certstatus) {
        this.certstatus = certstatus;
    }

    public String getFangId() {
        return fangId;
    }

    public void setFangId(String fangId) {
        this.fangId = fangId;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getHeadpic() {
        return headpic;
    }

    public void setHeadpic(String headpic) {
        this.headpic = headpic;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUsersign() {
        return usersign;
    }

    public void setUsersign(String usersign) {
        this.usersign = usersign;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getFavorites() {
        return favorites;
    }

    public void setFavorites(int favorites) {
        this.favorites = favorites;
    }

    public int getAttentions() {
        return attentions;
    }

    public void setAttentions(int attentions) {
        this.attentions = attentions;
    }

    public int getFans() {
        return fans;
    }

    public void setFans(int fans) {
        this.fans = fans;
    }

    public int getIsIdCertification() {
        return isIdCertification;
    }

    public void setIsIdCertification(int isIdCertification) {
        this.isIdCertification = isIdCertification;
    }

    public int getIsProfession() {
        return isProfession;
    }

    public void setIsProfession(int isProfession) {
        this.isProfession = isProfession;
    }

    public int getIsMessage() {
        return isMessage;
    }

    public void setIsMessage(int isMessage) {
        this.isMessage = isMessage;
    }

    public long getVideominduration() {
        return videominduration;
    }

    public void setVideominduration(long videominduration) {
        this.videominduration = videominduration;
    }

    public long getVideomaxduration() {
        return videomaxduration;
    }

    public void setVideomaxduration(long videomaxduration) {
        this.videomaxduration = videomaxduration;
    }

    public boolean isOpenPhone() {
        return isOpenPhone;
    }

    public void setOpenPhone(boolean openPhone) {
        isOpenPhone = openPhone;
    }
}
