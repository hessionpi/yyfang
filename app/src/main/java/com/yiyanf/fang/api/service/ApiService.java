package com.yiyanf.fang.api.service;

import com.yiyanf.fang.api.model.ResAllLiveForecast;
import com.yiyanf.fang.api.model.ResAttachedArea;
import com.yiyanf.fang.api.model.ResAttachedBuilding;
import com.yiyanf.fang.api.model.ResAttentionVideos;
import com.yiyanf.fang.api.model.ResCheckVersion;
import com.yiyanf.fang.api.model.ResCityGroup;
import com.yiyanf.fang.api.model.ResFeaturedList;
import com.yiyanf.fang.api.model.ResFetchGroupMemberList;
import com.yiyanf.fang.api.model.ResFetchLiveList;
import com.yiyanf.fang.api.model.ResFetchVodList;
import com.yiyanf.fang.api.model.ResGetAreas;
import com.yiyanf.fang.api.model.ResGetAttentionList;
import com.yiyanf.fang.api.model.ResGetBuildingAdverList;
import com.yiyanf.fang.api.model.ResGetBuildings;
import com.yiyanf.fang.api.model.ResGetCOSSign;
import com.yiyanf.fang.api.model.ResGetCertificationData;
import com.yiyanf.fang.api.model.ResGetCityIdByName;
import com.yiyanf.fang.api.model.ResGetCounselorList;
import com.yiyanf.fang.api.model.ResGetDiscussList;
import com.yiyanf.fang.api.model.ResGetFansList;
import com.yiyanf.fang.api.model.ResGetHotSearch;
import com.yiyanf.fang.api.model.ResGetInteractiveMsgs;
import com.yiyanf.fang.api.model.ResGetIsCertificationUser;
import com.yiyanf.fang.api.model.ResGetLVBAddr;
import com.yiyanf.fang.api.model.ResGetLiveDetails;
import com.yiyanf.fang.api.model.ResGetLivePlay;
import com.yiyanf.fang.api.model.ResGetLoginUserinfo;
import com.yiyanf.fang.api.model.ResGetMyVideos;
import com.yiyanf.fang.api.model.ResGetNotifications;
import com.yiyanf.fang.api.model.ResGetPictureList;
import com.yiyanf.fang.api.model.ResGetPublishVideos;
import com.yiyanf.fang.api.model.ResGetRegions;
import com.yiyanf.fang.api.model.ResGetReplyList;
import com.yiyanf.fang.api.model.ResGetResBuilding;
import com.yiyanf.fang.api.model.ResGetResBuildingList;
import com.yiyanf.fang.api.model.ResGetResRegion;
import com.yiyanf.fang.api.model.ResGetResRegionList;
import com.yiyanf.fang.api.model.ResGetRoomTypeList;
import com.yiyanf.fang.api.model.ResGetSearchResult;
import com.yiyanf.fang.api.model.ResGetShowroomFormat;
import com.yiyanf.fang.api.model.ResGetShowroomType;
import com.yiyanf.fang.api.model.ResGetUserSign;
import com.yiyanf.fang.api.model.ResGetVODSign;
import com.yiyanf.fang.api.model.ResGetVideoDetails;
import com.yiyanf.fang.api.model.ResGetVideoPublisherInfo;
import com.yiyanf.fang.api.model.ResHomePage;
import com.yiyanf.fang.api.model.ResLatestLiveForecast;
import com.yiyanf.fang.api.model.ResLiveApplyList;
import com.yiyanf.fang.api.model.ResLogin;
import com.yiyanf.fang.api.model.ResMineLiveReservation;
import com.yiyanf.fang.api.model.ResPublishVOD;
import com.yiyanf.fang.api.model.ResPublishVideoCategory;
import com.yiyanf.fang.api.model.ResRecommendVideos;
import com.yiyanf.fang.api.model.ResRelatedVideos;
import com.yiyanf.fang.api.model.ResSponsorLive;
import com.yiyanf.fang.api.model.ResStartPlay;
import com.yiyanf.fang.api.model.ResVideoClassify;
import com.yiyanf.fang.entity.BaseResponse;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * API 请求类
 *
 * Created by Hition on 2017/9/19.
 */

public interface ApiService {

    @POST("interface.htmls")
    Observable<BaseResponse<ResHomePage>> getHomePage(@Body RequestBody requestBody);
    @POST("interface.htmls")
    Observable<BaseResponse<ResGetLiveDetails>> getLiveDetails(@Body RequestBody requestBody);
    @POST("interface.htmls")
    Observable<BaseResponse<ResGetLVBAddr>> requestLVBAddr(@Body RequestBody requestBody);
    @POST("interface.htmls")
    Observable<BaseResponse<ResCheckVersion>> checkVersion(@Body RequestBody requestBody);
    @POST("interface.htmls")
    Observable<BaseResponse> sendCode(@Body RequestBody requestBody);
    @POST("interface.htmls")
    Observable<BaseResponse<ResLogin>> register(@Body RequestBody requestBody);
    @POST("interface.htmls")
    Observable<BaseResponse<ResLogin>> login(@Body RequestBody requestBody);
    @POST("interface.htmls")
    Observable<BaseResponse<ResLogin>> updateUserinfo(@Body RequestBody requestBody);
    @POST("interface.htmls")
    Observable<BaseResponse<ResLogin>> findPassword(@Body RequestBody requestBody);
    @POST("interface.htmls")
    Observable<BaseResponse> changePassword(@Body RequestBody requestBody);
    @POST("interface.htmls")
    Observable<BaseResponse> iDCertification(@Body RequestBody requestBody);
    @POST("interface.htmls")
    Observable<BaseResponse> agentCertification(@Body RequestBody requestBody);
    @POST("interface.htmls")
    Observable<BaseResponse<ResGetLoginUserinfo>> getLoginUserinfo(@Body RequestBody requestBody);
    @POST("interface.htmls")
    Observable<BaseResponse<ResGetAttentionList>> getAttentionList(@Body RequestBody requestBody);
    @POST("interface.htmls")
    Observable<BaseResponse<ResGetFansList>> getFansList(@Body RequestBody requestBody);
    @POST("interface.htmls")
    Observable<BaseResponse<ResGetLivePlay>> getLivePlay(@Body RequestBody requestBody);
    @POST("interface.htmls")
    Observable<BaseResponse<ResGetCertificationData>> getCertificationData(@Body RequestBody requestBody);
    @POST("interface.htmls")
    Observable<BaseResponse<ResMineLiveReservation>> mineLiveReservation(@Body RequestBody requestBody);
    @POST("interface.htmls")
    Observable<BaseResponse<ResGetIsCertificationUser>> getIsCertificationUser(@Body RequestBody requestBody);
    @POST("interface.htmls")
    Observable<BaseResponse<ResSponsorLive>> sponsorLive(@Body RequestBody requestBody);
    @POST("interface.htmls")
    Observable<BaseResponse<ResLiveApplyList>> getLiveApplyList(@Body RequestBody requestBody);
    @POST("interface.htmls")
    Observable<BaseResponse> applyLive(@Body RequestBody requestBody);
    @POST("interface.htmls")
    Observable<BaseResponse> changeLiveStatus(@Body RequestBody requestBody);
    @POST("interface.htmls")
    Observable<BaseResponse<ResFetchLiveList>> fetchLiveList(@Body RequestBody requestBody);
    @POST("interface.htmls")
    Observable<BaseResponse> enterGroup(@Body RequestBody requestBody);
    @POST("interface.htmls")
    Observable<BaseResponse> quitGroup(@Body RequestBody requestBody);
    @POST("interface.htmls")
    Observable<BaseResponse<ResFetchGroupMemberList>> fetchGroupmemberlist(@Body RequestBody requestBody);
    @POST("interface.htmls")
    Observable<BaseResponse> addLikecount(@Body RequestBody requestBody);
    @POST("interface.htmls")
    Observable<BaseResponse<ResLatestLiveForecast>> getLatestLiveForecast(@Body RequestBody requestBody);
    @POST("interface.htmls")
    Observable<BaseResponse<ResAllLiveForecast>> getAllLiveForecast(@Body RequestBody requestBody);
    @POST("interface.htmls")
    Observable<BaseResponse> reserve(@Body RequestBody requestBody);
    @POST("interface.htmls")
    Observable<BaseResponse<ResFetchVodList>> getMineFavoriteVod(@Body RequestBody requestBody);
    @POST("interface.htmls")
    Observable<BaseResponse<ResGetCOSSign>> getCOSSign(@Body RequestBody requestBody);
    @POST("interface.htmls")
    Observable<BaseResponse<ResGetUserSign>> getUserSign(@Body RequestBody requestBody);
    @POST("interface.htmls")
    Observable<BaseResponse<ResGetVODSign>> getVODSign(@Body RequestBody requestBody);
    @POST("interface.htmls")
    Observable<BaseResponse<ResPublishVideoCategory>> publishVideoCategory(@Body RequestBody requestBody);
    @POST("interface.htmls")
    Observable<BaseResponse<ResGetShowroomType>> getShowroomType(@Body RequestBody requestBody);
    @POST("interface.htmls")
    Observable<BaseResponse<ResPublishVOD>> publishVOD(@Body RequestBody requestBody);
    @POST("interface.htmls")
    Observable<BaseResponse<ResGetVideoDetails>> getVideoDetails(@Body RequestBody requestBody);
    @POST("interface.htmls")
    Observable<BaseResponse> finishPublishVOD(@Body RequestBody requestBody);
    @POST("interface.htmls")
    Observable<BaseResponse<ResFetchVodList>> fetchVodList(@Body RequestBody requestBody);
    @POST("interface.htmls")
    Observable<BaseResponse<ResFeaturedList>> featuredList(@Body RequestBody requestBody);
    @POST("interface.htmls")
    Observable<BaseResponse<ResRelatedVideos>> relatedVideos(@Body RequestBody requestBody);
    @POST("interface.htmls")
    Observable<BaseResponse<ResAttachedBuilding>> attachedBuilding(@Body RequestBody requestBody);
    @POST("interface.htmls")
    Observable<BaseResponse<ResAttachedArea>> attachedArea(@Body RequestBody requestBody);
    @POST("interface.htmls")
    Observable<BaseResponse<ResGetNotifications>>  getNotifications(@Body RequestBody requestBody);
    @POST("interface.htmls")
    Observable<BaseResponse<ResGetInteractiveMsgs>>  getInteractiveMsgs(@Body RequestBody requestBody);
    @POST("interface.htmls")
    Observable<BaseResponse> attention(@Body RequestBody requestBody);
    @POST("interface.htmls")
    Observable<BaseResponse> favorite(@Body RequestBody requestBody);
    @POST("interface.htmls")
    Observable<BaseResponse> discuss(@Body RequestBody requestBody);
    @POST("interface.htmls")
    Observable<BaseResponse> reply(@Body RequestBody requestBody);
    @POST("interface.htmls")
    Observable<BaseResponse<ResGetDiscussList>> getDiscussList(@Body RequestBody requestBody);
    @POST("interface.htmls")
    Observable<BaseResponse<ResGetReplyList>> getReplyList(@Body RequestBody requestBody);
    @POST("interface.htmls")
    Observable<BaseResponse> deleteDiscuss(@Body RequestBody requestBody);
    @POST("interface.htmls")
    Observable<BaseResponse> deleteReply(@Body RequestBody requestBody);
    @POST("interface.htmls")
    Observable<BaseResponse<ResStartPlay>> startPlay(@Body RequestBody requestBody);
    @POST("interface.htmls")
    Observable<BaseResponse> stopPlay(@Body RequestBody requestBody);
    @POST("interface.htmls")
    Observable<BaseResponse<ResGetAreas>>  getAreas(@Body RequestBody requestBody);
    /*@POST("/interface.htmls")
    Observable<BaseResponse<ResGetBuildings>>  getBuildings(@Body RequestBody requestBody);*/
    @POST("interface.htmls")
    Observable<BaseResponse<ResGetBuildings>>  getResponsibleBuildings(@Body RequestBody requestBody);
    @POST("interface.htmls")
    Observable<BaseResponse<ResGetResBuildingList>>  getResBuildingList(@Body RequestBody requestBody);
    @POST("interface.htmls")
    Observable<BaseResponse<ResGetRoomTypeList>>  getRoomTypeList(@Body RequestBody requestBody);
    @POST("interface.htmls")
    Observable<BaseResponse<ResGetShowroomFormat>>  getShowroomFormat(@Body RequestBody requestBody);
    @POST("interface.htmls")
    Observable<BaseResponse<ResGetCounselorList>>  getCounselorList(@Body RequestBody requestBody);
    @POST("interface.htmls")
    Observable<BaseResponse<ResGetResBuilding>>  getResBuilding(@Body RequestBody requestBody);
    @POST("interface.htmls")
    Observable<BaseResponse<ResCityGroup>>  getCityGroups(@Body RequestBody requestBody);
    @POST("interface.htmls")
    Observable<BaseResponse<ResGetResRegionList>>  getResRegionList(@Body RequestBody requestBody);
    @POST("interface.htmls")
    Observable<BaseResponse<ResGetResRegion>>  getResRegion(@Body RequestBody requestBody);
    @POST("interface.htmls")
    Observable<BaseResponse<ResGetPictureList>>  getPictureList(@Body RequestBody requestBody);
    @POST("interface.htmls")
    Observable<BaseResponse<ResGetBuildingAdverList>>  getBuildingAdverList(@Body RequestBody requestBody);
    @POST("interface.htmls")
    Observable<BaseResponse<ResGetBuildingAdverList>>  getHotBuildingsByAreaId(@Body RequestBody requestBody);
    @POST("interface.htmls")
    Observable<BaseResponse<ResGetHotSearch>>  getHotSearch(@Body RequestBody requestBody);
    @POST("interface.htmls")
    Observable<BaseResponse<ResGetSearchResult>>  getSearchResult(@Body RequestBody requestBody);


    //************************ for API version 1.2 ***************
    @POST("interface.htmls")
    Observable<BaseResponse<ResRecommendVideos>> getRecommendVideos(@Body RequestBody requestBody);
    @POST("interface.htmls")
    Observable<BaseResponse<ResAttentionVideos>> getAttentionVideos(@Body RequestBody requestBody);
    @POST("interface.htmls")
    Observable<BaseResponse<ResVideoClassify>> getVideoClassification(@Body RequestBody requestBody);
    @POST("interface.htmls")
    Observable<BaseResponse<ResGetRegions>> getRegions(@Body RequestBody requestBody);
    @POST("interface.htmls")
    Observable<BaseResponse<ResGetBuildings>> getBuildings(@Body RequestBody requestBody);
    @POST("interface.htmls")
    Observable<BaseResponse<ResGetVideoPublisherInfo>> getVideoPublisherInfo(@Body RequestBody requestBody);
    @POST("interface.htmls")
    Observable<BaseResponse<ResGetPublishVideos>> getPublishVideos(@Body RequestBody requestBody);
    @POST("interface.htmls")
    Observable<BaseResponse<ResGetCityIdByName>> getCityIdByName(@Body RequestBody requestBody);
    @POST("interface.htmls")
    Observable<BaseResponse> makeShare(@Body RequestBody requestBody);

    //************************ for API version 1.3 ***************
    @POST("interface.htmls")
    Observable<BaseResponse> exposureMobile(@Body RequestBody requestBody);
  @POST("interface.htmls")
    Observable<BaseResponse> deleteVideo(@Body RequestBody requestBody);
    @POST("interface.htmls")
    Observable<BaseResponse<ResGetMyVideos>> getMyVideos(@Body RequestBody requestBody);
    //*********************** it's end  *****************
}
