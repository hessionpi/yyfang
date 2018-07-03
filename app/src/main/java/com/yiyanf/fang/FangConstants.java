package com.yiyanf.fang;

import com.tencent.cos.common.COSEndPoint;

/**
 * Created by Hition on 2017/9/19.
 * <p>
 * 静态函数,用于存储项目中静态常量
 */

public class FangConstants {

    /**
     * 两次按下返回键退出的间隔
     */
    public static final Integer QUIT_INTERVAL = 2 * 1000;

    public static final String CASH_PATH = "yyfang_cache";
    public static final String DIR_PATH = "yiyanf";

    // 本地数据库名称，主要用于部分业务数据进行本地持久化存储
    public static final String DB_NAME = "fang.realm";

    //************在腾讯云视频直播服务配置************
    //云通信服务相关配置
//    public static final int IMSDK_ACCOUNT_TYPE = 15420;
    public static int IMSDK_APPID = 1400046151;

    //COS存储服务相关配置
    public static String COS_BUCKET = "fang";
    public static String COS_APPID = "1254235267";
    //对应关系填入，如是“华南”请填写COSEndPoint.COS_GZ，“华北”请填写COSEndPoint.COS_TJ，“华东”请填写COSEndPoint.COS_SH
    public static COSEndPoint COS_REGION = COSEndPoint.COS_SH;

    //业务Server的Http配置
    public static final String SVR_POST_URL_DEVELOP = "http://111.202.205.174";
    public static final String SVR_POST_URL_ONLINE = "http://fang.yiyanf.com";
    //直播分享页面的跳转地址，分享到微信、手Q后点击观看将会跳转到这个地址，请参考https://www.qcloud.com/document/product/454/8046 文档部署html5的代码后，替换成相应的页面地址
    public static final String SVR_LivePlayShare_URL = "https://www.baidu.com";
    //设置第三方平台的appid和appsecrect，大部分平台进行分享操作需要在第三方平台创建应用并提交审核，通过后拿到appid和appsecrect并填入这里，具体申请方式请参考http://dev.umeng.com/social/android/operation
    //有关友盟组件更多资料请参考这里：http://dev.umeng.com/social/android/quick-integration
    public static final String WEIXIN_SHARE_ID = "";
    public static final String WEIXIN_SHARE_SECRECT = "";

    public static final String SINA_WEIBO_SHARE_ID = "";
    public static final String SINA_WEIBO_SHARE_SECRECT = "";
    public static final String SINA_WEIBO_SHARE_REDIRECT_URL = "http://sns.whalecloud.com/sina2/callback";

    public static final String QQZONE_SHARE_ID = "";
    public static final String QQZONE_SHARE_SECRECT = "";

    //小直播appid
    public static final int XIAOZHIBO_APPID = 0;

    //主播退出广播字段
    public static final String EXIT_APP = "EXIT_APP";

    //码率
    public static final int BITRATE_SLOW = 900;
    public static final int BITRATE_NORMAL = 1200;
    public static final int BITRATE_FAST = 1600;

    //直播端右下角listview显示type
    public static final int TEXT_TYPE = 0;
    public static final int MEMBER_ENTER = 1;
    public static final int MEMBER_EXIT = 2;
    public static final int PRAISE = 3;

    public static final int LOCATION_PERMISSION_REQ_CODE = 1;

    /**
     * IM 互动消息类型
     */
    public static final int IMCMD_PAILN_TEXT = 1;   // 文本消息
    public static final int IMCMD_ENTER_LIVE = 2;   // 用户加入直播
    public static final int IMCMD_EXIT_LIVE = 3;   // 用户退出直播
    public static final int IMCMD_PRAISE = 4;   // 点赞消息
    public static final int IMCMD_DANMU = 5;   // 弹幕消息

    //ERROR CODE TYPE
    public static final int ERROR_GROUP_NOT_EXIT = 10010;
    public static final int ERROR_QALSDK_NOT_INIT = 6013;
    public static final int ERROR_JOIN_GROUP_ERROR = 10015;
    public static final int SERVER_NOT_RESPONSE_CREATE_ROOM = 1002;
    public static final int NO_LOGIN_CACHE = 1265;


    /**
     * 视频上传secretID错误，已经废弃，不会抛出
     */
    public static final int ERR_UGC_INVALID_SECRETID = 1011;

    /**
     * 视频上传signature错误
     */
    public static final int ERR_UGC_INVALID_SIGNATURE = 1012;

    /**
     * 视频文件的路径错误
     */
    public static final int ERR_UGC_INVALID_VIDOPATH = 1013;
    /**
     * 当前路径下视频文件不存在
     */
    public static final int ERR_UGC_INVALID_VIDEO_FILE = 1014;

    /**
     * 视频上传文件名太长或含有特殊字符
     */
    public static final int ERR_UGC_FILE_NAME = 1015;

    /**
     * 视频文件封面路径不对
     */
    public static final int ERR_UGC_INVALID_COVER_PATH = 1016;


    /**
     * 用户可见的错误提示语
     */
    public static final String ERROR_MSG_NET_DISCONNECTED = "网络异常，请检查网络";

    //直播端错误信息
    public static final String ERROR_MSG_CREATE_GROUP_FAILED = "创建直播房间失败,Error:";
    public static final String ERROR_MSG_GET_PUSH_URL_FAILED = "拉取直播推流地址失败,Error:";
    public static final String ERROR_MSG_OPEN_CAMERA_FAIL = "无法打开摄像头，需要摄像头权限";
    public static final String ERROR_MSG_OPEN_MIC_FAIL = "无法打开麦克风，需要麦克风权限";
    public static final String ERROR_MSG_RECORD_PERMISSION_FAIL = "无法进行录屏,需要录屏权限";
    public static final String ERROR_MSG_NO_LOGIN_CACHE = "您的帐号已在其它地方登录";

    //播放端错误信息
    public static final String ERROR_MSG_GROUP_NOT_EXIT = "直播已结束，加入失败";
    public static final String ERROR_MSG_JOIN_GROUP_FAILED = "加入房间失败，Error:";
    public static final String ERROR_MSG_LIVE_STOPPED = "直播已结束";
    public static final String ERROR_MSG_NOT_QCLOUD_LINK = "非腾讯云链接，若要放开限制请联系腾讯云商务团队";
    public static final String ERROR_RTMP_PLAY_FAILED = "视频流播放失败，Error:";

    public static final String TIPS_MSG_STOP_PUSH = "当前正在直播，是否退出直播？";


    //**********************************************************************


    // ******************************** 请求Fang Server API常量定义
    /**
     * 用于存放用户登录后用户信息
     */
    public static final String LOGIN_MODEL_FILENAME = "/userinfo";

    /*public static final String SVR_RETURN_CODE  = "returnValue";
    public static final String SVR_RETURN_MSG   = "returnMsg";
    public static final String SVR_RETURN_DATA  = "returnData";*/

    public static final int RETURN_VALUE_OK = 0;
    public static final int RETURN_VALUE_SYSTEM_ERROE = 500;

    public static final int EXPIRETIME = 600;

    public static final String USERID_KEY = "userid";
    public static final String PLATFORM_KEY = "platform";
    public static final String CURRENTTIME_KEY = "currenttime";
    public static final String EXPIRETIME_KEY = "expiretime";
    public static final String SIGN_KEY = "sign";
    public static final String RANDOM_KEY = "random";
    public static final String VERSION_KEY = "apiversion";

    public static final String ACTION_KEY = "action";
    public static final String HEADER_KEY = "header";


    public static final String PLATFORM_VALUE = "android";
    public static final String API_VERSION_VALUE_DEFAULT = "1.0.0";


    //默认
    public static final int DEFAULT = 0;
    //登录
    public static final int UESR_LOGIN = 1;
    //注册
    public static final int USER_REGISTER = 2;
    //忘记密码
    public static final int USER_FIND_PASSWORD = 3;
    //完善资料
    public static final int USER_PERFECT_DATA = 4;
    //修改密码
    public static final int USER_CHANGE_PASSWORD = 5;
    //认证资料
    public static final int AUTH_DATA = 6;
    //关注列表
    public static final int ATTENTION_LIST = 7;
    public static final int FANS_LIST = 8;

    //所有区域
    public static final int GET_REGIONS = 10;
    //某一区域下所有楼盘
    public static final int BUILDINGS = 11;
    public static final int GET_CITY_ID_BY_NAME = 15;
    public static final int COS_SIGN = 20;

    public static final int VIDEO_CATEGORY = 30;

    public static final int VOD_SIGN = 50;
    public static final int VOD_PUBLISH = 51;
    public static final int VOD_DISCUSS = 52;
    public static final int VOD_DISCUSS_REPLY = 53;
    public static final int VOD_DISCUSS_LIST = 54;
    public static final int VOD_DISCUSS_REPLY_LIST = 55;
    public static final int VOD_DISCUSS_DELETE = 56;
    public static final int VOD_DISCUSS_REPLY_DELETE = 57;
    public static final int VOD_FEATUREDLIST = 58;
    public static final int VOD_FETCHVODLIST = 59;
    public static final int VOD_RELATED_VIDEOS = 60;
    public static final int VOD_ATTACHED_BUILDING = 61;
    public static final int VOD_ATTACHED_AREA = 62;
    public static final int VOD_PLAY_START = 63;
    public static final int VOD_PLAY_EXIT = 64;
    public static final int VOD_VIDEO_DETAILS = 65;


    //楼盘模块
    public static final int CITY_GROUPS = 75;

    public static final int CHOICE_BUILDING = 80;
    public static final int ROOM_TYPE = 81;
    public static final int ROOM_FORMAT = 82;
    public static final int COUNSELOR_LIST = 83;
    public static final int BUILDING_DETAILS = 84;
    public static final int HOT_REGION = 85;
    public static final int REGION_DETAILS = 86;
    public static final int PICTURE_LIST = 87;
    public static final int BUILDING_ADVER = 88;
    public static final int HOT_BUILDINGS_BY_AREAID = 89;

    //搜索历史
    public static final String FANG_SEARCH_HISTORY = "search_history";
    public static final int SEARCH_HISTORY = 90;
    public static final int SEARCH_RESULT = 91;

    public static final int GET_NOTIFICATION = 100;
    public static final int GET_INTERACTIVE_MSG = 101;
    public static final int CHECK_VERSION_UPDATE = 105;

    public static final int VIDEOS_RECOMMEND = 200;
    public static final int VIDEOS_ATTENTION = 201;

    //视频发布者信息
    public static final int VIDEO_PUBLISHER_INFO=210;
    //视频发布者所有视频
    public static final int PUBLISHER_VIDEOS=211;
    //我的视频
    public static final int MY_VIDEOS=212;


    public static final String LIVE_STATUS_ONLINE = "ON";
    public static final String LIVE_STATUS_OFFLINE = "OFF";

    // 直播列表默认每页条数
    public static final int FETCH_LIVE_PAGE_SIZE_DEFAULT = 5;
    public static final int PAGE_SIZE_DEFAULT = 10;
    public static final int PAGE_SIZE_WATER_FALL = 20;

    public static final String ACTIVITY_RESULT = "activity_result";

    public static boolean isRecordFinish = false;

    public static final int IMPORT_MIN_DURATION_SECONDS = 10;
    /*public static final int RECORD_MIN_DURATION = 15 * 1000;
    public static final int RECORD_MAX_DURATION = 3 * 60 * 1000;*/

    // 横屏视频，宽比高长
    public static final int VIDEO_LANDSCAPE = 0;
    // 竖屏视频，高比宽长
    public static final int VIDEO_PORTRAIT = 1;


    /*************静态的H5页面地址 **************/
    public static final String URL_ABOUT_US = "https://m.yiyanf.com/about.htmls";
    public static final String URL_PROTOCOL = "https://m.yiyanf.com/protocol.htmls";
    public static final String URL_OPERATE = "https://m.yiyanf.com/operate.htmls";
    /***************************************/

    // 极光推送操作序列号
    public static final int JPUSH_SEQUENCE = 0x1024;

    //h5
    public static final String SHARE_WEB = "https://m.yiyanf.com/";
    //楼盘详情
    public static final String BUILDING_WEB = SHARE_WEB+"building/getBuildingDetail.htmls?buildingid=";
    //区域详情
    public static final String REGION_WEB = SHARE_WEB+"area/getAreaDetail.htmls?regionid=";
    //个人主页
    public static final String USER = SHARE_WEB+"user/userCard.htmls?userid=";
    //楼盘更多信息
    public static final String BUILDING_INFO = SHARE_WEB+"building/getBuildingDetailInfo.htmls?buildingid=";
    //楼盘地图
    public static final String BUILDING_MAP = SHARE_WEB+"building/getBuildingMap.htmls?buildingid=";
    //区域更多信息
    public static final String AREA_INFO = SHARE_WEB+"area/getAreaDetailInfo.htmls?regionid=";
    //区域地图
    public static final String AREA_MAP = SHARE_WEB+"building/getBuildingMap.htmls?buildingid=";
    //直播分享页
    public static final String LIVE_WEB = SHARE_WEB+"live/shareLive.htmls?liveid=";
    //视频分享页
    public static final String VEDIO_WEB = SHARE_WEB+"vod/shareVideo.htmls?videoid=";
	
	
	
	
	
	
	
    public static final String INTENT_KEY_SINGLE_CHOOSE = "single_video";
    public static final String DEFAULT_MEDIA_PACK_FOLDER = "txrtmp";      // UGC编辑器输出目录

   // public static final int  MAX_VIDEO_TIME = 180000;
    //******************************** Fang Server 常量定义结束








}
