package com.yiyanf.fang.db;

import com.yiyanf.fang.api.model.Building;
import com.yiyanf.fang.api.model.City;
import com.yiyanf.fang.entity.BuildingHistory;
import com.yiyanf.fang.entity.CityHistory;
import com.yiyanf.fang.entity.Video;
import java.util.List;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * 版权：壹眼房 版权所有
 *
 * 作者：hition
 *
 * 创建时间：2018/1/12
 *
 * 功能描述：数据库管理类，主要用于对数据的增、删、改、查操作
 *
 * 修订记录：
 */

public class DBManager {

    /*private static DBManager instnace;

    public static DBManager getInstance() {
        if (null == instnace) {
            synchronized (DBManager.class) {
                if (null == instnace) {
                    instnace = new DBManager();
                }
            }
        }
        return instnace;
    }*/

    private Realm mRealm;

    public DBManager() {
        mRealm = Realm.getDefaultInstance();
    }

    /**
     * 关闭数据库
     */
    public void close(){
        mRealm.close();
    }

    /**
     * 插入上传失败视频
     * @param video         失败视频
     *
     */
    public Video addFailVideo(Video video) {
        video.setCreateTime(System.currentTimeMillis());
        mRealm.beginTransaction();
        Video mVideo = mRealm.copyToRealmOrUpdate(video);
        mRealm.commitTransaction();
        return mVideo;
    }

    /**
     * 查询所有失败视频列表
     */
    public List<Video> getFailVideos() {
        RealmResults<Video> stocks = mRealm.where(Video.class).findAllSorted("createTime", Sort.DESCENDING);
        mRealm.beginTransaction();
        List<Video> all = mRealm.copyFromRealm(stocks);
        mRealm.commitTransaction();
        return all;
    }

    /**
     * 删除上传失败视频
     */
    public void deleteFailVideo(long videoid) {
        Video video = mRealm.where(Video.class).equalTo("videoid", videoid).findFirst();
        if (null != video) {
            mRealm.beginTransaction();
            video.deleteFromRealm();
            mRealm.commitTransaction();
        }
    }

    /**
     * 保存选择城市历史
     */
    public CityHistory saveCityChooseHistory(City city){
        CityHistory history = new CityHistory(city.getCityid(),city.getCityname());
        history.setCreateTime(System.currentTimeMillis());
        mRealm.beginTransaction();
        CityHistory mCity = mRealm.copyToRealmOrUpdate(history);
        mRealm.commitTransaction();
        return mCity;
    }

    /**
     * 获取历史选择城市列表
     * @return  历史选择城市列表
     */
    public List<CityHistory> getCityChooseHistory() {
        RealmResults<CityHistory> citys = mRealm.where(CityHistory.class).findAllSorted("createTime", Sort.DESCENDING);
        mRealm.beginTransaction();
        List<CityHistory> all = mRealm.copyFromRealm(citys);
        mRealm.commitTransaction();
        return all;
    }

    /**
     * 清空历史选择城市
     */
    public void deleteAllCityChooseHistory() {
        mRealm.beginTransaction();
        mRealm.deleteAll();
        mRealm.commitTransaction();
    }

    /**
     * 保存选择楼盘历史
     */
    public BuildingHistory saveBuildingChooseHistory(int cityId,Building building){
        BuildingHistory history = new BuildingHistory(building.getBuildingid(),building.getBuildingname());
        history.setCityId(cityId);
        history.setCreateTime(System.currentTimeMillis());
        mRealm.beginTransaction();
        BuildingHistory mBuilding = mRealm.copyToRealmOrUpdate(history);
        mRealm.commitTransaction();
        return mBuilding;
    }

    /**
     * 获取楼盘历史选择
     *
     */
    public List<BuildingHistory> getBuildingChooseHistory(int cityId) {
        RealmResults<BuildingHistory> citys = mRealm.where(BuildingHistory.class).equalTo("cityId", cityId).findAllSorted("createTime", Sort.DESCENDING);
        mRealm.beginTransaction();
        List<BuildingHistory> all = mRealm.copyFromRealm(citys);
        mRealm.commitTransaction();
        return all;
    }


}
