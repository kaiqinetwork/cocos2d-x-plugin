//
//  YVChatManagerChatDelegate.h
//  YvImSDKOCWapper
//
//  Created by dada on 15/10/8.
//  Copyright (c) 2015年 yunva. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "YVChatManageDelegateBase.h"

@protocol YVChatManagerChatDelegate <YVChatManageDelegateBase>
@optional
//好友聊天消息通知
- (void)onIMChatFriendNotify:(YvIMChatFriendNotify *)chatFriendNotify;
//好友聊天消息发送回应
- (void)onIMChatFriendResp:(YvIMChatFriendResp *)chatFriendResp;
//群聊天消息通知
- (void)onIMChatGroupNotify:(YvIMChatGroupNotify *)chatGroupNotify;
//群聊天消息发送回应
- (void)onIMChatGroupResp:(YvIMChatGroupResp *)chatGroupResp;
//发送进度（目前仅支持图片消息）
-(void)onIMChatMsgSendPercentNotify:(YvIMChatMsgSendPercentNotify*)ChatMsgSendPercentNotify;
@end
