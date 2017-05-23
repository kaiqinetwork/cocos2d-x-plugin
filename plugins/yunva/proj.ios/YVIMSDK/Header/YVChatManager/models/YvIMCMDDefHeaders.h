//
//  YvIMModelHeaders.h
//  YvIMSDKAPI
//
//  Created by liwenjie on 15-3-9.
//  Copyright (c) 2015年 com.yunva.yaya. All rights reserved.
//

//#import "IMSDK.h"
//#import "yvpacket_sdk.h"

//云消息SOURCE
#define  CLOUDMSG_SYSTEM          @"SYSTEM"
#define  CLOUDMSG_PUSH            @"PUSH"
#define  CLOUDMSG_FRIEND          @"P2P"
#define  CLOUDMSG_GROUP           @"GROUP"
//// 调试状态
//typedef NS_ENUM(int, YvSendVoiceMessageChannel)
//{
//    YvSendVoiceMessageChannel_None      = 0,//没有频道在发语音消息
//    YvSendVoiceMessageChannel_World     = 1,//世界频道在发语音消息
//    YvSendVoiceMessageChannel_Group     = 2,//世界频道在发语音消息
//    YvSendVoiceMessageChannel_P2P       = 30,//私聊频道在发语音消息
//    
//};
//发送错误码
typedef enum
{
    YvErrorCodeUnknown              =  1,       //unknown  error
    YvErrorCodeUploadFile           =  1901,    //upload file fail
    YvErrorCodeDownFile             =  1902,    //down file fail
    YvErrorCodeDomainFail           =  1903,    //url error
    YvErrorCodeUploadRepeat         =  1904,    //upload file reqeat
    YvErrorCodeDownRepeat           =  1905,    //down file reqeat
    
    YvErrorCodeRecordPower          =  1911,    //record audio error permission denied(ios > ios7)
    YvErrorCodeRecordEncode         =  1912,    //record audio create encdoe fail
    YvErrorCodeRecordPathError      =  1913,    //record audio filename error
    
    YvErrorCodePlayFileNull         =  1921,    //play file null
    YvErrorCodePlayFileSmall        =  1922,    //play file size samll  < 192B
    YvErrorCodePlayUrlError         =  1923,    //url domain analysis fail
    
    YvErrorCodeSpeechhttpFail       =  1931,    //speech http req fail
    YvErrorCodeSpeechFileFail       =  1932,    //speech file fail
    YvErrorCodeSpeechAuthFail       =  1933,    //speech auth fail
}yverror;

//消息发送状态
enum p_sendMsg_state
{
    p_sendMsg_sending = 0,//正在发送中
    p_sendMsg_sendSuccess = 1,//发送成功
    p_sendMsg_sendFail = 2,//发送失败
    
    P_SendMsg_downLoading = 3,//下载中
    P_SendMsg_downLoadFinish = 4,//下载完成
    P_SendMsg_downLoadFail = 5,//下载出错

};
typedef enum p_sendMsg_state P_SendMsg_State;

enum e_addfriend_affirm{
    af_refuse    = 0, //拒绝
    af_agree     = 1, //同意加好友(单项)
    af_agree_add = 2, //同意加好友并加对方为好友(双向)
};
typedef  enum e_addfriend_affirm E_Addfriend_Affirm;

enum e_delfriend{
    df_exit_in_list     = 0, //不从好友列表中删除
    df_remove_from_list = 1, //从好友列表中删除
};
typedef  enum e_delfriend E_Delfriend;

//操作好友黑名单
enum e_oper_friend_act
{
    oper_add_blacklist = 3, //加入黑名单
    oper_del_blacklist = 4, //删除黑名单
};
typedef  enum e_oper_friend_act E_Oper_Friend_Act;

//好友状态
enum e_friend_status{
    fs_offline = 0, //下线
    fs_online  = 1, //在线
};
typedef  enum e_friend_status E_Friend_Status;

//语音识别类型
enum yvimspeech_language
{
    im_speech_zn = 1, //中文
    im_speech_ct = 2, //粤语
    im_speech_en = 3, //英语
};
typedef  enum yvimspeech_language Yvimspeech_Language;

//发送结果类型
enum e_chat_msgsend_result {
    e_msg_send_fail = 0, //发送失败
    e_msg_send_succ = 1, //发送成功
};
typedef  enum e_chat_msgsend_result E_Chat_Msgsend_Result;

//消息类型
enum e_chat_msgtype {
    chat_msgtype_image = 0, //图像文件
    chat_msgtype_audio = 1, //音频文件
    chat_msgtype_text  = 2, //文本消息
};
typedef  enum e_chat_msgtype E_Chat_Msgtype;

//群验证方式
enum e_groupverify {
    gv_allow	  =	1,	//不需要验证
    gv_answer	  =	2,	//答题验证
    gv_audit	  =	3,	//管理员审核
    gv_not_allow  =	4,	//群不允许加入
};
typedef  enum e_groupverify E_GroupVerify;

//同意与拒绝加群
enum e_joingroup {
    jg_refuse = 0, //拒绝
    jg_agree  = 1, //同意
};
typedef  enum e_joingroup E_Joingroup;

//被邀请用户返回结果
enum e_group_invite {
    gi_refuse = 0, //拒绝
    gi_agree  = 1, //同意
};
typedef  enum e_group_invite E_Group_Invite;

//群成员角色
enum e_group_role{
    gr_owners	= 2, //群所有者
    gr_admin	= 3, //群管理者
    gr_number	= 4, //群成员
    gr_visitor	= 10,//群游客
};
typedef  enum e_group_role E_Group_Role;

//群成员是否在线
enum group_member_online {
    gm_status_online = 1,    //在线
};
typedef  enum group_member_online E_Group_Member_Online;

//消息提示
enum e_msg_set{
    em_auto_pop		= 1,	//自动弹出消息
    em_prompt		= 2,	//接收并提示消息
    em_show_amount	= 3,	//显示条数
    em_not_prompt	= 4,	//不提示消息
};
typedef  enum e_msg_set E_Msg_Set;

//队伍模式
enum troops_mode
{
    troops_mode_grab		= 1,	//抢麦模式
    troops_mode_chairman	= 2,	//主席模式
    troops_mode_order		= 3,	//麦序
};
typedef  enum troops_mode E_Troops_Mode;

//麦序模式
enum troopsm_mode
{
    troops_grab		= 1,	//抢麦模式
    troops_chairman	= 2,	//主席模式
    troops_order	= 3,	//麦序
};
typedef  enum troopsm_mode E_Troopsm_Mode;

//队伍聊天内容类型
enum chat_type
{
    chat_image = 0,     //图片
    chat_audio = 1,     //语音
    chat_text = 2,      //文本
};
typedef  enum chat_type E_Chat_Type;

//角色
enum troops_role
{
    troops_role_member = 0,// 非队长
    troops_role_leader,// 队长
};
typedef  enum troops_role E_Troops_Role;

//禁言
enum troops_gag
{
    troops_gaged = 0,// 禁言
    troops_gag_cancel = 1,// 非禁言
};
typedef  enum troops_gag E_Troops_Gag;

//控麦、放麦
enum troops_wheat_control
{
    troops_wheat_dismiscontrol = 0,// 放麦
    troops_wheat_getcontrol = 1,//控麦
};
typedef  enum troops_wheat_control E_Troops_Wheat_Control;

//禁麦、开麦
enum troops_wheat_disable
{
    troops_wheat_micdisable = 0,// 禁麦
    troops_wheat_micenable = 1,//开麦
};
typedef  enum troops_wheat_disable troops_Wheat_Disable;

//网络状态
enum l_net_state
{
    yv_net_disconnect = 0,
    yv_net_connect = 1,
};
typedef  enum l_net_state L_Net_State;

//识别类型
enum yvspeech
{
    speech_file = 0,              //文件识别
    speech_file_and_url = 1,      //文件识别返回url
    speech_url = 2,               //url识别
    speech_live = 3,              //实时语音识别(未完成)
};
typedef  enum yvspeech R_Speech_Type;

//群用户列表
#define IM_GROUP_USERLIST_NOTIFY        0x13000

//创建群
#define IM_GROUP_CREATE_REQ             0x13002

//创建群回应
#define IM_GROUP_CREATE_RESP            0x13003

//搜索群
#define IM_GROUP_SEARCH_REQ             0x13004

//搜索群回应
#define IM_GROUP_SEARCH_RESP            0x13005

//加入群
#define IM_GROUP_JOIN_REQ               0x13006

//加入群通知
#define IM_GROUP_JOIN_NOTIFY            0x13007

//同意拒绝加群
#define IM_GROUP_JOIN_ACCEPT            0x13008

//申请加群返回结果通知
#define IM_GROUP_JOIN_RESP              0x13009

//退群
#define IM_GROUP_EXIT_REQ               0x13010

//退群响应
#define IM_GROUP_EXIT_RESP              0x13011

//退群通知
#define IM_GROUP_EXIT_NOTIFY            0x13012

//修改群属性
#define IM_GROUP_MODIFY_REQ             0x13013

//修改群属性响应
#define IM_GROUP_MODIFY_RESP            0x13014

//转移群主请求
#define IM_GROUP_SHIFTOWNER_REQ         0x13015

//转移群主通知
#define IM_GROUP_SHIFTOWNER_NOTIFY      0x13016

//转移群主响应
#define IM_GROUP_SHIFTOWNER_RESP        0x13017

//踢除群成员
#define IM_GROUP_KICK_REQ               0x13018

//踢除群成员响应
#define IM_GROUP_KICK_RESP              0x13020

//踢除群成员通知
#define IM_KGROUP_KICK_NOTIFY           0x13019

//邀请好友入群
#define IM_GROUP_INVITE_REQ             0x13021

//邀请通知
#define IM_GROUP_INVITE_NOTIFY          0x13022

//被邀请者同意或拒绝群邀请
#define IM_GROUP_INVITE_ACCEPT          0x13023

//邀请好友入群响应
#define IM_GROUP_INVITE_RESP            0x13024

//邀请好友入群回应
#define IM_GROUP_INVITE_RESPON          0x13041

//被邀请者同意或拒绝群邀请响应
#define IM_GROUP_INVITE_ACCEPT_RESP     0x13042

//设置群成员角色请求
#define IM_GROUP_SETROLE_REQ            0x13025

//设置群成员角色通知
#define IM_GROUP_SETROLE_NOTIFY         0x13027

//设置群成员角色返回
#define IM_GROUP_SETROLE_RESP           0x13026

//解散群请求
#define IM_GROUP_DISSOLVE_REQ           0x13028

//解散群响应
#define IM_GROUP_DISSOLVE_RESP          0x13029

//管理员修改他人名片
#define IM_GROUP_SETOTHER_REQ           0x13030

//修改他人名片通知
#define IM_GROUP_SETOTHER_NOTIFY        0x13031

//修改他人名片返回
#define IM_GROUP_SETOTHER_RESP          0x13032


//群属性通知(群列表)
#define IM_GROUP_PROPERTY_NOTIFY        0x13033

//群成员上线
#define IM_GROUP_MEMBER_ONLINE          0x13034

//新成员加入群
#define IM_GROUP_USERJOIN_NOTIFY        0x13035

/*==============================================*/

//设置设备信息
#define IM_DEVICE_SETINFO               0x11012

//云娃登录请求
#define IM_LOGIN_REQ                    0x11000

//云娃登录返回
#define IM_LOGIN_RESP                   0x11001

//cp账号登录请求
#define IM_THIRD_LOGIN_REQ				0x11002

//获取第三方账号信息请求
#define IM_GET_THIRDBINDINFO_REQ             0x11014

//获取第三方账号信息返回
#define IM_GET_THIRDBINDINFO_RESP             0x11015

//重连成功通知
#define IM_RECONNECTION_NOTIFY         0x11013

//注销
#define IM_LOGOUT_REQ                   0x11004

//cp账号登录返回
#define IM_THIRD_LOGIN_RESP				0x11003

//请求添加好友
#define IM_FRIEND_ADD_REQ               0x12000

//请求添加好友消息发送回应
#define IM_FRIEND_ADD_RESPOND    0x12025

//添加好友回应，对方接受/拒绝
#define IM_FRIEND_ADD_RESP              0x12001

//好友请求通知
#define IM_FRIEND_ADD_NOTIFY            0x12002

//同意添加好友
#define IM_FRIEND_ADD_ACCEPT            0x12003

//同意添加好友回应
#define IM_FRIEND_ACCEPT_RESP           0x12004

//删除好友请求
#define IM_FRIEND_DEL_REQ               0x12005

//删除好友回应
#define IM_FRIEND_DEL_RESP              0x12006

//删除好友通知
#define IM_FRIEND_DEL_NOTIFY            0x12007

//搜索好友请求
#define IM_FRIEND_SEARCH_REQ            0x12018

//搜索好友回应
#define IM_FRIEND_SEARCH_RESP           0x12019

//推荐好友
#define IM_FRIEND_RECOMMAND_REQ         0x12008

//推荐好友回应
#define IM_FRIEND_RECOMMAND_RESP        0x12009

//好友操作请求(黑名单)
#define IM_FRIEND_OPER_REQ              0x12010

//好友操作回应(黑名单)
#define IM_FRIEND_OPER_RESP             0x12011

//好友列表推送
#define IM_FRIEND_LIST_NOTIFY           0x12012

//好友列表查询请求
#define IM_FRIEND_LIST_REQ		     0x12028

//好友列表查询回应
#define IM_FRIEND_LIST_RESP		     0x12029

//黑名单列表推送
#define IM_FRIEND_BLACKLIST_NOTIFY      0x12013

//黑名单列表查询请求
#define IM_FRIEND_BLACKLIST_REQ		     0x12030

//黑名单列表查询回应
#define IM_FRIEND_BLACKLIST_RESP		 0x12031

/*最近联系人推送*/
#define IM_FRIEND_NEARLIST_NOTIFY       0x12014

//最近联系人列表查询请求
#define IM_FRIEND_NEARLIST_REQ		0x12032

//最近联系人列表查询回应
#define IM_FRIEND_NEARLIST_RESP		0x12033

/*好友状态推送*/
#define IM_FRIEND_STATUS_NOTIFY         0x12015

//设置好友信息
#define IM_FRIEND_INFOSET_REQ           0x12016

//设置好友信息回应
#define IM_FRIEND_INFOSET_RESP          0x12017

//获取频道信息请求
#define IM_CHANNEL_GETINFO_REQ			0x16000

//登录 注:登录账号传入了通配符，会直接登录， 不需要再调此登录
#define IM_CHANNEL_LOGIN_REQ           0x16007

#define IM_CHANNEL_LOGIN_RESP           0x16008

//退出频道
#define IM_CHANNEL_LOGOUT_REQ           0x16009

//获取频道信息返回
#define IM_CHANNEL_GETINFO_RESP			0x16001

//获取频道历史信息请求
#define IM_CHANNEL_HISTORY_MSG_REQ      0x16005

//频道获取历史消息返回
#define IM_CHANNEL_HISTORY_MSG_RESP     0x16006

//发送频道文字消息请求
#define IM_CHANNEL_TEXTMSG_REQ          0x16002

//发送频道语音消息
#define IM_CHANNEL_VOICEMSG_REQ         0x16003

//发送消息回应
#define IM_CHANNEL_SENDMSG_RESP    0x16010

//修改通配符
#define IM_CHANNEL_MODIFY_REQ     0x16011

//修改通配符回应
#define IM_CHANNEL_MODIFY_RESP   0x16012

//频道收到消息通知
#define IM_CHANNEL_MESSAGE_NOTIFY       0x16004

//开始录音(最长60秒)
#define	IM_RECORD_STRART_REQ            0x19000

//停止录音请求  回调返回录音文件路径名
#define	IM_RECORD_STOP_REQ              0x19001

#define IM_GET_SDKINFO_RESP             0x11018


//停止录音返回  回调返回录音文件路径名
#define	IM_RECORD_STOP_RESP             0x19002

//播放录音请求
#define	IM_RECORD_STARTPLAY_REQ         0x19003

//播放语音完成
#define	IM_RECORD_FINISHPLAY_RESP       0x19004

//播放URL下载进度
#define IM_RECORD_PLAY_PERCENT_NOTIFY   0x19016

//停止播放语音
#define	IM_RECORD_STOPPLAY_REQ          0x19005

//开始语音识别
#define IM_SPEECH_START_REQ             0x19006

//停止语音识别
#define IM_SPEECH_STOP_REQ              0x19007

//设置语音识别语言
#define IM_SPEECH_SETLANGUAGE_REQ       0x19008

//停止语音识别返回
#define IM_SPEECH_STOP_RESP				0x19009

//上传文件
#define IM_UPLOAD_FILE_REQ				0x19010

//上传文件回应
#define IM_UPLOAD_FILE_RESP		        0x19011

//下载文件请求
#define IM_DOWNLOAD_FILE_REQ            0x19012

//下载文件回应
#define IM_DOWNLOAD_FILE_RESP           0x19013

//设置录音信息
#define	IM_RECORD_SETINFO_REQ	        0x19014

//录音声音大小通知
#define	IM_RECORD_VOLUME_NOTIFY	         0x19015

//获取URL对应的文件路径
#define IM_GET_CACHE_FILE_REQ           0x19018

//获取URL对应的文件路径返回
#define IM_GET_CACHE_FILE_RESP           0x19019

//清除缓存
#define IM_CACHE_CLEAR    0x19020

//上传地理位置请求
#define IM_UPLOAD_LOCATION_REQ			0x19030

//上传地理位置请求返回
#define IM_UPLOAD_LOCATION_RESP			0x19031

//获取位置信息请求
#define IM_GET_LOCATION_REQ				0x19032

//获取sdk信息
#define IM_GET_SDKINFO_REQ              0x11017   //skd  9.11

//获取地理信息请求返回
#define IM_GET_LOCATION_RESP			0x19033

//搜索（附近）用户
#define IM_SEARCH_AROUND_REQ			0x19034

//搜索（附近）用户
#define IM_SEARCH_AROUND_REQ			0x19034

//搜索（附近）用户返回
#define IM_SEARCH_AROUND_RESP			0x19035

//获取个人信息
#define IM_USER_GETINFO_REQ             0x19013


//获取个人信息回应
#define IM_USER_GETINFO_RESP            0x12020   //本来是0x19014  9.15 mark by czw 改为0x12020

//好友聊天-文本
#define IM_CHAT_FRIEND_TEXT_REQ         0x14000

//好友聊天-图像
#define IM_CHAT_FRIEND_IMAGE_REQ        0x14001

//好友聊天 - 语音
#define IM_CHATI_FRIEND_AUDIO_REQ       0x14002

//好友聊天通知
#define IM_CHAT_FRIEND_NOTIFY           0x14003

//好友聊天发送回应
#define IM_CHATT_FRIEND_RESP            0x14004

//群聊 - 文本
#define IM_CHAT_GROUP_TEXT_REQ          0x14006

//群聊 -  图片
#define IM_CHAT_GROUP_IMAGE_REQ         0x14007

//群聊 - 语音
#define IM_CHATA_GROUP_AUDIO_REQ        0x14008

//取消发送（目前仅支持图片消息）
#define IM_CHAT_CANCEL_SEND_REQ         0x14012

//群聊天推送
#define IM_CHAT_GROUP_NOTIFY            0x14009

//群聊消息发送响应
#define IM_CHAT_GROUPMSG_RESP           0x14010

//发送进度（目前仅支持图片消息）
#define IM_CHAT_MSG_SEND_PERCENT_NOTIFY 0x14011

//云消息回应通知
#define IM_CLOUDMSG_LIMIT_NOTIFY        0x15005

//云消息通知
#define IM_CLOUDMSG_NOTIFY              0x15002

//云消息确认
#define IM_CLOUDMSG_READ_STATUS         0x15007

//离线消息忽略
#define IM_CLOUDMSG_IGNORE_REQ          0x15008

//PUSH消息
#define IM_MSG_PUSH                     0x15006


//创建队伍
#define CMDNO_TROOPS_CREATE_REQ         0x17001

//创建队伍响应
#define CMDNO_TROOPS_CREATE_RESP        0x17002

//登陆队伍
#define CMDNO_TROOPS_LOGIN_REQ          0x17003

//登陆队伍响应
#define CMDNO_TROOPS_LOGIN_RESP         0x17004

//登出队伍
#define CMDNO_TROOPS_LOGOUT_REQ         0x17005

//登出队伍响应
#define CMDNO_TROOPS_LOGOUT_RESP        0x17006

//用户登录通知
#define CMDNO_TROOPS_USERLOGIN_NOTIFY   0x17007

//用户登出通知
#define CMDNO_TROOPS_USERLOGOUT_NOTIFY  0x17008

//用户列表通知
#define CMDNO_TROOPS_USERLIST_NOTIFY    0x17009

//发送文字
#define CMDNO_TROOPS_SENDTEXT_REQ       0x17010

//发送语音
#define CMDNO_TROOPS_SENDAUDIO_REQ      0x17011

//发送图片
#define CMDNO_TROOPS_SENDIMAGE_REQ      0x17012

//发送聊天消息响应
#define CMDNO_TROOPS_SENDCHATMSG_RESP   0x17013

//聊天信息通知
#define CMDNO_TROOPS_CHATMSG_NOTIFY     0x17014

//队长转让
#define CMDNO_TROOPS_APPOINT_REQ        0x17015

//队长转让回应
#define CMDNO_TROOPS_APPOINT_RESP       0x17016

//队长转让通知
#define CMDNO_TROOPS_APPOINT_NOTIFY     0x17017

//禁言
#define CMDNO_TROOPS_GAG_REQ            0x17018

//禁言回应
#define CMDNO_TROOPS_GAG_RESP           0x17019

//禁言通知
#define CMDNO_TROOPS_GAG_NOTIFY         0x17020

//抢麦操作
#define CMDNO_TROOPS_ROB_WHEAT_REQ      0x17021

//抢麦回应
#define CMDNO_TROOPS_ROB_WHEAT_RESP     0x17022

//下麦操作
#define CMDNO_TROOPS_PUT_WHEAT_REQ      0x17023

//下麦操作
#define CMDNO_TROOPS_PUT_WHEAT_RESP     0x17024

//麦序列表通知(当麦序列表有改变时通知)
//麦序列表通知
#define CMDNO_TTROOPS_WHEAT_LIST_NOTIFY 0x17025

//(控麦/放麦)操作
#define CMDNO_TROOPS_CONTROL_WHEAT_REQ  0x17026

//(控麦/放麦)操作回应
#define CMDNO_TROOPS_CONTROL_WHEAT_RESP 0x17027

//(禁麦/开麦)操作
#define CMDNO_TROOPS_DISABLE_WHEAT_REQ  0x17028

//(禁麦/开麦)操作回应
#define CMDNO_TROOPS_DISABLE_WHEAT_RESP 0x17029

//增加麦序时间
#define CMDNO_TROOPS_ADD_WHEAT_TIME_REQ 0x17030

//增加麦序时间回应
#define CMDNO_TROOPS_ADD_WHEAT_TIME_RESP    0x17031

//从麦序中移除
#define CMDNO_TROOPS_DEL_WHEAT_REQ      0x17032

//从麦序中移除回应
#define CMDNO_TROOPS_DEL_WHEAT_RESP     0x17033

//清空麦序列表
#define CMDNO_TROOPS_CLEAR_WHEAT_REQ    0x17034

//清空麦序列表
#define CMDNO_TROOPS_CLEAR_WHEAT_RESP   0x17035

//抱上麦序第一个
#define CMDNO_TROOPS_TOP_WHEAT_REQ      0x17036

//抱上麦序第一个回应
#define CMDNO_TROOPS_TOP_WHEAT_RESP     0x17037

//打开语音
#define CMDNO_TROOPS_OPEN_AUDIO_REQ     0x17038

//打开语音回应
#define CMDNO_TROOPS_OPEN_AUDIO_RESP    0x17039

//说话人列表通知
#define CMDNO_TROOPS_SPEAK_LIST_NOTIFY  0x17040

//关闭语音
#define CMDNO_TROOPS_CLOSE_AUDIO_REQ    0x17041

//踢人请求
#define CMDNO_TROOPS_KICK_REQ           0x17042

//踢人回应
#define CMDNO_TROOPS_KICK_RESP          0x17043

//踢人通知
#define CMDNO_TROOPS_KICK_NOTIFY        0x17044

//设置队伍相关参数
#define CMDNO_TROOPS_SET_PARAM_REQ      0x17045

//设置队伍相关参数回应
#define CMDNO_TROOPS_SET_PARAM_RESP     0x17046

//房间属性通知
#define CMDNO_TROOPS_PARAM_NOTIFY       0x17047

//改变房间模式(房间管理员才有这权限)
#define CMDNO_TROOPS_CHANGE_MODE_REQ    0x17048

//改变房间模式(房间管理员才有这权限)回应
#define CMDNO_TROOPS_CHANGE_MODE_RESP   0x17049

//模式改变通知
#define CMDNO_TROOPS_CHANGE_MODE_NOTIFY 0x17050

//网络状态通知
#define IM_NET_STATE_NOTIFY                   0x11016

/*====================== 好友系统 =======================*/
#import "YvXNearChatInfo.h"
#import "YvXSearchInfo.h"
#import "YvXUserInfo.h"
#import "YvXPushMsg.h"
#import "YvXGroupChatMsg.h"
#import "YvXP2PChatMsg.h"
#import "YvXHistoryMsgInfo.h"
#import "YvXGroupUser.h"


//登录返回
#import "YvIMLoginResp.h"
//第三方登录返回
#import "YvIMThirdLoginResp.h"
//获取第三方信息
#import "YvIMGetThirdBindInfoResp.h"

//同意接受好友请求返回
#import "YvIMFriendAcceptResp.h"
//添加好友返回
#import "YvIMFriendAddResp.h"
//删除好友返回
#import "YvIMFriendDelResp.h"
//设置好友信息返回
#import "YvIMFriendInfoSetResp.h"
//操作好友（黑名单）返回
#import "YvIMFriendOperResp.h"
//推荐好友返回
#import "YvIMFriendRecommandResp.h"
//搜索好友返回
#import "YvIMFriendSearchResp.h"
//好友请求通知
#import "YvIMFriendAddNotify.h"
//添加新好友后，服务器返回请求成功/失败
#import "YvIMFriendAddToSelfResp.h"
//黑名单列表推送
#import "YvIMFriendBlackListNotify.h"
//好友列表推送
#import "YvIMFriendListNotify.h"
//最近联系人列表推送
#import "YvIMFriendNearListNotify.h"
#import "YvXRecentUser.h"
//好友状态推送
#import "YvIMFriendStatusNotify.h"
//删除通知
#import "YvIMFriendDelNotify.h"
//获取好友列表
#import "YvIMFriendListResp.h"
//获取黑名单列表
#import "YvIMFriendBlackListResp.h"
//获取最近联系人推送列表
#import "YvIMFriendNearListResp.h"

/*====================== 聊天 =======================*/
//好友聊天通知
#import "YvIMChatFriendNotify.h"
//好友聊天发送回应
#import "YvIMChatFriendResp.h"
//群聊消息发送响应
#import "YvIMChatGroupResp.h"
//群聊天推送
#import "YvIMChatGroupNotify.h"
//发送进度（目前仅支持图片消息）
#import "YvIMChatMsgSendPercentNotify.h"


/*====================== 群 =======================*/
#import "YvIMGroupUserListNotify.h"
#import "YvIMGroupUserMDYNotify.h"
#import "YvIMGroupCreateResp.h"
#import "YvIMGroupSearchResp.h"
#import "YvIMGroupJoinNotify.h"
#import "YvIMGroupAcceptOrRefuseNotify.h"
#import "YvIMGroupJoinResp.h"
#import "YvIMGroupExitResp.h"
#import "YvIMGroupExitNotify.h"
#import "YvIMGroupModifyResp.h"
#import "YvIMGroupShiftToWnerResp.h"
#import "YvIMGroupShiftToWnerNotify.h"
#import "YvIMGroupKickNotify.h"
#import "YvIMGroupKickResp.h"
#import "YvIMGroupInviteNotify.h"
#import "YvIMGroupInviteResp.h"
#import "YvIMGroupInviteRespon.h"
#import "YvIMGroupInviteAccesptResp.h"
#import "YvIMGroupSetRoleResp.h"
#import "YvIMGroupSetRoleNotify.h"
#import "YvIMGroupDissolveResp.h"
#import "YvIMGroupSetOtherResp.h"
//修改他人名片通知
#import "YvIMGroupSetOtherNotify.h"
//群属性通知(群列表)
#import "YvIMGroupPropertyNotify.h"
//群成员上线
#import "YvIMGroupMemberOnline.h"
//新成员加入群通知
#import "YvIMGroupUserJoinNotify.h"

//Sdk版本号
#import "YvIMGetSdkInfoResp.h"
#import "YvNotifyNewsmodel.h"


/*====================== 频道 =======================*/
//获取频道列表信息
#import "YvIMChannelGetInfoResp.h"
//频道消息通知
#import "YvIMChannelMessageNotify.h"
//频道历史消息返回
#import "YvIMChannelGetHistoryMsgResp.h"
//发送频道消息返回 
#import "YvIMChannelSendMessageResp.h"
//修改通配符返回
#import "YvIMChannelModifyResp.h"
/*====================== 语音工具 =======================*/
//录音结束返回
#import "YvIMRecordStopResp.h"
//播放结束返回
#import "YvIMRecordFinishPlayResp.h"
//语音识别结束返回
#import "YvIMSpeechStopResp.h"
//下载语音URL进度返回
#import "YvIMRecordPlayPercentNotify.h"
//获取URL对应的文件路径
#import "YvIMGetCacheFileResp.h"
/*====================== 定位 =======================*/
//更新地理位置
#import "YvIMUploadLocationResp.h"
//获取地理位置
#import "YvIMGetLocationResp.h"
//获取附近的人
#import "YvIMSearchAroundResp.h"
#import "YvxLocation.h"
#import "YvxAroundUser.h"
/*====================== 云消息推送 =======================*/
//云消息通知
#import "YvIMCloudMSGNotify.h"
//云消息回应通知
#import "YvIMCloudMSGLimitNotify.h"

/*====================== 上传图片工具 =======================*/
#import "YvIMUploadFileResp.h"
#import "YvIMDownloadFileResp.h"

/*====================== 个人信息 =======================*/
//获取用户信息返回
#import "YvIMUserGetInfoResp.h"

///*====================== 队伍 =======================*/
//创建队伍响应
#import "YvIMTroopsCreateResp.h"
//登录队伍响应
#import "YvIMTroopsLoginResp.h"
//登出队伍响应
#import "YvIMTroopsLogoutResp.h"
//用户登录通知
#import "YvIMTroopsUserLoginNotify.h"
//用户登出通知
#import "YvIMTroopsUserLogoutNotify.h"
//用户列表通知
#import "YvIMTroopsUserListNotify.h"
//发送聊天消息响应
#import "YvIMTroopsSendChatMsgResp.h"
//聊天信息通知
#import "YvIMTroopsChatMsgNotify.h"
//队长转让回应
#import "YvIMTroopsAppointResp.h"
//队长转让通知
#import "YvIMTroopsAppointNotify.h"
//禁言回应
#import "YvIMTroopsGagResp.h"
//禁言通知
#import "YvIMTroopsGagNotify.h"
//抢麦回应
#import "YvIMTroopsRobWheatResp.h"
//下麦回应
#import "YvIMTroopsPutWheatResp.h"
//麦序列表通知
#import "YvIMTroopsWheatListNotify.h"
//(控麦/放麦)操作回应
#import "YvIMTroopsControlWheatResp.h"
//(禁麦/开麦)操作回应
#import "YvIMTroopsDisableWheatResp.h"
//增加麦序时间回应
#import "YvIMTroopsAddWheatTimeResp.h"
//从麦序中移除回应
#import "YvIMTroopsDelWheatResp.h"
//清空麦序列表回应
#import "YvIMTroopsClearWheatResp.h"
//抱上麦序第一个回应
#import "YvIMTroopsTopWheatResp.h"
//打开语音回应
#import "YvIMTroopsOpenAudioResp.h"
//说话人列表通知
#import "YvIMTroopsSpeakListNotify.h"
//踢人请求回应
#import "YvIMTroopsKickResp.h"
//踢人通知
#import "YvIMTroopsKickNotify.h"
//设置队伍相关参数回应
#import "YvIMTroopsSetParamResp.h"
//房间属性通知
#import "YvIMTroopsParamNotify.h"
//模式改变通知
#import "YvIMTroopsChangeModeNotify.h"
//改变房间模式回应
#import "YvIMTroopsChangeModeResp.h"

/******************* 网络状态通知 ***********************/
//网络状态通知
#import "YvIMNetStateNotify.h"
//录音大小返回
#import "YvIMRecordVolumeNotify.h"