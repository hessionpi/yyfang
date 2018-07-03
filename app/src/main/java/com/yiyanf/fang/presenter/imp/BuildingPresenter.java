package com.yiyanf.fang.presenter.imp;

import com.yiyanf.fang.entity.BaseResponse;
import com.yiyanf.fang.model.BaseListener;
import com.yiyanf.fang.model.imp.BuildingModel;
import com.yiyanf.fang.view.IView;

/**
 * Created by Administrator on 2017/10/27.
 */

public class BuildingPresenter extends BasePresenter implements BaseListener {

    private BuildingModel mModel;
    private IView mView;

    public BuildingPresenter(IView iView) {
        this.mView = iView;
        this.mModel = new BuildingModel(this);
    }
    /**
     * 所有区域
     */
    public void getAreas() {
        addSubscription(mModel.getAreas());
    }

    /**
     * 所有城市行政区
     */
    public void getRegions() {
        addSubscription(mModel.getRegions());
    }

    /**
     * 某一区域所有楼盘
     */
    public void getBuildings(int areaid) {
        addSubscription(mModel.getBuildings(areaid));
    }

    public void getCityIdByName(String provinceName,String cityName) {
        addSubscription(mModel.getCityIdByName(provinceName,cityName));
    }

    public void getResponsibleBuildings() {
        addSubscription(mModel.getResponsibleBuildings());
    }

    public void getShowroomType(int buildingId,BaseListener listener){
        addSubscription(mModel.getShowroomType(buildingId,listener));
    }

    public void getResBuildingList(int ordercond,int regionId, int pageno, int pagesize){
        addSubscription(mModel.getResBuildingList(ordercond,regionId,pageno,pagesize));
    }

    public void getResBuilding(int buildingid){
        addSubscription(mModel.getResBuilding(buildingid));
    }

    public void getCityGroups(){
        addSubscription(mModel.getCityGroups());
    }

    public void getResRegionList(int cityId,int sort){
        addSubscription(mModel.getResRegionList(cityId,sort));
    }

    public void getResRegion(int regionId){
        addSubscription(mModel.getResRegion(regionId));
    }

    public void getRoomTypeList(int buildingid, String formatName, int pageno, int pagesize){
        addSubscription(mModel.getRoomTypeList(buildingid,formatName,pageno,pagesize));
    }

    public void getShowroomFormat(int buildingid){
        addSubscription(mModel.getShowroomFormat(buildingid));
    }

    public void  getCounselorList(int objectType ,int objectid, int pageno, int pagesize){
        addSubscription(mModel.getCounselorList(objectType,objectid,pageno,pagesize));
    }

    public void attentionOrCancel(int objectType,String objectId,int flag,BaseListener listener){
        addSubscription(mModel.attention(objectId,objectType,flag,listener));
    }

    public void getPictureList(int areaidcond ,int buildingidcond, int housetypeid){
        addSubscription(mModel.getPictureList(areaidcond,buildingidcond,housetypeid));
    }

    public void getBuildingAdverList(){
        addSubscription(mModel.getBuildingAdverList());
    }
    public void getHotBuildingsByAreaId(int regionId, int showcount){
        addSubscription(mModel.getHotBuildingsByAreaId(regionId,showcount));
    }

    @Override
    public void onSuccess(BaseResponse data, int flag) {
        mView.fillData(data, flag);
    }

    @Override
    public void onFailed(Throwable e, int flag) {
        mView.showFailedView(flag);
    }
}
