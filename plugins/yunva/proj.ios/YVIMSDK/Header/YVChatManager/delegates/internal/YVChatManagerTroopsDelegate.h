//
//  YVChatManagerTroopsDelegate.h
//  YvImSDKOCWapper
//
//  Created by dada on 15/10/9.
//  Copyright (c) 2015年 yunva. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "YVChatManageDelegateBase.h"
@protocol YVChatManagerTroopsDelegate <YVChatManageDelegateBase>
#ifdef czw
//创建队伍返回
- (void)onIMTroopsCreateResp:(YvIMTroopsCreateResp *)troopsCreateResp;//
//登录队伍返回
- (void)onIMTroopsLoginResp:(YvIMTroopsLoginResp *)troopsLoginResp;//
//登出队伍返回
- (void)onIMTroopsLogoutResp:(YvIMTroopsLogoutResp *)troopsLogoutResp;//
//用户登录通知
- (void)onIMTroopsUserLoginNotify:(YvIMTroopsUserLoginNotify *)userLoginNotify;//
//用户登出通知
- (void)onIMTroopsUserLogoutNotify:(YvIMTroopsUserLogoutNotify *)userLogoutNotify;//
//用户列表通知
- (void)onIMTroopsUserListtNotify:(YvIMTroopsUserListNotify *)userListNotify;//
//发送聊天消息返回
- (void)onIMTroopsSendChatMsgResp:(YvIMTroopsSendChatMsgResp *)troopsSendChatMsgResp;//
//聊天信息通知
- (void)onIMTroopsChatMsgNotify:(YvIMTroopsChatMsgNotify *)troopsChatMsgNotify;//
//队长转让回应
- (void)onIMTroopsAppointResp:(YvIMTroopsAppointResp *)troopsAppointResp;//
//队长转让通知
- (void)onIMTroopsAppointNotify:(YvIMTroopsAppointNotify *)troopsAppointNotify;//
//禁言回应
- (void)onIMTroopsGagResp:(YvIMTroopsGagResp *)troopsGagResp;//
//禁言通知
- (void)onIMTroopsGagNotify:(YvIMTroopsGagNotify *)troopsGagNotify;//
//抢麦回应
- (void)onIMTroopsRobWheatResp:(YvIMTroopsRobWheatResp *)troopsRobWheatResp;//
//下麦回应
- (void)onIMTroopsPutWheatResp:(YvIMTroopsPutWheatResp *)troopsPutWheatResp;//
//(控麦/放麦)操作回应
- (void)onIMTroopsControlWheatResp:(YvIMTroopsControlWheatResp *)troopsControlWheatResp;//
//麦序列表通知
- (void)onIMTroopsWheatListNotify:(YvIMTroopsWheatListNotify *)troopsWheatListNotify;//
//(禁麦/开麦)操作回应
- (void)onIMTroopsDisableWheatResp:(YvIMTroopsDisableWheatResp *)troopsDisableWheatResp;//
//增加麦序时间回应
- (void)onIMTroopsAddWheatTimeResp:(YvIMTroopsAddWheatTimeResp *)troopsAddWheatTimeResp;
//从麦序中移除回应
- (void)onIMTroopsDelWheatResp:(YvIMTroopsDelWheatResp *)troopsDelWheatResp;
//清空麦序列表回应
- (void)onIMTroopsClearWheatResp:(YvIMTroopsClearWheatResp *)troopsClearWheatResp;//
//抱上麦序第一个回应
- (void)onIMTroopsTopWheatResp:(YvIMTroopsTopWheatResp *)troopsTopWheatResp;//
//打开语音回应
- (void)onIMTroopsOpenAudioResp:(YvIMTroopsOpenAudioResp *)troopsOpenAudioResp;//
//说话人列表通知
- (void)onIMTroopsSpeakListNotify:(YvIMTroopsSpeakListNotify *)troopsSpeakListNotify;//
//踢人请求回应
- (void)onIMTroopsKickResp:(YvIMTroopsKickResp *)troopsKickResp;//
//踢人通知
- (void)onIMTroopsKickNotify:(YvIMTroopsKickNotify *)troopsKickNotify;//
//设置队伍相关参数回应
- (void)onIMTroopsSetParamResp:(YvIMTroopsSetParamResp *)troopsSetParamResp;
//房间队伍相关参数通知
- (void)onIMTroopsParamNotify:(YvIMTroopsParamNotify *)troopsParamNotify;
//房间模式改变通知
- (void)onIMTroopsChangeModeNotify:(YvIMTroopsChangeModeNotify *)troopsChangeModeNotify;//
//改变房间模式回应
- (void)onIMTroopsChangeModeResp:(YvIMTroopsChangeModeResp *)troopsChangeModeResp;//
#endif
@end
