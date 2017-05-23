//
//  YVChatManagerTroops.h
//  YvImSDKOCWapper
//
//  Created by dada on 15/10/9.
//  Copyright (c) 2015年 yunva. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "YVChatManagerBase.h"
@protocol YVChatManagerTroops <YVChatManagerBase>
#ifdef czw

#warning --暂时关闭，不适用，调式中
//获取队伍用户列表
- (NSMutableArray *)getTroopsUserList;
//获取队伍用户列表
- (YvIMTroopsLoginResp *)getTroopsInfo;

//创建队伍
- (void)troopsCreateReqWithTroopsName:(NSString *)troopsName gameId:(UInt32)gameId gameName:(NSString *)gameName;//
//登陆队伍
- (void)troopsLoginReqWithTroopsId:(UInt32)troopsId password:(NSString *)password expand:(NSString *)expand;//
//登出队伍
- (void)troopsLogoutReqWithTroopsId:(UInt32)troopsId;//
//发送文字
- (void)troopsSendTextReqWithText:(NSString *)text expand:(NSString *)expand;//
//发送语音
- (void)troopsSendAudioReqWithFileName:(NSString *)filename times:(UInt32)times expand:(NSString *)expand;//
//发送图片
- (void)troopsSendImageReqWithFileName:(NSString *)filename expand:(NSString *)expand;//
//队长转让
- (void)troopsAppointReqToUserId:(UInt32)touserid;//
//禁言
- (void)troopsGagReqWithUserId:(UInt32)touserid act:(UInt8)act;//
//抢麦操作
- (void)troopsRobWheatReq;//
//下麦操作
- (void)troopsPutWheatReq;//
//(控麦/放麦)操作
- (void)troopsControlWheatReq:(UInt8)control;//
//(禁麦/开麦)操作
- (void)troopsDisableWheatReq:(UInt8)disable;//
//增加麦序时间
- (void)troopsAddWheatTimeReq:(UInt32)userid;
//从麦序中移除
- (void)troopsDelWheatReq:(UInt32)userid;
//清空麦序列表
- (void)troopsClearWheatReq;
//抱上麦序第一个
- (void)troopsTopWheatReq:(UInt32)userid;
//打开语音
- (void)troopsOpenAudioReq;//
//关闭语音
- (void)troopsCloseAudioReq;//
//踢人
- (void)troopsKickReq:(UInt32)userid;//error
//设置队伍相关参数
- (void)troopsSetParamReqWithTeamName:(NSString *)teamName announcement:(NSString *)announcement sequenceTime:(UInt32)sequenceTime maxCount:(UInt32)maxCount passwd:(UInt8)passwd;
//改变房间模式(房间管理员才有这权限)
- (void)troopsChangeModeReq:(UInt8)mode;//
#endif
@end
