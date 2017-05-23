//
//  YVChatManagerChat.h
//  YvImSDKOCWapper
//
//  Created by dada on 15/10/8.
//  Copyright (c) 2015年 yunva. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "YVChatManagerBase.h"
@protocol YVChatManagerChat <YVChatManagerBase>
@required

//好友私聊发送消息
- (void)chatFriendTextMsg:(NSString *)data toUserId:(UInt32)userid ext:(NSString *)ext flag:(NSString *)flag;

//好友私聊发送图片消息
- (void)chatFriendImageMsg:(NSString *)image toUserId:(UInt32)userid ext:(NSString *)ext flag:(NSString *)flag;

//好友私聊发送语音消息
- (void)chatFriendAudioMsg:(NSString *)file time:(UInt32)time toUserId:(UInt32)userid text:(NSString *)text ext:(NSString *)ext flag:(NSString *)flag;

//群发送消息
- (void)chatGroupTextMsg:(NSString *)data groupid:(UInt32)groupid ext:(NSString *)ext AndFlag:(NSString*)flag;

//群发送图片消息
- (void)chatGroupImageMsg:(NSString *)image groupid:(UInt32)groupid ext:(NSString *)ext AndFlag:(NSString*)flag;

//群发送语音消息
- (void)chatGroupAudioMsg:(NSString *)file time:(UInt32)time groupid:(UInt32)groupid text:(NSString *)text ext:(NSString *)ext AndFlag:(NSString*)flag;

//取消发送（目前仅支持图片消息）
-(void)chatCancelSendreqWithFriendOrGroupid:(UInt32 )FriendOrgroupid AndMsgFlag:(NSString *)flag;
@end
