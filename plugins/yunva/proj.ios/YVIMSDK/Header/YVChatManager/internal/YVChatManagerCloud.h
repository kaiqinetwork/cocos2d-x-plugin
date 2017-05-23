//
//  YVChatManagerCloud.h
//  YvImSDKOCWapper
//
//  Created by dada on 15/10/8.
//  Copyright (c) 2015年 yunva. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "YVChatManagerBase.h"
#import "YVChatManagerCloudDeleagte.h"
@protocol YVChatManagerCloud <YVChatManagerBase>

//请求云消息    私聊和群聊界面请求历史和现在 的消息
- (void)cloudMSGLimitWithSource:(NSString *)source c_id:(UInt32)c_id index:(UInt32)index limit:(UInt32)limit delegate:(id<YVChatManagerCloudDeleagte>)delegate;

//请求云消息确认已读   （针对好友消息）发出确认消息 例子：        [[YvIMCloudBL shareInstance] cloudMSGReadStatusWithCloudMsgId:cloudMsgNotify.endid cloudMsgSource:[NSString stringWithFormat:@"%d",xuser.userid] delegate:nil];//分别是：结束索引，好友id

- (void)cloudMSGReadStatusWithCloudMsgId:(UInt32)cloudMsgId cloudMsgSource:(NSString *)cloudMsgSource delegate:(id<YVChatManagerCloudDeleagte>)delegate;
//请求离线消息忽略（针对群消息）例子：         [[YvIMCloudBL shareInstance] cloudMsgIgnoreReqWithSource:CLOUDMSG_GROUP c_id:groupNotifyl.groupid index:cloudMsgNotify.endid delegate:nil];分别是：group字符串，群id，结束索引

- (void)cloudMsgIgnoreReqWithSource:(NSString *)source c_id:(UInt32)c_id index:(UInt32)index delegate:(id<YVChatManagerCloudDeleagte>)delegate;
@end
