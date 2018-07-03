package com.yiyanf.fang.api;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.yiyanf.fang.FangApplication;
import com.yiyanf.fang.FangConstants;
import com.yiyanf.fang.api.model.ReqAgentCertification;
import com.yiyanf.fang.api.model.ReqCityGroup;
import com.yiyanf.fang.api.model.ReqFetchLiveList;
import com.yiyanf.fang.api.model.ReqFetchVodList;
import com.yiyanf.fang.api.model.ReqHeader;
import com.yiyanf.fang.api.model.ReqPublishVOD;
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
import com.yiyanf.fang.api.service.ApiService;
import com.yiyanf.fang.entity.BaseResponse;
import com.yiyanf.fang.entity.LiveFilter;
import com.yiyanf.fang.entity.VideoFilter;
import com.yiyanf.fang.entity.VideoSort;
import com.yiyanf.fang.model.UserInfoCenter;
import com.yiyanf.fang.util.NetWorkUtil;
import com.yiyanf.fang.util.SignTools;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

/**
 * API 管理类
 * <p>
 * Created by Hition on 2017/9/19.
 */
public class ApiManager {

    public static boolean mIsDevelopEnv = true;
    public static String REQUEST_URL_API = "";

    private static ApiManager instance;
    private Retrofit retrofit;
    private static final MediaType CONTENT_TYPE = MediaType.parse("application/json");

    public static void setDevEnv() {
        if (mIsDevelopEnv) {
            REQUEST_URL_API = FangConstants.SVR_POST_URL_DEVELOP + "/fang/";
        } else {
            REQUEST_URL_API = FangConstants.SVR_POST_URL_ONLINE + "/fang/";
        }
    }

    private ApiManager() {
        File cacheDir = FangApplication.getApplication().getExternalCacheDir();
        // 设置OkHttp请求数据时缓存
        File cacheFile = new File(cacheDir, FangConstants.CASH_PATH);
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 100);
        Interceptor interceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                boolean isConnect = NetWorkUtil.isNetworkConnected(FangApplication.getApplication());

                if (!isConnect) {
                    request = request.newBuilder()
                            .cacheControl(CacheControl.FORCE_CACHE)
                            .build();
                }

                Response response = chain.proceed(request);
                if (isConnect) {
                    int maxAge = 0;
                    // 有网络时 设置缓存超时时间0个小时
                    response.newBuilder()
                            .addHeader("Content-Type", "application/json;charset=UTF-8")
                            .addHeader("Cache-Control", "public, max-age=" + maxAge)
                            .removeHeader("Pragma")// 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                            .build();
                } else {
                    // 无网络时，设置超时为4周
                    int maxStale = 60 * 60 * 24 * 28;
                    response.newBuilder()
                            .addHeader("Content-Type", "application/json;charset=UTF-8")
                            .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                            .removeHeader("Pragma")
                            .build();
                }
                return response;
            }
        };

        OkHttpClient client = new OkHttpClient.Builder().cache(cache)
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(REQUEST_URL_API)
                //增加返回值为Gson的支持(以实体类返回)
                .addConverterFactory(GsonConverterFactory.create())
                //增加返回值为Oservable<T>的支持
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .build();
    }

    public static ApiManager getInstance() {
        if (instance == null) {
            synchronized (ApiManager.class) {
                if (instance == null) {
                    instance = new ApiManager();
                }
            }
        }
        return instance;
    }

    /**
     * 添加公共头json    apiversion      platform     sign 等
     *
     * @return JSONObject
     */
    private JSONObject putHeader(String apiVersion) throws JSONException {
        ApiParams headerJson = new ApiParams();
        headerJson.withSign(apiVersion);
        return headerJson;
    }

    private ReqHeader putHeaderByReq(String apiVersion){
        long currenttime = System.currentTimeMillis();
        int random = new Random(Integer.MAX_VALUE).nextInt();
        String sign = SignTools.createSign(FangConstants.PLATFORM_VALUE,currenttime,FangConstants.EXPIRETIME,random);

        ReqHeader reqheader = new ReqHeader();
        if(TextUtils.isEmpty(apiVersion)){
            reqheader.setApiversion(FangConstants.API_VERSION_VALUE_DEFAULT);
        }else{
            reqheader.setApiversion(apiVersion);
        }
        reqheader.setUserid(UserInfoCenter.getInstance().getUserId());
        reqheader.setPlatform(FangConstants.PLATFORM_VALUE);
        reqheader.setCurrenttime(currenttime);
        reqheader.setExpiretime(FangConstants.EXPIRETIME);
        reqheader.setRandom(random);
        reqheader.setSign(sign);
        return reqheader;
    }


    private JSONObject putUserinfo(String nickName, String avatar, String coverPic, String location) throws JSONException {
        ApiParams userinfoJson = new ApiParams();
        userinfoJson.with("nickname", nickName)
                .with("headpic", avatar)
                .with("frontcover", coverPic)
                .with("location", location);
        return userinfoJson;
    }

    /**
     *
     * 获取首页内容
     */
    public Observable<BaseResponse<ResHomePage>> getHomePage() {
        String content = "";
        try {
            ApiParams req = new ApiParams().with(FangConstants.ACTION_KEY, "HomePage")
                    .with(FangConstants.HEADER_KEY, putHeader(null));

            content = req.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(CONTENT_TYPE, content);
        ApiService service = retrofit.create(ApiService.class);
        return service.getHomePage(requestBody);
    }


    /**
     * 根据直播id 获取直播详细信息
     * @param liveId                直播id
     *
     */
    public Observable<BaseResponse<ResGetLiveDetails>> getLiveDetails(long liveId) {
        String content = "";
        try {
            ApiParams req = new ApiParams().with(FangConstants.ACTION_KEY, "GetLiveDetails")
                    .with(FangConstants.HEADER_KEY, putHeader(null))
                    .with("liveid",liveId);

            content = req.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(CONTENT_TYPE, content);
        ApiService service = retrofit.create(ApiService.class);
        return service.getLiveDetails(requestBody);
    }

    /**
     * 请求直播推流地址
     *
     * @param groupId  群组id
     * @param title    直播标题
     * @param nickName 主播昵称
     * @param avatar   主播头像
     * @param coverPic 封面
     * @param location 位置信息
     * @param liveOrientation  主播屏幕方向
     */
    public Observable<BaseResponse<ResGetLVBAddr>> getLVBAddress(String groupId, String title, String nickName, String avatar, String coverPic, String location,int liveOrientation) {
        String content = "";
        try {
            JSONObject userInfo = putUserinfo(nickName, avatar, coverPic, location);
            ApiParams req = new ApiParams().with(FangConstants.ACTION_KEY, "GetLVBAddr")
                    .with(FangConstants.HEADER_KEY, putHeader(null))
                    .with("groupid", groupId)
                    .with("title", title)
                    .with("userinfo", userInfo)
                    .with("orientation",liveOrientation);

            content = req.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(CONTENT_TYPE, content);
        ApiService service = retrofit.create(ApiService.class);
        return service.requestLVBAddr(requestBody);
    }

    /**
     * 设置主播在线状态
     *
     * @param status 状态  在线/离线
     */
    public Observable<BaseResponse> changeLiveStatus(long applyId,long liveId,String status) {
        String content = "";
        try {
            ApiParams req = new ApiParams().with(FangConstants.ACTION_KEY, "ChangeLiveStatus")
                    .with(FangConstants.HEADER_KEY, putHeader(null))
                    .with("applyid",applyId)
                    .with("liveid",liveId)
                    .with("status", status);

            content = req.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(CONTENT_TYPE, content);
        ApiService service = retrofit.create(ApiService.class);
        return service.changeLiveStatus(requestBody);
    }

    /**
     * 拉取直播列表
     * @param pageno        分页号
     */
    public Observable<BaseResponse<ResFetchLiveList>> fetchLiveList(LiveFilter filter, int pageno, int pagesize){
/*        String content = "";
        try {
            ApiParams req = new ApiParams().with(FangConstants.ACTION_KEY, "FetchLiveList")
                    .with(FangConstants.HEADER_KEY,putHeader(null))
                    .with("condition",condition)
                    .with("pageno", pageno)
                    .with("pagesize",pagesize);

            content = req.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(CONTENT_TYPE,content);
        ApiService service = retrofit.create(ApiService.class);
        return service.fetchLiveList(requestBody);*/


        ReqFetchLiveList request = new ReqFetchLiveList();
        request.setAction("FetchLiveList");
        request.setHeader(putHeaderByReq(null));

        if(null != filter){
            request.setAreaidcond(filter.getAreaId());
            request.setAreanamecond(filter.getAreaName());
            request.setBuildingidcond(filter.getBuildingId());
            request.setBuildingnamecond(filter.getBuildingName());
            request.setUseridcond(filter.getUseridcond());
            request.setLivevideotype(filter.getLivevideotype());
            request.setSort(filter.getSort());
        }

        request.setPageno(pageno);
        request.setPagesize(pagesize);

        Gson gson = new Gson();
        String content = gson.toJson(request);

        RequestBody requestBody = RequestBody.create(CONTENT_TYPE, content);
        ApiService service = retrofit.create(ApiService.class);
        return service.fetchLiveList(requestBody);
    }

    /**
     * 检查更新
     * @param packageName                   应用包名
     * @param versionCode                   版本号
     *
     */

    public Observable<BaseResponse<ResCheckVersion>> checkVersion(String packageName, int versionCode) {
        String content = "";
        try {
            ApiParams req = new ApiParams().with(FangConstants.ACTION_KEY, "CheckVersion")
                    .with(FangConstants.HEADER_KEY, putHeader(null))
                    .with("packagename",packageName )
                    .with("versioncode", versionCode);
            content = req.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(CONTENT_TYPE, content);
        ApiService service = retrofit.create(ApiService.class);
        return service.checkVersion(requestBody);
    }


    /**
     * 发送短信验证码
     *
     * @param mobile 手机号
     * @return Observable
     */
    public Observable<BaseResponse> sendCode(String mobile) {
        String content = "";
        try {
            ApiParams req = new ApiParams().with(FangConstants.ACTION_KEY, "SendCode")
                    .with(FangConstants.HEADER_KEY, putHeader(null))
                    .with("mobile", mobile);

            content = req.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(CONTENT_TYPE, content);
        ApiService service = retrofit.create(ApiService.class);
        return service.sendCode(requestBody);
    }

    /**
     * 新用户注册
     *
     * @param mobile   用户名
     * @param password 用户密码
     * @param sendcode 短信验证码
     * @return Observable<BaseResponse>
     */
    public Observable<BaseResponse<ResLogin>> register(String mobile, String password, String sendcode) {
        String content = "";
        try {
            ApiParams req = new ApiParams().with(FangConstants.ACTION_KEY, "Register")
                    .with(FangConstants.HEADER_KEY, putHeader(null))
                    .with("username", mobile)
                    .with("password", password)
                    .with("sendcode", sendcode);
            content = req.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(CONTENT_TYPE, content);
        ApiService service = retrofit.create(ApiService.class);
        return service.register(requestBody);
    }

    /**
     * 用户登录
     *
     * @param mobile   手机号
     * @param password 密码
     * @return Observable<BaseResponse>
     */
    public Observable<BaseResponse<ResLogin>> login(String mobile, String password) {
        String content = "";
        try {
            ApiParams req = new ApiParams().with(FangConstants.ACTION_KEY, "Login")
                    .with(FangConstants.HEADER_KEY, putHeader(null))
                    .with("username", mobile)
                    .with("password", password);
            content = req.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(CONTENT_TYPE, content);
        ApiService service = retrofit.create(ApiService.class);
        return service.login(requestBody);
    }

    /**
     * 忘记密码
     *
     * @param mobile   手机号
     * @param password 密码
     * @param sendcode 验证码
     * @return Observable<BaseResponse>
     */
    public Observable<BaseResponse<ResLogin>> findPassword(String mobile, String password, String sendcode) {
        String content = "";
        try {
            ApiParams req = new ApiParams().with(FangConstants.ACTION_KEY, "ResetPassword")
                    .with(FangConstants.HEADER_KEY, putHeader(null))
                    .with("mobile", mobile)
                    .with("newpwd", password)
                    .with("sendcode", sendcode);
            content = req.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(CONTENT_TYPE, content);
        ApiService service = retrofit.create(ApiService.class);
        return service.findPassword(requestBody);
    }

    /**
     * 用户完善资料
     *
     * @param headpic  头像地址
     * @param nickname 昵称
     * @param sex      性别
     * @return Observable<BaseResponse>
     */
    public Observable<BaseResponse<ResLogin>> updateUserinfo(String headpic, String nickname, int sex,String ecompany,String signature) {
        String content = "";
        try {
            ApiParams req = new ApiParams().with(FangConstants.ACTION_KEY, "UpdateUserinfo")
                    .with(FangConstants.HEADER_KEY, putHeader(null))
                    .with("headpic", headpic)
                    .with("nickname", nickname)
                    .with("sex", sex)
                    .with("company", ecompany)
                    .with("signature", signature);
            content = req.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(CONTENT_TYPE, content);
        ApiService service = retrofit.create(ApiService.class);
        return service.updateUserinfo(requestBody);
    }
    /**
     * 实名认证
     *
     * @return Observable<BaseResponse>
     */
    public Observable<BaseResponse> iDCertification(String idcard, String name ){
        String content = "";
        try {
            ApiParams req = new ApiParams().with(FangConstants.ACTION_KEY, "IdCertification")
                    .with(FangConstants.HEADER_KEY, putHeader(null))
                    .with("idcard", idcard)
                    .with("name", name);
            content = req.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(CONTENT_TYPE, content);
        ApiService service = retrofit.create(ApiService.class);
        return service.iDCertification(requestBody);
    }
    /**
     * 实名认证资料
     *
     * @return Observable<BaseResponse>
     */
    public Observable<BaseResponse> agentCertification(String headpic, String agentcompany,  ArrayList<Integer> buildings ,String certificateurl ,String visitcardurl){
        ReqAgentCertification reqAgentCertification = new ReqAgentCertification();
        reqAgentCertification.setAction("AgentCertification");
        reqAgentCertification.setHeader(putHeaderByReq(null));
        reqAgentCertification.setHeadpic(headpic);
        reqAgentCertification.setAgentcompany(agentcompany);
        reqAgentCertification.setBuildings(buildings);
        reqAgentCertification.setCertificateurl(certificateurl);
        reqAgentCertification.setVisitcardurl(visitcardurl);
        Gson gson = new Gson();
        String content = gson.toJson(reqAgentCertification);
        RequestBody requestBody = RequestBody.create(CONTENT_TYPE, content);
        ApiService service = retrofit.create(ApiService.class);
        return service.agentCertification(requestBody);

        /*String content = "";
        try {
            ApiParams req = new ApiParams().with(FangConstants.ACTION_KEY, "AgentCertification")
                    .with(FangConstants.HEADER_KEY, putHeader(null))
                    .with("userid", userid)
                    .with("headpic", headpic)
                    .with("agentcompany", agentcompany)
                    .with("buildings", buildings)
                    .with("certificateurl", certificateurl)
                    .with("visitcardurl", visitcardurl);
            content = req.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(CONTENT_TYPE, content);
        ApiService service = retrofit.create(ApiService.class);
        return service.agentCertification(requestBody);*/
    }

    /**
     * 获取登录用户资料信息
     *
     * @return Observable<BaseResponse>
     */
    public Observable<BaseResponse<ResGetLoginUserinfo>> getLoginUserinfo() {
        String content = "";
        try {
            ApiParams req = new ApiParams().with(FangConstants.ACTION_KEY, "GetLoginUserinfo")
                    .with(FangConstants.HEADER_KEY, putHeader(null));
            content = req.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(CONTENT_TYPE, content);
        ApiService service = retrofit.create(ApiService.class);
        return service.getLoginUserinfo(requestBody);
    }

    /**
     * 修改密码
     * @param old_pwd       旧密码
     * @param new_pwd       新密码
     * @return
     */
    public Observable<BaseResponse> changePassword(String old_pwd, String new_pwd) {
        String content = "";
        try {
            ApiParams req = new ApiParams().with(FangConstants.ACTION_KEY, "ChangePassword")
                    .with(FangConstants.HEADER_KEY, putHeader(null))
                    .with("oldpwd", old_pwd)
                    .with("newpwd", new_pwd);
            content = req.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(CONTENT_TYPE, content);
        ApiService service = retrofit.create(ApiService.class);
        return service.changePassword(requestBody);
    }
    /**
     * 我的关注列表
     * @return
     */
    public Observable<BaseResponse<ResGetAttentionList>> getAttentionList(int regionflag, int buildingflag, int userflag) {
        String content = "";
        try {
            ApiParams req = new ApiParams().with(FangConstants.ACTION_KEY, "GetAttentionList")
                    .with(FangConstants.HEADER_KEY, putHeader(null))
                    .with("regionflag", regionflag)
                    .with("buildingflag", buildingflag)
                    .with("userflag", userflag);
            content = req.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(CONTENT_TYPE, content);
        ApiService service = retrofit.create(ApiService.class);
        return service.getAttentionList(requestBody);
    }
    /**
     * 我的粉丝列表
     * @return
     */
    public Observable<BaseResponse<ResGetFansList>> getFansList(int pageno, int pagesize) {
        String content = "";
        try {
            ApiParams req = new ApiParams().with(FangConstants.ACTION_KEY, "GetFansList")
                    .with(FangConstants.HEADER_KEY, putHeader(null))
                    .with("pageno", pageno)
                    .with("pagesize",pagesize);
            content = req.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(CONTENT_TYPE, content);
        ApiService service = retrofit.create(ApiService.class);
        return service.getFansList(requestBody);
    }
    /**
     * 我的直播列表
     * @return
     */
    public Observable<BaseResponse<ResGetLivePlay>> getLivePlay(int pageno, int pagesize) {
        String content = "";
        try {
            ApiParams req = new ApiParams().with(FangConstants.ACTION_KEY, "GetLivePlay")
                    .with(FangConstants.HEADER_KEY, putHeader(null))
                    .with("pageno", pageno)
                    .with("pagesize",pagesize);
            content = req.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(CONTENT_TYPE, content);
        ApiService service = retrofit.create(ApiService.class);
        return service.getLivePlay(requestBody);
    }
    /**
     * 我的预约列表
     * @return
     */
    public Observable<BaseResponse<ResMineLiveReservation>> mineLiveReservation(int pageno, int pagesize) {
        String content = "";
        try {
            ApiParams req = new ApiParams().with(FangConstants.ACTION_KEY, "MineLiveReservation")
                    .with(FangConstants.HEADER_KEY, putHeader(null))
                    .with("pageno", pageno)
                    .with("pagesize",pagesize);
            content = req.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(CONTENT_TYPE, content);
        ApiService service = retrofit.create(ApiService.class);
        return service.mineLiveReservation(requestBody);
    }

    /**
     * 获取认证人资料
     * @return
     */
    public Observable<BaseResponse<ResGetCertificationData>> getCertificationData() {
        String content = "";
        try {
            ApiParams req = new ApiParams().with(FangConstants.ACTION_KEY, "GetCertificationData")
                    .with(FangConstants.HEADER_KEY, putHeader(null));

            content = req.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(CONTENT_TYPE, content);
        ApiService service = retrofit.create(ApiService.class);
        return service.getCertificationData(requestBody);
    }
    /**
     * 获取是否是认证人
     * @return
     */
    public Observable<BaseResponse<ResGetIsCertificationUser>> getIsCertificationUser(int userid) {
        String content = "";
        try {
            ApiParams req = new ApiParams().with(FangConstants.ACTION_KEY, "GetIsCertificationUser")
                    .with(FangConstants.HEADER_KEY, putHeader(null))
                    .with("userid", userid);

            content = req.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(CONTENT_TYPE, content);
        ApiService service = retrofit.create(ApiService.class);
        return service.getIsCertificationUser(requestBody);
    }

    /**
     * 所有区域
     */
    public Observable<BaseResponse<ResGetAreas>> getAreas() {
        String content = "";
        try {
            ApiParams req = new ApiParams().with(FangConstants.ACTION_KEY, "GetAreas")
                    .with(FangConstants.HEADER_KEY, putHeader(null));
            content = req.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(CONTENT_TYPE, content);
        ApiService service = retrofit.create(ApiService.class);
        return service.getAreas(requestBody);
    }


    /**
     * 某一区域所有楼盘
     */
    /*public Observable<BaseResponse<ResGetBuildings>> getBuildings(int areaid) {
        String content = "";
        try {
            ApiParams req = new ApiParams().with(FangConstants.ACTION_KEY, "GetBuildings")
                    .with(FangConstants.HEADER_KEY, putHeader(null))
                     .with("areaid", areaid);
            content = req.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(CONTENT_TYPE, content);
        ApiService service = retrofit.create(ApiService.class);
        return service.getBuildings(requestBody);
    }*/

    /**
     * 认证用户楼盘
     */
    public Observable<BaseResponse<ResGetBuildings>> getResponsibleBuildings() {
        String content = "";
        try {
            ApiParams req = new ApiParams().with(FangConstants.ACTION_KEY, "GetResponsibleBuildings")
                    .with(FangConstants.HEADER_KEY, putHeader(null));
            content = req.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(CONTENT_TYPE, content);
        ApiService service = retrofit.create(ApiService.class);
        return service.getResponsibleBuildings(requestBody);
    }

    /**
     * 发起直播
     *
     */
    public Observable<BaseResponse<ResSponsorLive>> sponsorLive() {
        String content = "";
        try {
            ApiParams req = new ApiParams().with(FangConstants.ACTION_KEY, "SponsorLive")
                    .with(FangConstants.HEADER_KEY, putHeader(null));
            content = req.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(CONTENT_TYPE, content);
        ApiService service = retrofit.create(ApiService.class);
        return service.sponsorLive(requestBody);
    }

    /**
     * 获取申请直播列表
     *
     */
    public Observable<BaseResponse<ResLiveApplyList>> getLiveApplyList() {
        String content = "";
        try {
            ApiParams req = new ApiParams().with(FangConstants.ACTION_KEY, "LiveApplyList")
                    .with(FangConstants.HEADER_KEY, putHeader(null));
            content = req.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(CONTENT_TYPE, content);
        ApiService service = retrofit.create(ApiService.class);
        return service.getLiveApplyList(requestBody);
    }

    /**
     * 申请直播
     *
     * @param areaid        区域id
     * @param areaName      区域名
     * @param buildingid    楼盘id
     * @param buildingName  楼盘名
     * @param startlivetime 直播开始时间
     * @param title         直播标题
     * @param description   直播内容描述
     */
    public Observable<BaseResponse> applyLive(int areaid,String areaName,int buildingid,String buildingName, String startlivetime, String title, String description) {
        String reqcontent = "";
        try {
            ApiParams req = new ApiParams().with(FangConstants.ACTION_KEY, "ApplyLive")
                    .with(FangConstants.HEADER_KEY, putHeader(null))
                    .with("areaid", areaid)
                    .with("area",areaName)
                    .with("buildingid", buildingid)
                    .with("building",buildingName)
                    .with("startlivetime", startlivetime)
                    .with("title", title)
                    .with("livedesc", description);
            reqcontent = req.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(CONTENT_TYPE, reqcontent);
        ApiService service = retrofit.create(ApiService.class);
        return service.applyLive(requestBody);
    }

    /**
     * 通知服务器有群成员进入
     * @param liveid                    直播id
     * @param groupid                    群组id
     * @param nickname                   观众昵称
     * @param headpic                    观众头像
     */
    public Observable<BaseResponse> enterGroup(long liveid,String groupid,String nickname,String headpic){
        String reqcontent = "";
        try {
            ApiParams req = new ApiParams().with(FangConstants.ACTION_KEY, "EnterGroup")
                    .with(FangConstants.HEADER_KEY,putHeader(null))
                    .with("liveid",liveid)
                    .with("groupid",groupid)
                    .with("nickname",nickname)
                    .with("headpic",headpic);
            reqcontent = req.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(CONTENT_TYPE,reqcontent);
        ApiService service = retrofit.create(ApiService.class);
        return service.enterGroup(requestBody);
    }

    /**
     * 通知服务器有群成员退出
     *
     * @param liveid                        直播id
     * @param groupid                       群组id
     */
    public Observable<BaseResponse> quitGroup(long liveid,String groupid){
        String reqcontent = "";
        try {
            ApiParams req = new ApiParams().with(FangConstants.ACTION_KEY, "QuitGroup")
                    .with(FangConstants.HEADER_KEY,putHeader(null))
                    .with("liveid",liveid)
                    .with("groupid",groupid);
            reqcontent = req.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(CONTENT_TYPE,reqcontent);
        ApiService service = retrofit.create(ApiService.class);
        return service.quitGroup(requestBody);
    }

    /**
     * 拉取群成员列表
     *
     * @param liveid                        主播id
     * @param groupid                       群组id
     * @param pageno                        页码
     * @param pagesize                      分页大小
     */
    public Observable<BaseResponse<ResFetchGroupMemberList>> fetchGroupmemberlist(long liveid, String groupid, int pageno, int pagesize){
        String reqcontent = "";
        try {
            ApiParams req = new ApiParams().with(FangConstants.ACTION_KEY, "FetchGroupMemberList")
                    .with(FangConstants.HEADER_KEY,putHeader(null))
                    .with("liveid",liveid)
                    .with("groupid",groupid)
                    .with("pageno", pageno)
                    .with("pagesize",pagesize);
            reqcontent = req.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(CONTENT_TYPE,reqcontent);
        ApiService service = retrofit.create(ApiService.class);
        return service.fetchGroupmemberlist(requestBody);
    }

    /**
     * 修改点赞计数器
     *
     * @param liveid                    主播id
     */
    public Observable<BaseResponse> addLikecount(long liveid){
        String reqcontent = "";
        try {
            ApiParams req = new ApiParams().with(FangConstants.ACTION_KEY, "AddLikeCount")
                    .with(FangConstants.HEADER_KEY,putHeader(null))
                    .with("liveid",liveid);
            reqcontent = req.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(CONTENT_TYPE,reqcontent);
        ApiService service = retrofit.create(ApiService.class);
        return service.addLikecount(requestBody);
    }

    /**
     * 获取最新直播预告
     */
    public Observable<BaseResponse<ResLatestLiveForecast>> getLatestLiveForecast(){
        String reqcontent = "";
        try {
            ApiParams req = new ApiParams().with(FangConstants.ACTION_KEY, "LatestLiveForecast")
                    .with(FangConstants.HEADER_KEY,putHeader(null));
            reqcontent = req.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(CONTENT_TYPE,reqcontent);
        ApiService service = retrofit.create(ApiService.class);
        return service.getLatestLiveForecast(requestBody);
    }

    /**
     * 获取最新直播预告
     * @param pageno                页码
     * @param pagesize              每页大小
     */
    public Observable<BaseResponse<ResAllLiveForecast>> getAllLiveForecast(int userid,int pageno, int pagesize){
        String reqcontent = "";
        try {
            ApiParams req = new ApiParams().with(FangConstants.ACTION_KEY, "AllLiveForecast")
                    .with(FangConstants.HEADER_KEY,putHeader(null))
                    .with("userid",userid)
                    .with("pageno",pageno)
                    .with("pagesize",pagesize);
            reqcontent = req.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(CONTENT_TYPE,reqcontent);
        ApiService service = retrofit.create(ApiService.class);
        return service.getAllLiveForecast(requestBody);
    }

    /**
     * 预约、取消预约
     * @param applyid               申请直播id
     * @param flag                  1 预约        0 取消预约
     */
    public Observable<BaseResponse> reserve(long applyid, int flag){
        String reqcontent = "";
        try {
            ApiParams req = new ApiParams().with(FangConstants.ACTION_KEY, "Reserve")
                    .with(FangConstants.HEADER_KEY,putHeader(null))
                    .with("applyid",applyid)
                    .with("flag",flag);
            reqcontent = req.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(CONTENT_TYPE,reqcontent);
        ApiService service = retrofit.create(ApiService.class);
        return service.reserve(requestBody);
    }



    /**
     * 获取游客用户签名
     */
    public Observable<BaseResponse<ResGetUserSign>> getUserSign(String visitorid) {
        String content = "";
        try {
            ApiParams req = new ApiParams().with(FangConstants.ACTION_KEY, "GetUserSign")
                    .with(FangConstants.HEADER_KEY, putHeader(null))
                    .with("visitorid",visitorid);
            content = req.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(CONTENT_TYPE, content);
        ApiService service = retrofit.create(ApiService.class);
        return service.getUserSign(requestBody);
    }

    /**
     * 获取VOD 上传视频签名
     */
    public Observable<BaseResponse<ResGetVODSign>> getVODSign(long videoId) {
        String content = "";
        try {
            ApiParams req = new ApiParams().with(FangConstants.ACTION_KEY, "GetVODSign")
                    .with(FangConstants.HEADER_KEY, putHeader(null))
                    .with("videoid",videoId);
            content = req.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(CONTENT_TYPE, content);
        ApiService service = retrofit.create(ApiService.class);
        return service.getVODSign(requestBody);
    }


    /**
     * 获取视频详情
     * @param videoId               视频id
     *
     */
    public Observable<BaseResponse<ResGetVideoDetails>> getVideoDetails(long videoId) {
        String content = "";
        try {
            ApiParams req = new ApiParams().with(FangConstants.ACTION_KEY, "GetVideoDetails")
                    .with(FangConstants.HEADER_KEY, putHeader(null))
                    .with("videoid",videoId);
            content = req.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(CONTENT_TYPE, content);
        ApiService service = retrofit.create(ApiService.class);
        return service.getVideoDetails(requestBody);
    }

    /**
     * 发布视频
     * @param reqPublishVOD   发布视频请求实体
     *
     */
    public Observable<BaseResponse<ResPublishVOD>> publishVOD(ReqPublishVOD reqPublishVOD) {
        reqPublishVOD.setAction("PublishVOD");
        reqPublishVOD.setHeader(putHeaderByReq(null));
        Gson gson = new Gson();
        String content = gson.toJson(reqPublishVOD);

        RequestBody requestBody = RequestBody.create(CONTENT_TYPE, content);
        ApiService service = retrofit.create(ApiService.class);
        return service.publishVOD(requestBody);
    }

    /**
     * 发布视频完成
     *
     * @param videoId               视频id
     * @param frontCover            封面地址
     * @param fileId                腾讯云返回的视频文件id
     */
    public Observable<BaseResponse> finishPublishVOD(long videoId, String frontCover,String videoURL, String fileId) {
        String content = "";
        try {
            ApiParams req = new ApiParams().with(FangConstants.ACTION_KEY, "FinishPublishVOD")
                    .with(FangConstants.HEADER_KEY, putHeader(null))
                    .with("videoid",videoId)
                    .with("frontcover",frontCover)
                    .with("videourl",videoURL)
                    .with("fileid",fileId);
            content = req.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(CONTENT_TYPE, content);
        ApiService service = retrofit.create(ApiService.class);
        return service.finishPublishVOD(requestBody);
    }

    /**
     * 获取精选视频列表
     */
    public Observable<BaseResponse<ResFeaturedList>> featuredList() {
        String content = "";
        try {
            ApiParams req = new ApiParams().with(FangConstants.ACTION_KEY, "FeaturedList")
                    .with(FangConstants.HEADER_KEY, putHeader(null));
            content = req.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(CONTENT_TYPE, content);
        ApiService service = retrofit.create(ApiService.class);
        return service.featuredList(requestBody);
    }

    /**
     * 拉取视频（点播）列表
     * @param filter                筛选条件
     * @param pageno                当前页码
     * @param pagesize              每页大小
     */
    public Observable<BaseResponse<ResFetchVodList>> fetchVodList(VideoFilter filter, VideoSort sort,int pageno, int pagesize) {
        ReqFetchVodList request = new ReqFetchVodList();
        request.setAction("FetchVodList");
        request.setHeader(putHeaderByReq(null));

        if(null != filter){
            request.setCitygroupidcond(filter.getCityGroupId());
            request.setAreaidcond(filter.getAreaId());
            request.setAreanamecond(filter.getAreaName());
            request.setBuildingidcond(filter.getBuildingId());
            request.setBuildingnamecond(filter.getBuildingName());
            request.setHousetypeidcond(filter.getHousetypeId());
            request.setCategoryidcond(filter.getCategoryid());
            request.setUserid(filter.getUserid());
        }

        request.setSort(sort.getValue());
        request.setPageno(pageno);
        request.setPagesize(pagesize);

        Gson gson = new Gson();
        String content = gson.toJson(request);

        RequestBody requestBody = RequestBody.create(CONTENT_TYPE, content);
        ApiService service = retrofit.create(ApiService.class);
        return service.fetchVodList(requestBody);
    }


    /**
     * 相关视频
     * @param videoid    视频id
     */
    public Observable<BaseResponse<ResRelatedVideos>> relatedVideos(long videoid) {
        String content = "";
        try {
            ApiParams req = new ApiParams().with(FangConstants.ACTION_KEY, "RelatedVideos")
                    .with(FangConstants.HEADER_KEY, putHeader(null))
                    .with("videoid",videoid);
            content = req.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(CONTENT_TYPE, content);
        ApiService service = retrofit.create(ApiService.class);
        return service.relatedVideos(requestBody);
    }
    /**
     * 获取我的收藏视频
     */
    public Observable<BaseResponse<ResFetchVodList>> getMineFavoriteVod(int pageno, int pagesize) {
        String content = "";
        try {
            ApiParams req = new ApiParams().with(FangConstants.ACTION_KEY, "GetMineFavoriteVod")
                    .with(FangConstants.HEADER_KEY, putHeader(null))
                    .with("pageno",pageno)
                    .with("pagesize",pagesize);

            content = req.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(CONTENT_TYPE, content);
        ApiService service = retrofit.create(ApiService.class);
        return service.getMineFavoriteVod(requestBody);
    }

    /**
     * 视频所属楼盘
     * @param videoid 视频id
     */
    public Observable<BaseResponse<ResAttachedBuilding>> attachedBuilding(long videoid) {
        String content = "";
        try {
            ApiParams req = new ApiParams().with(FangConstants.ACTION_KEY, "AttachedBuilding")
                    .with(FangConstants.HEADER_KEY, putHeader(null))
                    .with("videoid",videoid);
            content = req.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(CONTENT_TYPE, content);
        ApiService service = retrofit.create(ApiService.class);
        return service.attachedBuilding(requestBody);
    }

    /**
     * 所属区域
     * @param videoid    视频id
     *
     */
    public Observable<BaseResponse<ResAttachedArea>> attachedArea(long videoid) {
        String content = "";
        try {
            ApiParams req = new ApiParams().with(FangConstants.ACTION_KEY, "AttachedArea")
                    .with(FangConstants.HEADER_KEY, putHeader(null))
                    .with("videoid",videoid);
            content = req.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(CONTENT_TYPE, content);
        ApiService service = retrofit.create(ApiService.class);
        return service.attachedArea(requestBody);
    }


    /**
     * 获取cos
     */
    public Observable<BaseResponse<ResGetCOSSign>> getCOSSign() {
        String content = "";
        try {
            ApiParams req = new ApiParams().with(FangConstants.ACTION_KEY, "GetCOSSign")
                    .with(FangConstants.HEADER_KEY, putHeader(null));
            content = req.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(CONTENT_TYPE, content);
        ApiService service = retrofit.create(ApiService.class);
        return service.getCOSSign(requestBody);
    }


    /**
     * 获取所有的系统推送通知
     * @param pageNo            分页号
     * @param pageSize          每页大小
     */
    public Observable<BaseResponse<ResGetNotifications>> getNotifications(int pageNo, int pageSize) {
        String content = "";
        try {
            ApiParams req = new ApiParams().with(FangConstants.ACTION_KEY, "GetNotifications")
                    .with(FangConstants.HEADER_KEY, putHeader(null))
                    .with("pageno",pageNo)
                    .with("pagesize",pageSize);
            content = req.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(CONTENT_TYPE, content);
        ApiService service = retrofit.create(ApiService.class);
        return service.getNotifications(requestBody);
    }

    /**
     * 获取互动消息通知
     * @param pageNo                分页号
     * @param pageSize              每页大小
     */
    public Observable<BaseResponse<ResGetInteractiveMsgs>> getInteractiveMsgs(int pageNo, int pageSize) {
        String content = "";
        try {
            ApiParams req = new ApiParams().with(FangConstants.ACTION_KEY, "GetInteractiveMsgs")
                    .with(FangConstants.HEADER_KEY, putHeader(null))
                    .with("pageno",pageNo)
                    .with("pagesize",pageSize);
            content = req.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(CONTENT_TYPE, content);
        ApiService service = retrofit.create(ApiService.class);
        return service.getInteractiveMsgs(requestBody);
    }


    /**
     * 关注/取消关注
     * @param objectId                      关注对象id
     * @param objectType                    对象类型  0 人   1区域    2 楼盘
     * @param flag                          1 关注    0取消关注
     */
    public Observable<BaseResponse> attention(String objectId,int objectType,int flag) {
        String content = "";
        try {
            ApiParams req = new ApiParams().with(FangConstants.ACTION_KEY, "Attention")
                    .with(FangConstants.HEADER_KEY, putHeader(null))
                    .with("objectid",objectId)
                    .with("objecttype",objectType)
                    .with("flag",flag);
            content = req.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(CONTENT_TYPE, content);
        ApiService service = retrofit.create(ApiService.class);
        return service.attention(requestBody);
    }

    /**
     * 收藏/取消收藏
     * @param objectId          收藏对象id
     * @param favtype           类型  （1 视频    2 文章）
     * @param flag              标识  （1 收藏    0取消收藏）
     */
    public Observable<BaseResponse> favorite(String objectId,int favtype,int flag) {
        String content = "";
        try {
            ApiParams req = new ApiParams().with(FangConstants.ACTION_KEY, "Favorite")
                    .with(FangConstants.HEADER_KEY, putHeader(null))
                    .with("favobjectid",objectId)
                    .with("favtype",favtype)
                    .with("flag",flag);
            content = req.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(CONTENT_TYPE, content);
        ApiService service = retrofit.create(ApiService.class);
        return service.favorite(requestBody);
    }


    /**
     * 发布视频选择分类
     */
    public Observable<BaseResponse<ResPublishVideoCategory>> publishVideoCategory() {
        String content = "";
        try {
            ApiParams req = new ApiParams().with(FangConstants.ACTION_KEY, "PublishVideoCategory")
                    .with(FangConstants.HEADER_KEY, putHeader(null));
            content = req.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(CONTENT_TYPE, content);
        ApiService service = retrofit.create(ApiService.class);
        return service.publishVideoCategory(requestBody);
    }

    /**
     * 样板间户型
     * @param buildingId            楼盘id
     */
    public Observable<BaseResponse<ResGetShowroomType>> getShowroomType(int buildingId) {
        String content = "";
        try {
            ApiParams req = new ApiParams().with(FangConstants.ACTION_KEY, "GetShowroomType")
                    .with(FangConstants.HEADER_KEY, putHeader(null))
                    .with("buildingid",buildingId);
            content = req.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(CONTENT_TYPE, content);
        ApiService service = retrofit.create(ApiService.class);
        return service.getShowroomType(requestBody);
    }
    /**
     * 楼盘列表
     */
    public Observable<BaseResponse<ResGetResBuildingList>> getResBuildingList(int ordercond,int regionId, int pageno, int pagesize) {
        String content = "";
        try {
            ApiParams req = new ApiParams().with(FangConstants.ACTION_KEY, "GetResBuildingList")
                    .with(FangConstants.HEADER_KEY, putHeader(null))
                    .with("ordercond",ordercond)
                    .with("regionId",regionId)
                    .with("pageno",pageno)
                    .with("pagesize",pagesize);

            content = req.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(CONTENT_TYPE, content);
        ApiService service = retrofit.create(ApiService.class);
        return service.getResBuildingList(requestBody);

    }
    /**
     * 楼盘列表
     */
    public Observable<BaseResponse<ResGetResBuildingList>> getHotBuildingsByAreaId(int regionId, int showcount) {
        String content = "";
        try {
            ApiParams req = new ApiParams().with(FangConstants.ACTION_KEY, "GetHotBuildingsByAreaId")
                    .with(FangConstants.HEADER_KEY, putHeader(null))
                    .with("areaid",regionId)
                    .with("showcount",showcount);

            content = req.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(CONTENT_TYPE, content);
        ApiService service = retrofit.create(ApiService.class);
        return service.getResBuildingList(requestBody);

    }

    /**
     * 楼盘详情
     */
    public Observable<BaseResponse<ResGetResBuilding>> getResBuilding(int buildingid) {
        String content = "";
        try {
            ApiParams req = new ApiParams().with(FangConstants.ACTION_KEY, "GetResBuilding")
                    .with(FangConstants.HEADER_KEY, putHeader(null))
                    .with("buildingid",buildingid);
            content = req.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(CONTENT_TYPE, content);
        ApiService service = retrofit.create(ApiService.class);
        return service.getResBuilding(requestBody);
    }

    /**
     * 获取城市群列表
     */
    public Observable<BaseResponse<ResCityGroup>> getCityGroups(){
        ReqCityGroup request = new ReqCityGroup();
        request.setHeader(putHeaderByReq(null));
        request.setAction("CityGroup");

        Gson gson = new Gson();
        String content = gson.toJson(request);

        RequestBody requestBody = RequestBody.create(CONTENT_TYPE, content);
        ApiService service = retrofit.create(ApiService.class);
        return service.getCityGroups(requestBody);
    }

    /**
     * 区域列表
     *
     * @param cityid 城市群id
     */
    public Observable<BaseResponse<ResGetResRegionList>> getResRegionList(int cityid,int sort) {
        String content = "";
        try {
            ApiParams req = new ApiParams().with(FangConstants.ACTION_KEY, "GetResRegionList")
                    .with(FangConstants.HEADER_KEY, putHeader(null))
                    .with("cityid",cityid)
                    .with("sort",sort);
            content = req.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(CONTENT_TYPE, content);
        ApiService service = retrofit.create(ApiService.class);
        return service.getResRegionList(requestBody);
    }
    /**
     * 区域详情
     */
    public Observable<BaseResponse<ResGetResRegion>> getResRegion(int regionId) {
        String content = "";
        try {
            ApiParams req = new ApiParams().with(FangConstants.ACTION_KEY, "GetResRegion")
                    .with(FangConstants.HEADER_KEY, putHeader(null))
                    .with("regionId",regionId);
            content = req.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(CONTENT_TYPE, content);
        ApiService service = retrofit.create(ApiService.class);
        return service.getResRegion(requestBody);
    }


    /**
     *  楼盘户型
     */
    public Observable<BaseResponse<ResGetRoomTypeList>>  getRoomTypeList(int buildingid,String  formatName, int pageno, int pagesize) {
        String content = "";
        try {
            ApiParams req = new ApiParams().with(FangConstants.ACTION_KEY, "GetRoomTypeList")
                    .with(FangConstants.HEADER_KEY, putHeader(null))
                    .with("buildingid",buildingid)
                    .with("formatname",formatName)
                    .with("pageno",pageno)
                    .with("pagesize",pagesize);
            content = req.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(CONTENT_TYPE, content);
        ApiService service = retrofit.create(ApiService.class);
        return service.getRoomTypeList(requestBody);
    }
    /**
     *  楼盘格局
     */
    public Observable<BaseResponse<ResGetShowroomFormat>>  getShowroomFormat(int buildingid) {
        String content = "";
        try {
            ApiParams req = new ApiParams().with(FangConstants.ACTION_KEY, "GetShowroomFormat")
                    .with(FangConstants.HEADER_KEY, putHeader(null))
                    .with("buildingid",buildingid);
            content = req.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(CONTENT_TYPE, content);
        ApiService service = retrofit.create(ApiService.class);
        return service.getShowroomFormat(requestBody);
    }
    /**
     *  楼盘图片
     */
    public Observable<BaseResponse<ResGetPictureList>>  getPictureList(int areaidcond,int buildingid,int housetypeid) {
        String content = "";
        try {
            ApiParams req = new ApiParams().with(FangConstants.ACTION_KEY, "GetPictureList")
                    .with(FangConstants.HEADER_KEY, putHeader(null))
                    .with("areaidcond",areaidcond)
                    .with("buildingidcond",buildingid)
                    .with("housetypeid",housetypeid);
            content = req.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(CONTENT_TYPE, content);
        ApiService service = retrofit.create(ApiService.class);
        return service.getPictureList(requestBody);
    }
    /**
     *  楼盘轮播图
     */
    public Observable<BaseResponse<ResGetBuildingAdverList>>  getBuildingAdverList() {
        String content = "";
        try {
            ApiParams req = new ApiParams().with(FangConstants.ACTION_KEY, "GetBuildingAdverList")
                    .with(FangConstants.HEADER_KEY, putHeader(null));
            content = req.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(CONTENT_TYPE, content);
        ApiService service = retrofit.create(ApiService.class);
        return service.getBuildingAdverList(requestBody);
    }

    /**
     *  置业顾问
     */
    public Observable<BaseResponse<ResGetCounselorList>>  getCounselorList(int objectType , int objectid, int pageno, int pagesize) {
        String content = "";
        try {
            ApiParams req = new ApiParams().with(FangConstants.ACTION_KEY, "GetCounselorList")
                    .with(FangConstants.HEADER_KEY, putHeader(null))
                    .with("objectType",objectType)
                    .with("objectid",objectid)
                    .with("pageno",pageno)
                    .with("pagesize",pagesize);
            content = req.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(CONTENT_TYPE, content);
        ApiService service = retrofit.create(ApiService.class);
        return service.getCounselorList(requestBody);
    }
    /**
     *  热门搜索
     */
    public Observable<BaseResponse<ResGetHotSearch>>  getHotSearch() {
        String content = "";
        try {
            ApiParams req = new ApiParams().with(FangConstants.ACTION_KEY, "GetHotSearch")
                    .with(FangConstants.HEADER_KEY, putHeader(null));

            content = req.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(CONTENT_TYPE, content);
        ApiService service = retrofit.create(ApiService.class);
        return service.getHotSearch(requestBody);
    }
    /**
     *  搜索结果
     */
    public Observable<BaseResponse<ResGetSearchResult>>  getSearchResult(String condition, int pageno, int pagesize) {
        String content = "";
        try {
            ApiParams req = new ApiParams().with(FangConstants.ACTION_KEY, "GetSearchResult")
                    .with(FangConstants.HEADER_KEY, putHeader(null))
                    .with("condition", condition)
                    .with("pageno", pageno)
                    .with("pagesize", pagesize);

            content = req.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(CONTENT_TYPE, content);
        ApiService service = retrofit.create(ApiService.class);
        return service.getSearchResult(requestBody);
    }

    /**
     *  评论
     * @param objectType                0 视频、1 区域、2楼盘
     * @param objectId                  评论的对象的id
     * @param discussContent            评论内容
     */
    public Observable<BaseResponse> discuss(int objectType,String objectId,String discussContent) {
        String content = "";
        try {
            ApiParams req = new ApiParams().with(FangConstants.ACTION_KEY, "Discuss")
                    .with(FangConstants.HEADER_KEY, putHeader(null))
                    .with("objecttype",objectType)
                    .with("objectid",objectId)
                    .with("content",discussContent);
            content = req.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(CONTENT_TYPE, content);
        ApiService service = retrofit.create(ApiService.class);
        return service.discuss(requestBody);
    }

    /**
     * 回复评论/回复
     * @param nickname                      回复人昵称
     * @param discussid                     评论id
     * @param replyid                       回复id
     * @param replyContent                  回复内容
     */
    public Observable<BaseResponse> reply(String nickname,long discussid,long replyid,String replyContent) {
        String content = "";
        try {
            ApiParams req = new ApiParams().with(FangConstants.ACTION_KEY, "Reply")
                    .with(FangConstants.HEADER_KEY, putHeader(null))
                    .with("nickname",nickname)
                    .with("discussid",discussid)
                    .with("replyid",replyid)
                    .with("content",replyContent);
            content = req.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(CONTENT_TYPE, content);
        ApiService service = retrofit.create(ApiService.class);
        return service.reply(requestBody);
    }

    /**
     * 获取评论列表
     * @param objectType                    0 视频、1 区域、2楼盘
     * @param objectId                      评论的对象的id
     * @param pageNo                        页码
     * @param pageSize                      分页大小
     */
    public Observable<BaseResponse<ResGetDiscussList>> getDiscussList (int objectType,String objectId,int pageNo,int pageSize) {
        String content = "";
        try {
            ApiParams req = new ApiParams().with(FangConstants.ACTION_KEY, "GetDiscussList")
                    .with(FangConstants.HEADER_KEY, putHeader(null))
                    .with("objecttype",objectType)
                    .with("objectid",objectId)
                    .with("pageno",pageNo)
                    .with("pagesize",pageSize);
            content = req.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(CONTENT_TYPE, content);
        ApiService service = retrofit.create(ApiService.class);
        return service.getDiscussList(requestBody);
    }

    /**
     * 获取回复列表
     * @param discussid                     评论内容id
     * @param pageNo                        页码
     * @param pageSize                      分页大小
     */
    public Observable<BaseResponse<ResGetReplyList>> getReplyList (long discussid, int pageNo, int pageSize) {
        String content = "";
        try {
            ApiParams req = new ApiParams().with(FangConstants.ACTION_KEY, "GetReplyList")
                    .with(FangConstants.HEADER_KEY, putHeader(null))
                    .with("discussid",discussid)
                    .with("pageno",pageNo)
                    .with("pagesize",pageSize);
            content = req.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(CONTENT_TYPE, content);
        ApiService service = retrofit.create(ApiService.class);
        return service.getReplyList(requestBody);
    }

    /**
     * 删除评论
     * @param discussid 评论id
     */
    public Observable<BaseResponse> deleteDiscuss(long discussid) {
        String content = "";
        try {
            ApiParams req = new ApiParams().with(FangConstants.ACTION_KEY, "DeleteDiscuss")
                    .with(FangConstants.HEADER_KEY, putHeader(null))
                    .with("discussid",discussid);
            content = req.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(CONTENT_TYPE, content);
        ApiService service = retrofit.create(ApiService.class);
        return service.deleteDiscuss(requestBody);
    }

    /**
     * 删除回复
     * @param replyid   回复内容id
     */
    public Observable<BaseResponse> deleteReply(long replyid) {
        String content = "";
        try {
            ApiParams req = new ApiParams().with(FangConstants.ACTION_KEY, "DeleteReply")
                    .with(FangConstants.HEADER_KEY, putHeader(null))
                    .with("replyid",replyid);
            content = req.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(CONTENT_TYPE, content);
        ApiService service = retrofit.create(ApiService.class);
        return service.deleteReply(requestBody);
    }


    /**
     * 开始播放视频
     * @param videoid                       视频id
     */
    public Observable<BaseResponse<ResStartPlay>> startPlay (long videoid) {
        String content = "";
        try {
            ApiParams req = new ApiParams().with(FangConstants.ACTION_KEY, "StartPlay")
                    .with(FangConstants.HEADER_KEY, putHeader(null))
                    .with("videoid",videoid);
            content = req.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(CONTENT_TYPE, content);
        ApiService service = retrofit.create(ApiService.class);
        return service.startPlay(requestBody);
    }

    /**
     * 停止播放
     * @param videoid               视频id
     * @param playid                播放id
     */
    public Observable<BaseResponse> stopPlay (long videoid, long playid) {
        String content = "";
        try {
            ApiParams req = new ApiParams().with(FangConstants.ACTION_KEY, "StopPlay")
                    .with(FangConstants.HEADER_KEY, putHeader(null))
                    .with("videoid",videoid)
                    .with("playid",playid);
            content = req.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(CONTENT_TYPE, content);
        ApiService service = retrofit.create(ApiService.class);
        return service.stopPlay(requestBody);
    }

    /**
     * 获取推荐视频列表
     * @param sortBy       排序方式
     * @param pNo          页码
     * @param pSize        每页大小
     */
    public Observable<BaseResponse<ResRecommendVideos>> getRecommendVideos (int sortBy,int pNo,int pSize) {
        String content = "";
        try {
            ApiParams req = new ApiParams().with(FangConstants.ACTION_KEY, "RecommendVideos")
                    .with(FangConstants.HEADER_KEY, putHeader(null))
                    .with("sort",sortBy)
                    .with("pageno",pNo)
                    .with("pagesize",pSize);
            content = req.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(CONTENT_TYPE, content);
        ApiService service = retrofit.create(ApiService.class);
        return service.getRecommendVideos(requestBody);
    }

    /**
     * 获取关注视频列表
     * @param sortBy       排序方式
     * @param pNo          页码
     * @param pSize        每页大小
     */
    public Observable<BaseResponse<ResAttentionVideos>> getAttentionVideos (int sortBy, int pNo, int pSize) {
        String content = "";
        try {
            ApiParams req = new ApiParams().with(FangConstants.ACTION_KEY, "AttentionVideos")
                    .with(FangConstants.HEADER_KEY, putHeader(null))
                    .with("sort",sortBy)
                    .with("pageno",pNo)
                    .with("pagesize",pSize);
            content = req.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(CONTENT_TYPE, content);
        ApiService service = retrofit.create(ApiService.class);
        return service.getAttentionVideos(requestBody);
    }

    /**
     * 获取视频分类
     */
    public Observable<BaseResponse<ResVideoClassify>> getVideoClassification () {
        String content = "";
        try {
            ApiParams req = new ApiParams().with(FangConstants.ACTION_KEY, "VideoClassify")
                    .with(FangConstants.HEADER_KEY, putHeader(null));
            content = req.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(CONTENT_TYPE, content);
        ApiService service = retrofit.create(ApiService.class);
        return service.getVideoClassification(requestBody);
    }

    /**
     *
     * 获取城市列表
     */
    public Observable<BaseResponse<ResGetRegions>> getRegions () {
        String content = "";
        try {
            ApiParams req = new ApiParams().with(FangConstants.ACTION_KEY, "GetRegions")
                    .with(FangConstants.HEADER_KEY, putHeader(null));
            content = req.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(CONTENT_TYPE, content);
        ApiService service = retrofit.create(ApiService.class);
        return service.getRegions(requestBody);
    }

    /**
     * 获取楼盘根据城市id
     *
     * @param cityId
     */
    public Observable<BaseResponse<ResGetBuildings>> getBuildings (int cityId) {
        String content = "";
        try {
            ApiParams req = new ApiParams().with(FangConstants.ACTION_KEY, "GetBuildings")
                    .with(FangConstants.HEADER_KEY, putHeader(null))
                    .with("cityid",cityId);
            content = req.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(CONTENT_TYPE, content);
        ApiService service = retrofit.create(ApiService.class);
        return service.getBuildings(requestBody);
    }
    /**
     * 获取视频发布者信息
     *
     * @param publisherid
     */
    public Observable<BaseResponse<ResGetVideoPublisherInfo>> getVideoPublisherInfo (String publisherid) {
        String content = "";
        try {
            ApiParams req = new ApiParams().with(FangConstants.ACTION_KEY, "GetVideoPublisherInfo")
                    .with(FangConstants.HEADER_KEY, putHeader(null))
                    .with("publisherid",publisherid);
            content = req.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(CONTENT_TYPE, content);
        ApiService service = retrofit.create(ApiService.class);
        return service.getVideoPublisherInfo(requestBody);
    }
    /**
     * 获取视频发布者所有视频
     *
     * @param publisherid
     */
    public Observable<BaseResponse<ResGetPublishVideos>> getPublishVideos (String publisherid,int pageno, int pagesize) {
        String content = "";
        try {
            ApiParams req = new ApiParams().with(FangConstants.ACTION_KEY, "GetPublishVideos")
                    .with(FangConstants.HEADER_KEY, putHeader(null))
                    .with("publisherid",publisherid)
                    .with("pageno",pageno)
                    .with("pagesize",pagesize);
            content = req.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(CONTENT_TYPE, content);
        ApiService service = retrofit.create(ApiService.class);
        return service.getPublishVideos(requestBody);
    }
  /**
     * 通过城市和省名字获取城市id
     *
     * @param provinceName              省名
     * @param cityName                  城市名
     */
    public Observable<BaseResponse<ResGetCityIdByName>> getCityIdByName (String provinceName,String cityName) {
        String content = "";
        try {
            ApiParams req = new ApiParams().with(FangConstants.ACTION_KEY, "GetCityIdByName")
                    .with(FangConstants.HEADER_KEY, putHeader(null))
                    .with("province",provinceName)
                    .with("city",cityName);
            content = req.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(CONTENT_TYPE, content);
        ApiService service = retrofit.create(ApiService.class);
        return service.getCityIdByName(requestBody);
    }


    /**
     * 分享视频
     * @param shareUrl                  分享地址
     * @param videoId                   视频id
     * @param channel                   分享渠道    微信好友/微信朋友圈
     */
    public Observable<BaseResponse> makeShare (String shareUrl,String videoId,int type,int channel) {
        String content = "";
        try {
            ApiParams req = new ApiParams().with(FangConstants.ACTION_KEY, "Share")
                    .with(FangConstants.HEADER_KEY, putHeader(null))
                    .with("shareurl",shareUrl)
                    .with("shareobjectid",videoId)
                    .with("sharetype",type)
                    .with("channel",channel);
            content = req.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(CONTENT_TYPE, content);
        ApiService service = retrofit.create(ApiService.class);
        return service.makeShare(requestBody);
    }
    /**
     *    是否公开手机号
     */

    public Observable<BaseResponse> exposureMobile (int visible) {
        String content = "";
        try {
            ApiParams req = new ApiParams().with(FangConstants.ACTION_KEY, "ExposureMobile")
                    .with(FangConstants.HEADER_KEY, putHeader(null))
                    .with("visible",visible);
            content = req.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(CONTENT_TYPE, content);
        ApiService service = retrofit.create(ApiService.class);
        return service.exposureMobile(requestBody);
    }
 /**
     * 删除我自己发布的视频
     * @param videoid           视频id
     */
    public Observable<BaseResponse> deleteVideo(long videoid) {
        String content = "";
        try {
            ApiParams req = new ApiParams().with(FangConstants.ACTION_KEY, "DeleteUserVOD")
                    .with(FangConstants.HEADER_KEY, putHeader(null))
                    .with("videoid",videoid);
            content = req.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(CONTENT_TYPE, content);
        ApiService service = retrofit.create(ApiService.class);
        return service.deleteVideo(requestBody);
    }
    /**
     *   获取我的视频
     */

    public Observable<BaseResponse<ResGetMyVideos>> getMyVideos (int pageno, int pagesize) {
        String content = "";
        try {
            ApiParams req = new ApiParams().with(FangConstants.ACTION_KEY, "GetMyVideos")
                    .with(FangConstants.HEADER_KEY, putHeader(null))
                    .with("pageno",pageno)
                    .with("pagesize",pagesize);
            content = req.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(CONTENT_TYPE, content);
        ApiService service = retrofit.create(ApiService.class);
        return service.getMyVideos(requestBody);
    }
}

