package com.yiyanf.fang.model.imp;

import com.yiyanf.fang.FangConstants;
import com.yiyanf.fang.api.ApiManager;
import com.yiyanf.fang.api.model.ResCityGroup;
import com.yiyanf.fang.api.model.ResGetAreas;
import com.yiyanf.fang.api.model.ResGetBuildingAdverList;
import com.yiyanf.fang.api.model.ResGetBuildings;
import com.yiyanf.fang.api.model.ResGetCityIdByName;
import com.yiyanf.fang.api.model.ResGetCounselorList;
import com.yiyanf.fang.api.model.ResGetPictureList;
import com.yiyanf.fang.api.model.ResGetRegions;
import com.yiyanf.fang.api.model.ResGetResBuilding;
import com.yiyanf.fang.api.model.ResGetResBuildingList;
import com.yiyanf.fang.api.model.ResGetResRegion;
import com.yiyanf.fang.api.model.ResGetResRegionList;
import com.yiyanf.fang.api.model.ResGetRoomTypeList;
import com.yiyanf.fang.api.model.ResGetShowroomFormat;
import com.yiyanf.fang.api.model.ResGetShowroomType;
import com.yiyanf.fang.entity.BaseResponse;
import com.yiyanf.fang.model.BaseListener;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/10/27.
 */

public class BuildingModel {
    private BaseListener listener;
    public BuildingModel(BaseListener listener) {
        this.listener = listener;
    }

    /**
     * 获取所有区域
     */
    public Subscription getAreas( ){
        Observable<BaseResponse<ResGetAreas>> observable = ApiManager.getInstance().getAreas();
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseResponse<ResGetAreas>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable t) {
                        listener.onFailed(t, FangConstants.GET_REGIONS);
                    }

                    @Override
                    public void onNext(BaseResponse<ResGetAreas> baseResponse) {
                        listener.onSuccess(baseResponse, FangConstants.GET_REGIONS);
                    }
                });
    }

    /**
     *
     * 获取城市列表
     */
    public Subscription getRegions(){
        Observable<BaseResponse<ResGetRegions>> observable = ApiManager.getInstance().getRegions();
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseResponse<ResGetRegions>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable t) {
                        listener.onFailed(t, FangConstants.GET_REGIONS);
                    }

                    @Override
                    public void onNext(BaseResponse<ResGetRegions> baseResponse) {
                        listener.onSuccess(baseResponse, FangConstants.GET_REGIONS);
                    }
                });
    }

    /**
     * 某一区域所有楼盘
     * @param cityId  区域id
     */
    public Subscription getBuildings(int cityId){
        Observable<BaseResponse<ResGetBuildings>> observable = ApiManager.getInstance().getBuildings(cityId);
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseResponse<ResGetBuildings>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable t) {
                        listener.onFailed(t, FangConstants.BUILDINGS);
                    }

                    @Override
                    public void onNext(BaseResponse<ResGetBuildings> baseResponse) {
                        listener.onSuccess(baseResponse, FangConstants.BUILDINGS);
                    }
                });
    }

    /**
     * 通过城市和省名字获取城市id
     *
     * @param provinceName                  省名
     * @param cityName                      城市名
     */
    public Subscription getCityIdByName(String provinceName,String cityName){
        Observable<BaseResponse<ResGetCityIdByName>> observable = ApiManager.getInstance().getCityIdByName(provinceName,cityName);
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseResponse<ResGetCityIdByName>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable t) {
                        listener.onFailed(t, FangConstants.GET_CITY_ID_BY_NAME);
                    }

                    @Override
                    public void onNext(BaseResponse<ResGetCityIdByName> baseResponse) {
                        listener.onSuccess(baseResponse, FangConstants.GET_CITY_ID_BY_NAME);
                    }
                });
    }

    /**
     * 某一区域精选楼盘
     * @param regionId  区域id
     */
    public Subscription getHotBuildingsByAreaId(int regionId, int showcount){
        Observable<BaseResponse<ResGetResBuildingList>> observable = ApiManager.getInstance().getHotBuildingsByAreaId(regionId,showcount);
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseResponse<ResGetResBuildingList>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable t) {
                        listener.onFailed(t, FangConstants.HOT_BUILDINGS_BY_AREAID);
                    }

                    @Override
                    public void onNext(BaseResponse<ResGetResBuildingList> baseResponse) {
                        listener.onSuccess(baseResponse, FangConstants.HOT_BUILDINGS_BY_AREAID);
                    }
                });
    }

    /**
     * 认证用户楼盘
     */
    public Subscription getResponsibleBuildings(){
        Observable<BaseResponse<ResGetBuildings>> observable = ApiManager.getInstance().getResponsibleBuildings();
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseResponse<ResGetBuildings>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable t) {
                        listener.onFailed(t, FangConstants.BUILDINGS);
                    }

                    @Override
                    public void onNext(BaseResponse<ResGetBuildings> baseResponse) {
                        listener.onSuccess(baseResponse, FangConstants.BUILDINGS);
                    }
                });
    }

    /**
     * 获取楼盘样板间户型
     * @param buildingId  楼盘id
     */
    public Subscription getShowroomType(int buildingId,final BaseListener listener){
        Observable<BaseResponse<ResGetShowroomType>> observable = ApiManager.getInstance().getShowroomType(buildingId);
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseResponse<ResGetShowroomType>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable t) {
                        listener.onFailed(t, FangConstants.DEFAULT);
                    }

                    @Override
                    public void onNext(BaseResponse<ResGetShowroomType> baseResponse) {
                        listener.onSuccess(baseResponse, FangConstants.DEFAULT);
                    }
                });
    }
    /**
     * 获取楼盘列表
     */
    public Subscription getResBuildingList(int ordercond, int regionId, int pageno, int pagesize) {
        Observable<BaseResponse<ResGetResBuildingList>> observable = ApiManager.getInstance().getResBuildingList(ordercond,regionId,pageno,pagesize);
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseResponse<ResGetResBuildingList>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable t) {
                        listener.onFailed(t, FangConstants.CHOICE_BUILDING);
                    }

                    @Override
                    public void onNext(BaseResponse<ResGetResBuildingList> baseResponse) {
                        listener.onSuccess(baseResponse, FangConstants.CHOICE_BUILDING);
                    }
                });
    }

    /**
     * 获取楼盘详情
     */
    public Subscription getResBuilding(int buildingId) {
        Observable<BaseResponse<ResGetResBuilding>> observable = ApiManager.getInstance().getResBuilding(buildingId);
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseResponse<ResGetResBuilding>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable t) {
                        listener.onFailed(t, FangConstants.BUILDING_DETAILS);
                    }

                    @Override
                    public void onNext(BaseResponse<ResGetResBuilding> baseResponse) {
                        listener.onSuccess(baseResponse, FangConstants.BUILDING_DETAILS);
                    }
                });
    }

    /**
     * 获取城市群列表
     */
    public Subscription getCityGroups() {
        Observable<BaseResponse<ResCityGroup>> observable = ApiManager.getInstance().getCityGroups();
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseResponse<ResCityGroup>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable t) {
                        listener.onFailed(t, FangConstants.CITY_GROUPS);
                    }

                    @Override
                    public void onNext(BaseResponse<ResCityGroup> baseResponse) {
                        listener.onSuccess(baseResponse, FangConstants.CITY_GROUPS);
                    }
                });
    }

    /**
     * 获取区域列表
     */
    public Subscription getResRegionList(int cityId,int sort) {
        Observable<BaseResponse<ResGetResRegionList>> observable = ApiManager.getInstance().getResRegionList(cityId,sort);
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseResponse<ResGetResRegionList>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable t) {
                        listener.onFailed(t, FangConstants.HOT_REGION);
                    }

                    @Override
                    public void onNext(BaseResponse<ResGetResRegionList> baseResponse) {
                        listener.onSuccess(baseResponse, FangConstants.HOT_REGION);
                    }
                });
    }

    /**
     * 获取区域详情
     */
    public Subscription getResRegion(int regionId) {
        Observable<BaseResponse<ResGetResRegion>> observable = ApiManager.getInstance().getResRegion(regionId);
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseResponse<ResGetResRegion>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable t) {
                        listener.onFailed(t, FangConstants.REGION_DETAILS);
                    }

                    @Override
                    public void onNext(BaseResponse<ResGetResRegion> baseResponse) {
                        listener.onSuccess(baseResponse, FangConstants.REGION_DETAILS);
                    }
                });
    }

    /**
     * 户型
     * @param buildingid
     * @param formatName
     * @param pageno
     * @param pagesize
     * @return
     */
    public Subscription getRoomTypeList(int buildingid, String formatName, int pageno, int pagesize) {
        Observable<BaseResponse<ResGetRoomTypeList>> observable = ApiManager.getInstance().getRoomTypeList(buildingid,formatName,pageno,pagesize);
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseResponse<ResGetRoomTypeList>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable t) {
                        listener.onFailed(t, FangConstants.ROOM_TYPE);
                    }

                    @Override
                    public void onNext(BaseResponse<ResGetRoomTypeList> baseResponse) {
                        listener.onSuccess(baseResponse, FangConstants.ROOM_TYPE);
                    }
                });
    }
    public Subscription getShowroomFormat(int buildingid) {
        Observable<BaseResponse<ResGetShowroomFormat>> observable = ApiManager.getInstance().getShowroomFormat(buildingid);
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseResponse<ResGetShowroomFormat>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable t) {
                        listener.onFailed(t, FangConstants.ROOM_FORMAT);
                    }

                    @Override
                    public void onNext(BaseResponse<ResGetShowroomFormat> baseResponse) {
                        listener.onSuccess(baseResponse, FangConstants.ROOM_FORMAT);
                    }
                });
    }

    /**
     *置业顾问
     * @return
     */

    public Subscription getCounselorList(int objectType ,int objectid, int pageno, int pagesize) {
        Observable<BaseResponse<ResGetCounselorList>> observable = ApiManager.getInstance().getCounselorList(objectType,objectid,pageno,pagesize);
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseResponse<ResGetCounselorList>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable t) {
                        listener.onFailed(t, FangConstants.COUNSELOR_LIST);
                    }

                    @Override
                    public void onNext(BaseResponse<ResGetCounselorList> baseResponse) {
                        listener.onSuccess(baseResponse, FangConstants.COUNSELOR_LIST);
                    }
                });
    }
    /**
     *图片列表
     * @return
     */

    public Subscription getPictureList(int areaidcond ,int buildingidcond, int housetypeid) {
        Observable<BaseResponse<ResGetPictureList>> observable = ApiManager.getInstance().getPictureList(areaidcond,buildingidcond,housetypeid);
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseResponse<ResGetPictureList>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable t) {
                        listener.onFailed(t, FangConstants.PICTURE_LIST);
                    }

                    @Override
                    public void onNext(BaseResponse<ResGetPictureList> baseResponse) {
                        listener.onSuccess(baseResponse, FangConstants.PICTURE_LIST);
                    }
                });
    }

    /**
     *图片列表
     * @return
     */
    public Subscription getBuildingAdverList() {
        Observable<BaseResponse<ResGetBuildingAdverList>> observable = ApiManager.getInstance().getBuildingAdverList();
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseResponse<ResGetBuildingAdverList>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable t) {
                        listener.onFailed(t, FangConstants.BUILDING_ADVER);
                    }

                    @Override
                    public void onNext(BaseResponse<ResGetBuildingAdverList> baseResponse) {
                        listener.onSuccess(baseResponse, FangConstants.BUILDING_ADVER);
                    }
                });
    }

    /**
     * 关注/取消关注
     * @param objectId          对象id
     * @param flag              1 关注    0 取消关注
     */
    public Subscription attention(String objectId,int objectType,int flag,final BaseListener listener){
        Observable<BaseResponse> observable = ApiManager.getInstance().attention(objectId,objectType,flag);
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable t) {
                        listener.onFailed(t, FangConstants.DEFAULT);
                    }

                    @Override
                    public void onNext(BaseResponse baseResponse) {
                        listener.onSuccess(baseResponse, FangConstants.DEFAULT);
                    }
                });
    }




}
