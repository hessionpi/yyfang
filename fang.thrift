namespace java com.yiyanf.fang.api.model   // java
namespace cocoa fang



/**
* 模型
*
**/

// 公共请求头信息
struct ReqHeader{
    1:  string apiversion,                   // API版本号
    2:  string platform,                     // 平台信息（android、ios、web）
    3:  i64 currenttime,                     // 当前时间戳  毫秒
    4:  i32 expiretime,                      // 秘钥有效期  600 表示 有效期为600秒
    5:  i32 random,                          // 随机数
    6:  string sign,                         // 签名信息
    7:  string userid
}

/**
* 版本更新
**/

// 检查更新
struct ReqCheckVersion{
    1:string action,
    2:ReqHeader header,
    3:string packagename,
    4:i32 versioncode
}
struct ResCheckVersion{
    1:string versionname,
    2:string updatelog,
    3:string downloadurl,
    4:i64 appsize,
    5:string publishtime
}

// 发布App版本
struct ReqPublishNewVersion{
    1:string action,
    2:ReqHeader header,
    3:string versionname,
    4:i32 versioncode,
    5:string updatelog,
	6: i64 appsize,
	7: string downloadurl
}

// 获取App历史版本
struct ReqHistoryVersion{
  1:string action,
  2:ReqHeader header,
  3:i32 pageno,
  4:i32 pagesize
}
struct AppVersionInfo{
    1:string versionname,
    2:i32 versioncode,
    3:string downloadurl,
    4:i64 appsize,
    5:string updatelog,
    6:string publishtime,
	7:string publisher
}
struct ResHistoryVersion{
    1:i32 totalpage,
    2:list<AppVersionInfo> versionlist
}

// 发送短信验证码
struct ReqSendCode{
    1:  string action,                  // 接口名
    2:  ReqHeader header,
    3:  string mobile,                  // 手机号
	4:  string template
}

// 注册
struct ReqRegister{
    1:  string action,                  // 接口名
    2:  ReqHeader header,
    3:  string username,                // 手机号
    4:  string password,                // 用户密码
    5:  string sendcode                 // 短信验证码
}

/**
* 登录
**/
struct ReqLogin{
    1:  string action,                  // 接口名
    2:  ReqHeader header,
    3:  string username,                // 手机号
    4:  string password                // 用户密码
}
// 登录响应返回
struct ResLogin{
    1: string userid,
    2: i32 flag,
    3: string headpic,
    4: string thumbnail,
    5: string nickname,
    6: i32 sex,
	7: string company,
	8: string signature,
	9: bool mobilevisible,
	10: string usersign,
	11: i64 videominduration,
	12: i64 videomaxduration
}
/**
* 获取云通讯签名
**/
struct ReqGetUserSign{
    1:  string action,
    2:  ReqHeader header,
    3:  string visitorid
}
struct ResGetUserSign{
    1: string usersign
}

/**
* 忘记密码
**/
struct ReqResetPassword{
     1:  string action,
     2:  ReqHeader header,
     3:  string mobile,
     4:  string sendcode,
     5:  string newpwd
}

/**
* 完善用户资料
**/
struct ReqUpdateUserinfo{
    1: string action,
    2: ReqHeader header,
    3: string headpic,
    4: string thumbnail,
    5: string nickname,
    6: i32 sex,
    7: string company,
	8: string signature
}

/**
* 身份证认证
**/
struct ReqIdCertification{
    1:  string action,
    2:  ReqHeader header,
    3:  string idcard,
    4:  string name
}

/**
* 认证成为经纪人
**/
struct ReqAgentCertification{
    1:  string action,
    2:  ReqHeader header,
    3:  string headpic,
    4:  string agentcompany,
    5:  list<i32> buildings,
    6:  string certificateurl,
    7:  string visitcardurl
}

/**
* 获取用户信息
**/
struct ReqGetLoginUserinfo{
    1:  string action,
    2:  ReqHeader header,
	3:  i32 userid
	
}
// 用户信息
struct ResGetLoginUserinfo{
    1: string fangid,
    2: string headpic,
    3: string thumbnail,
    4: string nickname,
    5: string mobile,
    6: i32 sex,
	7: string company,
	8: string signature,
	9: bool mobilevisible,
    10: string usersign,
    11: i32 role,
    12: i32 certstatus,
	13: i32 isIdCertification,
	14: i32 isProfession,
	15: i32 isMessage,
    16: string location,
	17: i32 isattention,
    18: i32 collectcount,
    19: i32 attentioncount,
    20: i32 fanscount
}



//请求关注列表

struct ReqGetAttentionList{
    1:  string action,
    2:  ReqHeader header,
	3:  i32 useridcond,
    4:  i32 regionflag,
    5:  i32 buildingflag,
    6:  i32 userflag,
}
struct AttentionRegion{
    1: i32 regionid,
    2: string thumbnail,
    3: string regionname,
    4: i32 regionVideo,
    5: i32 regionLive,
    6: i32 regionbuilding,
    7: i32 counselor,
	8: i32 isattention
}
struct AttentionBuilding{
    1: i32 buildingid,
    2: string thumbnail,
    3: string buildingname,
   	4: i32 buildingVideo,
    5: i32 buildingLive,
    6: i32 discuss,
    7: i32 counselor,
	8: i32 isattention
}
struct AttentionUser{
    1: i32 userid,
    2: string thumbnail,
    3: string nickname,
    4: i32 isagent,
    5: string  homepageurl,
	6:  i32 attentioncount,
	7:  i32 fanscount,
	8:  i32 videocount,
	9:  i32 livecount,
    10: i32 isattention	
}
//获取关注列表

struct ResGetAttentionList{
    1:  list<AttentionRegion> attentionregions,
    2:  list<AttentionBuilding> attentionbuildings,
    3:  list<AttentionUser> attentionusers

}

//请求粉丝列表

struct ReqGetFansList{
    1:  string action,
    2:  ReqHeader header
	3:  i32 useridcond,
    4: i32 pageno,
    5: i32 pagesize

}

//获取粉丝列表
struct ResGetFansList{
    1:  list<AttentionUser> attentionusers,
   2: i64 totalcount,
   3: i32 totalpage

}

/**
* 修改密码
**/
struct ReqChangePassword{
    1: string action,
    2: ReqHeader header,
    3: string oldpwd,
    4: string newpwd
}

/**
* 获取用户名片统计数据
**/
struct ReqGetUserCardCount{
    1:  string action,
    2:  ReqHeader header,
	3:  string userid
}
struct UserCardCount{
    1:  string userid,
	2:  i32 attentioncount,
	3:  i32 fanscount,
	4:  i32 videocount,
	5:  i32 livecount
}
struct ResGetUserCardCount{
    1:  UserCardCount userCardCount
}

/**
* 获取经纪人资料
**/
struct ReqGetCertificationData{
    1:  string action,
    2:  ReqHeader header
	
}
struct Building{
    1: i32 buildingid,
    2: string buildingname
}

struct Certification{
 1:  string idcard,
    2:  string name,
    3:  string headpic,
    4:  string agentcompany,
    5:  list<Building> buildings,
    6:  string certificateurl,
    7:  string visitcardurl
}

struct ResGetCertificationData{
    1:Certification certification
}
/**
* 获取用户是否是经纪人
**/
struct ReqGetIsCertificationUser{
    1:  string action,
    2:  ReqHeader header,
	3: i32 userid
}

struct ShareUserData{
    1:  i32 isCertification,
    2:  string name,
    3:  string headpic,
    4:  list<Building> buildings
  
}

struct ResGetIsCertificationUser{
    1:ShareUserData userdata
}


/**
* 发起直播
**/
struct ReqSponsorLive{
    1: string action,
    2: ReqHeader header
}
struct ResSponsorLive{
    1: i32 needapply
}

/**
* 获取直播申请列表
**/
struct ReqLiveApplyList{
    1:  string action,
    2:  ReqHeader header
}
struct LiveApply{
    1: i32 status,
	2: i64 applyid,
    3: string disablereason,
    4: string title,
    5: i32 areaid,
    6: string area,
    7: i32 buildingid,
    8: string building,
    9: string livestarttime
}
struct ResLiveApplyList{
    1:  list<LiveApply> applyList
}


/**
* 申请直播
**/
struct ReqApplyLive{
    1:  string action,
    2:  ReqHeader header,
    3:  i32 areaid,
	4:  string area,
    5:  i32 buildingid,
	6:  string building,
    7:  string startlivetime,
    8:  string title,
	9:  string livedesc
}



/**
* 请求直播推流地址
**/
struct LiveUserinfo{
    1:  string headpic,                 // 用户头像
    2:  string nickname,                // 用户昵称
    3:  i32 type,
    4:  string frontcover,              // 封面地址
    5:  string location,
    6:  double lng,
    7: double lat
}
struct ReqGetLVBAddr{
    1:  string action,                  // 接口名
    2:  ReqHeader header,
    3:  string groupid,                  // 群组id
    4:  string title,                   // 直播标题
    5:  LiveUserinfo userinfo,            // 用户信息
    6: i32 areaid,
    7: string area,
    8: i32 buildingid,
    9: string building,
    10: string livedesc,
    11:i32 orientation
}
struct ResGetLVBAddr{
    1: i64 liveid,
    2: string pushurl,
    3: string shareurl,
    4: i32 timestamp
}


/**
 * 修改主播在线状态
 **/
struct ReqChangeLiveStatus{
    1: string action,
    2: ReqHeader header,
	3: i64 liveid,
	4: i64 applyid,
    5: string status                   // ON: 上线     OFF:下线
}


/**
* 修改直播点赞计数器
**/
struct ReqAddLikeCount{
    1:  string action,
    2:  ReqHeader header,
    3:  i64 liveid
}
/**
* 获取直播中/回放列表
**/
struct ReqFetchLiveList{
    1: string action,
    2: ReqHeader header,
   	3: i32 areaidcond,
    4: string areanamecond,
    5: i32 buildingidcond,
    6: string buildingnamecond,
	7: i32 useridcond,
	8: i32 livevideotype,
	9: i32 sort,
    10: i32 pageno,
    11: i32 pagesize
}
struct Liveinfo{
    1: i64 liveid,
    2: string userid,		           // 直播用户id
    3: string groupid	,       	  // 群组id
    4: i64 timestamp,	             // 开始推流的时间戳（单位：秒）
    5: i32 viewercount,		    // 在线数量，观众数量
    6: i32 likecount,		        // 点赞数量
	7: i32 sharecount,	            //分享数量
    8: string title,		       // 直播标题
    9: string frontcover,
    10: string playurl,		   // 播放地址
    11: string flv_playurl,		   // flv格式直播观看地址
    12: string hls_playurl,		   // hls格式直播观看地址
    13: string rtmp_playurl,		   //rtmp格式直播观看地址	
    14: string location,
    15: double lng,
    16: double lat,
    17: i32 isattention,
    18: i32 type,
    19: string nickname,
    20: string thumbnail
    21: i32 buildingid,
    22: string building,
    23: i32 areaid,
    24: string area,
    25: i32 hasevent,
    26: string eventid,
    27: i32 orientation
}
struct Tapeinfo{
    1:  string userid,		           // 直播用户用户id
    2:  i64 starttimestamp,       	  // 视频开始时间（直播时的录制开始时间）单位秒
	3:  string publishtime,           //视频发布时间
	4:  i64 duration,                 //时长 单位秒
	5:  i64 filesize,
    6:  i64 endtimestamp,	             // 视频结束时间（直播时的录制结束时间）单位秒
    7:  i32 likecount,		        // 点赞数量
	8:  i32 playcount,		        // 播放次数
	9:  i32 sharecount,	            //分享数量
	10:  i32 discusscount,	            //对该视频的评论次数
	11:  i32 maxviewercount,
    12:  string title,
    13: string livedesc,
    14: string frontcover,
    15: string playurl,		   // 播放地址
    16: string mp4_playurl,		   //mp4播放地址
    17: string flv_playurl,		   //flv播放地址
    18: string hls_playurl,		   //hls播放地址		
    19: i32 videowidth,
    20: i32 videoheight,
    21: string location,
    22: double lng,
    23: double lat,
	24: i64 videoid,            //视频id
	25: i32 isattention,
	26: i32 isfavorite,
    27: i32 type,
    28: string nickname,
    29: string thumbnail
    30: i32 buildingid,
    31: string building,
    32: i32 areaid,
    33: string area,
    34: i32 favoritecount,
    35: i32 orientation
}
struct ResFetchLiveList{
    1:  i64 totalcount,                     // 列表总数（直播列表总数+录制回放列表总数）
    2:  i32 totalpage,
	3:  i32 livecount,
	4:  i64 tapecount,
	5:  list<Liveinfo> livelist,
	6:  list<Tapeinfo> tapelist
}


/**
* 通知业务服务器有群成员进入
**/
struct ReqEnterGroup{
    1:  string action,
    2:  ReqHeader header,
    3:  i64 liveid,
    4:  string groupid,
    5:  string nickname,
    6:  string headpic
}


/**
* 通知业务服务器有群成员退出
**/
struct ReqQuitGroup{
    1:  string action,
    2:  ReqHeader header,
    3:  i64 liveid,
    4:  string groupid
}


/**
* 拉取群成员列表
**/
struct ReqFetchGroupMemberList{
    1:  string action,
    2:  ReqHeader header,
    3:  i64 liveid,
    4:  string groupid,
    5:  i32 topno
}
struct Meminfo{
    1:  string userid,
    2:  string nickname,
    3:  string headpic,
    4:  string thumbnail
}
struct ResFetchGroupMemberList{
    1: i32 totalcount,
    2: i32 visitorcount,
	3: string visitorheadpic,
	4: string visitorthumbnail,
    5:  list<Meminfo> memberlist
}


/*
*   举报
 */
struct ReqReportUser{
    1:  string action,
    2:  ReqHeader header,
    3:  string hostuserid,                      // 被举报人id
    4:  string reason                           // 举报原因
}

/**
* 获取我的直播回放列表
**/
struct ReqGetLivePlay{
    1:  string action,
    2:  ReqHeader header,
    3:  i32 pageno,
    4:  i32 pagesize

}
struct Playback{
1: i64 applyid,
    2: i64 videoid,
    3: string simplestarttime,
    4: i32 areaid,
    5: string area,
    6: i32 buildingid,
    7: string building,
    8: string shareurl,
    9: i32 totalreserved,
    10: string title,
    11: string status
   
}
struct ResGetLivePlay{
    1: i32 totalpage,
    2: i32 totallive,
    3: list<Playback> playbacklist
}


/**
* 删除我的回放
**/
struct ReqDeletePlayback{
    1:  string action,
    2:  ReqHeader header,
    3:  i64 videoid
}

/**
* 最新直播预告
**/
struct ReqLatestLiveForecast{
    1:  string action,
    2:  ReqHeader header,
	3:  i32 liveForecastCount
}
struct Liveforecast{
    1: i64 applyid,
    2: string title,
    3: string livestarttime,
    4: string simplestarttime,
    5: i32 areaid,
    6: string area,
    7: i32 buildingid,
    8: string building,
    9: string liveuserid,
    10: string livenickname,
    11: string liveheadpic,
    12: i32 isagent,
    13: i32 isattention,
    14: string shareurl,
    15: i32 isreserved,
    16: i32 totalreserved,
    17: i32 isplay,
	18: string frontcover,
	19: i64 liveid,
	20: i64 videoid,
	21: Tapeinfo videoinfo
}
struct ResLatestLiveForecast{
    1: list<Liveforecast> latestforecastlist
}

/**
* 所有直播预告
**/
struct ReqAllLiveForecast{
    1:  string action,
    2:  ReqHeader header, 
    3:  i32 pageno,
    4:  i32 pagesize
}
struct ResAllLiveForecast{
    1: list<Liveforecast> liveforecastlist,
    2:  i32 totalpage
}

/**
* 直播预告详情
**/
struct ReqGetLiveForecast{
    1:  string action,
    2:  ReqHeader header, 
    3:  i64 applyid,
}
struct ResGetLiveForecast{
    1: Liveforecast liveforecast
}


/**
* 预约、取消预约观看直播
**/
struct ReqReserve{
    1: string action,
    2: ReqHeader header,
    3: i64 applyid,
    4: i32 flag

}
/**
* 获取直播回放视频详情信息
**/
struct ReqGetTapeDetails{
     1:  string action,
     2:  ReqHeader header,
	 3:  i64 videoid
}
struct ResGetTapeDetails{
    1: Tapeinfo tapeInfo
}
/**
* 获取直播详情信息
**/
struct ReqGetLiveDetails{
     1:  string action,
     2:  ReqHeader header,
	 3:  i64 liveid
}
struct ResGetLiveDetails{
    1: Liveinfo liveinfo
}
/**
* 统计开始播放视频
**/
struct ReqStartPlay{
     1:  string action,
     2:  ReqHeader header,
     3:  i64 videoid,
     4:  i32 vodtype
}
struct ResStartPlay{
    1: i64 playid
}

/**
* 统计停止播放
**/
struct ReqStopPlay{
     1:  string action,
     2:  ReqHeader header,
     3:  i64 videoid,
     4:  i64 playid
}

/**
* 获取视频上传的签名
**/
struct ReqGetVODSign{
    1:  string action,
    2:  ReqHeader header,
	3:  i64 videoid
}
struct ResGetVODSign{
    1: string vodsign
}

/**
* 发布视频
**/
struct ReqPublishVOD{
    1:  string action,
    2:  ReqHeader header,
    3:  string title,
    4:  string videodesc,
    5:  i32 areaid,
	6:  string area,
    7:  i32 buildingid,
	8:  string building,
    9:  i32 classifyid,
    10: double lng,
    11: double lat,
    12: i32 width,
    13: i32 height,
    14: i64 filesize,
    15: i64 duration
}
struct ResPublishVOD{
    1:  string vodsign,
	2:  i64 videoid
}

/**
* 发布视频完成
**/
struct ReqFinishPublishVOD{
    1: string action,
    2: ReqHeader header,
    3: i64 videoid,
	4: string fileid,
	5: string frontcover,
	6: string videourl
}

/**
* 终止发布视频
**/
struct ReqStopPublishVOD{
    1:  string action,
    2:  ReqHeader header,
    3:  i64 videoid
}

/**
* 删除我的上传
**/
struct ReqDeleteUserVOD{
    1:  string action,
    2:  ReqHeader header,
    3:  i64 videoid
}

/**
* 拉取视频列表
**/
struct ReqFetchVodList{
    1: string action,
    2: ReqHeader header,
    3: i32 userid,
    4: i32 citygroupidcond,
    5: i32 areaidcond,
    6: string areanamecond,
    7: i32 buildingidcond,
    8: string buildingnamecond,
    9: i32 housetypeidcond,
    10: i32 categoryidcond,
    11: i32 sort,
	12: string vediostatus,
    13: i32 pageno,
    14: i32 pagesize
}
struct VODinfo{
    1: i64 videoid,
    2: string title,
    3: string frontcover,
    4: string videodesc,
    5: string playurl,
    6: string mp4_playurl,
    7: string flv_playurl,
    8: string hls_playurl,	
    9: i32 videowidth,
    10: i32 videoheight,
    11: string createtime,
    12: i64 duration,
    13: i64 filesize,
    14: i32 playcount,
    15: i32 discusscount,
    16: i32 areaid,
    17: string area,
    18: i32 buildingid,
    19: string building,
    20: i32 avgprice,
    21: string publisherid	,
    22: string nickname,
    23: string headpic,
    24: string thumbnail,
    25: i32 type,
    26: i32 isattention,
    27: i32 isfavorite,
    28: i32 favoritecount,
    29: string vediostatus
}
struct ResFetchVodList{
    1:  i64 totalcount,
    2:  i32 totalpage,
    3:  list<VODinfo> vodlist
}


/**
* 获取精选视频列表
**/
struct ReqFeaturedList{
     1:  string action,
     2:  ReqHeader header,
	 3:  i32 showCount
}
struct ResFeaturedList{
    1: list<VODinfo> featuredvodlist
}

/**
* 相关视频
**/
struct ReqRelatedVideos{
    1: string action,
    2: ReqHeader header,
    3: i64 videoid
}
struct ResRelatedVideos{
    1: list<VODinfo> relatedvodlist
}

/**
* 视频所属楼盘
**/
struct ReqAttachedBuilding{
    1: string action,
    2: ReqHeader header,
    3: i64 videoid
}
struct ResAttachedBuilding{
    1: i32 buildingid,
    2: string albumcover,
    3: string albumthumbnail,
    4: string buildingname,
    5: string address,
    6: i32 avgprice,
    7: string avgpriceunit,
    8: i32 totalprice,
    9: string totalpriceunit,
    10:list<string> labels,
	11: i32 buildingVideo,
    12: i32 buildingLive,
    13: i32 discuss,
    14: i32 counselor
}

/**
* 视频所属区域
**/
struct ReqAttachedArea{
    1: string action,
    2: ReqHeader header,
    3: i64 videoid

}
struct ResAttachedArea{
    1: i32 areaid,
    2: string areaname,
    3: string albumcover,
    4: string albumthumbnail,
    5: i32 rangeminprice,
    6: i32 rangemaxprice,
    7: string rangeunit,
    8: string recommend,
    9: string distance,
    10: list<string> labels,
    11: i32 buildingamount,
    12: i32 videoamount,
    13: i32 liveamount,
	14: i32 counselor
}
/**
* 获取视频详情信息
**/
struct ReqGetVodDetails{
     1:  string action,
     2:  ReqHeader header,
	 3:  i64 videoid
}
struct ResGetVodDetails{
    1: VODinfo vodInfo
}


//获取我的收藏视频
struct ReqGetMineFavoriteVod{
    1: string action,
    2: ReqHeader header,
    3: i32 pageno,
    4: i32 pagesize
}
//返回我的收藏视频
struct ResGetMineFavoriteVod{
    1:  i64 totalcount,
    2:  i32 totalpage,
    3:  list<VODinfo> vodlist
}

/**
* PC web上传视频获取视频截图
**/
struct ReqGetVodSnapshot{
    1: string action,
    2: ReqHeader header,
    3: i64 videoid
}
struct SampleSnapshot{
    1: string imageUrl,
}
struct ResGetVodSnapshot{
    1:  list<SampleSnapshot> sampleSnapshotList
}
/**
* pc web修改视频封面
**/
struct ReqModVODCover{
    1:  string action,
    2:  ReqHeader header,
    3:  i64 videoid,
	4:  string frontcover
}

/**
* 关注
**/
struct ReqAttention{
    1:  string action,
    2:  ReqHeader header,
    3:  string objectid,
    4:  i32 objecttype,
    5:  i32 flag
}

/**
* 获取COS文件上传的签名
**/
struct ReqGetCOSSign{
    1:  string action,
    2:  ReqHeader header
}
struct ResGetCOSSign{
    1: string sign
}

/**
* 分享
**/
struct ReqShare{
    1:  string action,
    2:  ReqHeader header,
    3:  string shareurl,
    4:  string shareobjectid,
    5:  i32 sharetype,
    6:  i32 channel
}

/**
* 收藏
**/
struct ReqFavorite{
    1:  string action,
    2:  ReqHeader header,
    3:  string favobjectid,
    4:  i32 favtype,
    5:  i32 flag
}

/**
* 发布视频选择分类
**/
struct ReqPublishVideoCategory{
    1:  string action,
    2:  ReqHeader header
}
// 视频分类标签
/*enum VideoCategoryLabel{
    NONE = 0,
    AREA = 1,
    BUILDING = 2,
    SHOWROOM = 3,
    OTHERS = 4
}*/
struct VideoCategory{
    1:i32 catid,
    2:string catname,
    3:i32 catlabel
}
struct ResPublishVideoCategory{
    1: list<VideoCategory> categorylist
}


/**
* 获取样板间户型
**/
struct ReqGetShowroomType{
   1:  string action,
   2:  ReqHeader header,
   3:  i32 buildingid
}
struct HouseType{
    1: i32 houtypeid,
    2: string houtypename
}
struct ResGetShowroomType{
    1: list<HouseType> houselist
}





/**
*  获取所有区域
**/
struct ReqGetAreas{
    1:  string action,
    2:  ReqHeader header
}
struct Area{
    1: i32 areaid,
    2: string areaname,
	3: i32 areaprice,
	4: i32 isattention
}
struct Citygroup{
    1: i64 citygroupid,
	2: string citygroupname,
    3: list<Area> arealist
}
struct ResGetAreas{
    1: list<Citygroup> citygrouplist
}


/**
* 获取区域下楼盘信息
**/
/*struct ReqGetBuildings{
    1:  string action,
    2:  ReqHeader header,
    3:  i32 areaid
}

struct ResGetBuildings{
    1:  list<Building> buildinglist
}*/

/**
* 当前经纪人获取负责楼盘
**/
struct ReqGetResponsibleBuildings{
    1:  string action,
    2:  ReqHeader header,
	3:  i32 useridcond
}
struct Respsbuilding{
    1: i32 areaid,
    2: string areaname,
    3: i32 buildingid,
    4: string buildingname
}
struct ResGetResponsibleBuildings{
    1:  list<Respsbuilding> respsbuildings
}


/**
* 评论
**/
struct ReqDiscuss{
    1:  string action,
    2:  ReqHeader header,
    3:  i32 objecttype,
    4:  string objectid,
	5:  string content
}
struct ResDiscuss{
   1: i64 discussid
}

/**
* 回复
**/
struct ReqReply{
    1: string action,
    2: ReqHeader header,
    3: string nickname,
    4: i64 discussid,
    5: i64 replyid,
	6: string content
}
struct ResReply{
   1: i64 replyid
}

/**
* 评论列表
**/
struct ReqGetDiscussList{
    1:  string action,
    2:  ReqHeader header,
    3:  i32 objecttype,
    4:  string objectid,
	5:  i32 pageno,
    6:  i32 pagesize
}
struct Recentreplyinfo{
    1: i64 replyid,
    2: string replyuserid,
    3: string replyname,
    4: string thumbnail,
    5: i32 isagent,
    6: bool isformain,
	7: string replieduserid,
    8: string repliedname,
    9: string content,
    10: string replytime
}
struct Discussinfo{
    1: i64 discussid,
    2: string userid,
    3: i32 isagent,
    4: string nickname,
    5:  string headpic,
    6:  string thumbnail,
	7:  string content,
    8:  string discusstime,
	9:  i32 replycount,
	10:  list<Recentreplyinfo> recentreplylist
}
struct ResGetDiscussList{
    1:  i64 totalcount,
    2:  i32 totalpage,
    3:  list<Discussinfo> discusslist
}
/**
* 回复列表
**/
struct ReqGetReplyList{
    1:  string action,
    2:  ReqHeader header,
    3:  i64 discussid,
	4:  i32 pageno,
    5:  i32 pagesize
}
struct ResGetReplyList{
    1:  i64 totalcount,
    2:  i32 totalpage,
    3:  list<Recentreplyinfo> replylist
}

/**
* 删除评论
**/
struct ReqDeleteDiscuss{
    1:  string action,
    2:  ReqHeader header,
    3:  i64 discussid
}

/**
* 删除回复
**/
struct ReqDeleteReply{
    1:  string action,
    2:  ReqHeader header,
    3:  i64 replyid
}


//请求楼盘列表
struct ReqGetResBuildingList{
  1: string action,                  // 接口名
  2: ReqHeader header,
  3: i32 ordercond,
  4: i32 citygroupidcond,  
  5: i32 regionid, 
  6: i32 pageno,
  7: i32 pagesize
}
struct  BuildingInfo{
  1: i32 buildingid,   
  2: string thumbnail, //缩略图
  3: string buildingName, //楼盘名称
  4: string buildingLoc, //楼盘地址
  5: string citygroupName,
  6: string regionName,
  7: list<string>  buildingTag, 
  8: i32 averagePrice,
  9: i32 housePrice,
  10: i32 isattention,
  11: i32 distance,
  12: string recomreason,
  13: i32 buildingVideo,
  14: i32 buildingLive,
  15: i32 discuss,
  16: i32 counselor
}
//返回楼盘列表
struct ResGetResBuildingList{
   1: i64 totalcount,
   2: i32 totalpage,
   3: list<BuildingInfo> resbuildings
}

//请求楼盘详情
struct ReqGetResBuilding{
  1: string action,                  // 接口名
  2: ReqHeader header,
  3: i32 buildingid  
}

//返回楼盘详情
struct ResGetResBuilding{
  1: i32 regionId,
  2: string buildingPic,  
  3: string buildingName, 
  4: string buildingAlias,
  5: string quota,
  6: list<string> buildingType,
  7: i32  averagePrice,
  8: i32   housePrice ,
  9: list<string>   buildingTag,
  10: string citygroupName,
  11: string regionName,
  12: string regionPrice,
  13: i32  buildingCount,
  14: string  buildingLoc,
  15: string  buildingLocUrl,
  16: string  openBuilding,
  17: string  handHouDate,
  18: string  houseType,
  19: string brand,
  20: string developer,
  21: string MoreUrl,
  22: i32 isattention, 
  23: string  bulid_type,
  24: i32  cycle,
  25: string  fitmentStatus,
  26: string  salesStatus,
  27: string  saleTime,
  28: string salePermit,
  29: string salesAddress,
  30: i32 households,  
  31: string  propertyFee,
  32: string  propertyCompany,
  33: i32  parkings,
  34: string  heatingType,
  35: string  waterType,
  36: string powerType,
  37: string gas,
  38: string plotRatio,
  39: string greeningRate,   
  40: string lng,
  41: string lat,
  42: i32 distance,
  43: string recomreason,
  44: i32 buildingVideo,
  45: i32 buildingLive,
  46: i32 discuss,
  47: i32 counselor
}

//请求楼盘下主力户型
struct ReqGetRoomTypeList{
  1: string action,                  // 接口名
  2: ReqHeader header,
  3: i32 buildingid,
  4: string formatname,
  5: i32 pageno,
  6: i32 pagesize
}

struct HouseTypeData{
   1: i32 houtypeDataId,
   2: string houtypeDataName,
   3: string houtypePic,
   4: string thumbnail,
   5: string houtypeStatus,
   6: string houtype,
   7: i32 houtypeArea,
   8: i32 houtypePrice,
   9: i32 videoCount,
   10: i32  pictureCount,
   11: list<string> houtypeTag
}
//返回楼盘下主力户型
struct ResGetRoomTypeList{
   1: i64 totalcount,
   2: i32 totalpage,
   3: list<HouseTypeData> houseTypeDatas
}


//请求置业顾问列表
struct ReqGetCounselorList{
  1: string action,                  // 接口名
  2: ReqHeader header,
  3: i32 objectType,
  4: i32 objectid,
  5: i32 sort,
  6: i32 pageno,
  7: i32 pagesize
}
struct Counselor{
   1: i32      counselorId,
   2: string   counselorName,
   3: string   thumbnail,
   4: bool     attention,
   5:list<string>  contributeList
}

//返回置业顾问列表

struct ResGetCounselorList{
   1: i64 totalcount,
   2: i32 totalpage,
   3: list<Counselor> Councilors,
   4: string councilorStarUrl
}

//请求楼盘格局
struct ReqGetShowroomFormat{
  1: string action,                  // 接口名
  2: ReqHeader header,
  3: i32 buildingid,
}



//返回楼盘格局
struct ResGetShowroomFormat{
    1: list<string> formatnamelist
}


struct PictureUrl{
   1: string   imageurl,
   2: string   thumbnail
}

//请求图片列表
struct ReqGetPictureList{
  1: string action,                  // 接口名
  2: ReqHeader header,
  3: i32 areaidcond,
  4: i32 buildingidcond,
  5: i32 housetypeidcond
}

struct PictureType{
   1: string   picturetypename,
   2: list<PictureUrl> pictures
}


//返回图片列表
struct ResGetPictureList{
    1: list<PictureType> picturetypes
}


//请求区域列表
struct ReqGetResRegionList{
  1: string action,                  // 接口名
  2: ReqHeader header,
  3: i32 cityid,
  4: i32 sort
}
struct RegionInfo{
  1: i32 regionid,   
  2: string thumbnail,
  3: string regionName,
  4: string regionPrice,
  5: string regionPriceUnit,
  6: string distance,
  7: list<string> regionTag,
  8: i32 isattention,
  9: i64 citygroupid,
  10: string citygroupname,
  11: string reason,
  12: string regionLoc,
  13: i32 regionVideo,
  14: i32 regionLive,
  15: i32 regionbuilding,
  16: i32 counselor
}
//返回区域列表
struct ResGetResRegionList{
   1: list<RegionInfo> resregions
}


//请求区域详情
struct ReqGetResRegion{
  1: string action,                  // 接口名
  2: ReqHeader header,
  3: i32 regionid
}

//返回区域详情
struct ResGetResRegion{
  1: string regionName,
  2: string regionAlias,
  3: string quota,
  4: string recomreason,
  5: string regionPrice,
  6: string regionPriceUnit,
  7: list<string> regionTag,
  8: string regionLoc ,
  9: string buildingLocUrl ,
  10: string  distance,
  11: string railtransit,
  12: i32 regionarea ,
  13: i32  population ,
  14: string MoreUrl ,
  15: i32 isattention,
  16: string	advantage,
  17: string	feature,
  18: string	areaLevel,
  19: string	scenicSpot,
  20: string	life,
  21: string	expressway,
  22: string	school,
  23: string	hospital,
  24: i32	acreage,
  25: string	climate,
  26: string	introduction,
  27: i32 regionVideo,
  28: i32 regionLive,
  29: i32 discuss,
  30: i32 counselor,
  31: string regionPic,
  32: string citygroupname
}

//请求轮播图，垂直滚动广告条

struct ReqGetBuildingAdverList{
  1: string action,                  // 接口名
  2: ReqHeader header
}

struct BuildingSelection{
  1: string selectiontitle,   
  2: string selectiontime,
  3: string selectionauthor,
  4: string contenturl
}

//返回轮播图，垂直滚动广告条
struct ResGetBuildingAdverList{
   1: list<string> pictureturls,
   2: list<string> picturecontents,
   3: list<BuildingSelection> buildingselections
}

//获取城市群下热点区域
struct ReqGetHotAreasByCityGroupId{
  1: string action,                  // 接口名
  2: ReqHeader header,
  3: i64 citygroupid,
  4: i32 showcount
}

//返回热点区域列表
struct ResGetHotAreasByCityGroupId{
   1: i64 citygroupid,
   2: string citygroupname,
   3: list<Area> arealist
}

//获取某一区域下的热点楼盘
struct ReqGetHotBuildingsByAreaId{
  1: string action,                  // 接口名
  2: ReqHeader header,
  3: i32 areaid,
  4: i32 showcount
}
//返回热点楼盘列表
struct ResGetHotBuildingsByAreaId{
  1: list<BuildingInfo> buildinglist
}

// 所有城市群
struct ReqCityGroup{
  1: string action,                  
  2: ReqHeader header
}

struct ResCityGroup{
  1: list<City> citygrouplist
}






//获取热门搜索
struct ReqGetHotSearch{
  1: string action,                  // 接口名
  2: ReqHeader header,
}
//返回热门搜索
struct ResGetHotSearch{
  1: list<string> hotsearchlist
}

// 获取搜索结果
struct ReqGetSearchResult{
    1: string action,
    2: ReqHeader header,
    3: string condition,
    4: i32 pageno,
    5: i32 pagesize
}

//返回搜索结果
struct ResGetSearchResult{
    1:  i64 totalcount,
    2:  i32 totalpage,
    3:  list<VODinfo> vodlist
}

/**
* 首页大集合
**/
struct ReqHomePage{
    1: string action,
    2: ReqHeader header
}
struct Banner{
    1:i32 bannerid,
    2:string bannerimg,
    3:string bannerlink
}
struct Article{
    1:i32 articleid,
    2:string articletitle,
    3:i32 authorid,
    4:string authorname,
    5:string authoravatar,
    6:string summary,
    7:string orignurl,
    8:string content,
    9:list<string> imgs
}
struct Circle{
     1:i32 circleid,
     2:string circlename,
     3:i32 userid,
     4:string username,
     5:string thumb,
     6:string content,
     7:i32 replycount
 }
 struct Consultant{
     1:i32 consultantuid,
     2:string consultantname,
     3:string consultantthumb,
     4:i32 respbuildingid,
     5:string respbuildingname,
     6:i32 isattention,
     7:string homeurl
 }
 struct ResHomePage{
     1:list<Banner> adbanners,
     2:list<Liveinfo> wonderfullives,
     3:list<Tapeinfo> wonderfultapes,
     4:list<Article> featuredarticle,
     5:list<VODinfo> wonderfulvideos,
     6:Circle latestcircle,
     7:list<Consultant> consultants
 }

 /**
 * 获取通知列表
 **/
 struct ReqGetNotifications{
     1: string action,
     2: ReqHeader header,
     3:i32 pageno,
     4:i32 pagesize
 }
 struct FangNotification{
     1:i32 msgid,
     2:i32 type,
     3:string arg,
     4:string msgtitle,
     5:string msgcreattime,
     6:string msgcontent
 }
 struct ResGetNotifications{
     1:list<FangNotification> notificationlist,
     2:i32 totalpage
 }

/**
 * 获取互动消息
**/
struct ReqGetInteractiveMsgs{
     1: string action,
     2: ReqHeader header,
     3:i32 pageno,
     4:i32 pagesize
}
struct InteractiveMsg{
    1:i64 discussid,
    2:i64 replyid,
    3:string thumbnail,
    4:string nickname,
    5:string content,
    6:string createtime,
    7:string origincontent,
    8:string source,
    9:i64 sourceid
}
struct ResGetInteractiveMsgs{
    1:list<InteractiveMsg> interactivelist,
    2:i32 totalpage
}

//请求用户预约列表
struct ReqMineLiveReservation{
    1:  string action,
    2:  ReqHeader header
    3: i32 pageno,
    4: i32 pagesize

}

//获取用户预约列表
struct ResMineLiveReservation{
    1: list<Liveforecast> livereservationlist,
    2:  i32 totalpage

}

//======================== V1.2 接口==================================

/**
* 推荐视频列表
**/
struct ReqRecommendVideos{
    1:string action,
    2:ReqHeader header,
    3:i32 sort,
    4:i32 pageno,
    5:i32 pagesize
}
struct SimpleVideo{
    1:i64 videoid,
    2:string frontcover,
    3:i32 coverwidth,
    4:i32 coverheight,
    5:string title
    6:string category,
    7:bool isagent,
    8:string publisherid,
    9:string publisheravatar,
    10:string publishername
}
struct ResRecommendVideos{
    1:i32 totalpage,
    2:list<SimpleVideo> recommendvideos
}

/**
* 关注的人、楼盘、区域发布视频列表
**/
struct ReqAttentionVideos{
    1:string action,
    2:ReqHeader header,
    3:i32 sort,
    4:i32 pageno,
    5:i32 pagesize
}
struct ResAttentionVideos{
    1:i32 totalpage,
    2:list<SimpleVideo> attentionvideos
}

/**
* 视频分类
**/
struct ReqVideoClassify{
    1:string action,
    2:ReqHeader header
}
struct Classification{
    1:i32 classifyid,
    2:string classifyname
}
struct ResVideoClassify{
    1:list<Classification> classifylist
}

/**
* 获取区域（城市）
**/
struct ReqGetRegions{
    1:string action,
    2:ReqHeader header
}
struct City{
  1:i32 cityid,
  2:string cityname,
  3:double lng,
  4:double lat
}
struct Region{
    1:string letter,
    2:list<City> citylist
}
struct ResGetRegions{
    1:list<Region> regionlist
}

/**
* 获取楼盘
**/
struct ReqGetBuildings{
    1:  string action,
    2:  ReqHeader header,
    3:  i32 cityid
}
struct BuildingGroup{
    1:string letter,
    2:list<Building> buildinglist
}
struct ResGetBuildings{
    1:  list<BuildingGroup> buildinggroups
}


/**
* 获取视频详细信息
**/
struct ReqGetVideoDetails{
    1:  string action,
    2:  ReqHeader header,
    3:  i64 videoid
}
struct ResGetVideoDetails{
    1:string title,
    2:string frontcover,
    3:i32 videowidth,
    4:i32 videoheight,
    5:string videodesc,
    6:string playurl,
    7:i32 cityid,
    8:string cityname,
    9:i32 buildingid,
    10:string buildingnme,
    11:string publisherid,
    12:bool isagent,
    13:string nickname,
    14:string thumbnail,
    15:string publishermobile,
    16:bool isattention,
    17:bool isfavorite,
    18:i32 favoritecount,
    19:i32 sharecount,
    20:i32 discusscount
}

/**
* 根据城市名获取城市id
**/
struct ReqGetCityIdByName{
    1: string action,
    2: ReqHeader header,
    3:string province,
    4:string city
}
struct ResGetCityIdByName{
    1:i32 cityid
}

/**
* 获取视频发布者信息
**/
struct ReqGetVideoPublisherInfo{
    1: string action,
    2: ReqHeader header,
    3:string publisherid
}
struct ResGetVideoPublisherInfo{
    1:string username,
    2:string mobile,
    3:string thumbnail,
    4:string avatar,
    5:string signature,
    6:string visiblemobile,
    7:bool isagent,
    8:bool isattention,
    9:bool isrealname,
    10:bool isprofessional,
    11:bool isagency,
    12:i32 attentions,
    13:i32 fans,
    14:i32 favourite,
    15:list<Respsbuilding> sellbuildings
}

/**
* 获取视频发布者所有视频
**/
struct ReqGetPublishVideos{
    1:string action,
    2:ReqHeader header,
    3:string publisherid,
    4:i32 pageno,
    5:i32 pagesize
}
struct ResGetPublishVideos{
    1:i32 amount,
    2:i32 totalpage,
    3:list<SimpleVideo> videolist
}

/**
* 公开或者取消公开手机号码
*
**/
struct ReqExposureMobile{
    1:string action,
    2:ReqHeader header,
    3:i32 visible
}

/**
* 获取我发布的视频列表
**/
struct ReqGetMyVideos{
    1:string action,
    2:ReqHeader header,
    3:i32 pageno,
    4:i32 pagesize
}
struct MineVideoInfo{
    1:i64 videoid,
    2:string frontcover
    3:i32 coverwidth,
    4:i32 coverheight,
    5:string title,
    6:i32 playtimes,
    7:i32 favoritecount,
    8:i32 sharecount
}
struct ResGetMyVideos{
    1:i32 amount,
    2:i32 totalpage,
    3:list<MineVideoInfo> videolist
}









//========================V1.2 接口end =================================
