//
//  YvIMCloudMSGNotify.h
//  YvIMSDKAPITestDemo
//  云消息通知
//  Created by liwenjie on 15-3-11.
//  Copyright (c) 2015年 com.yunva.yaya. All rights reserved.
//

//#define IM_CLOUDMSG_NOTIFY    0x15002

#import "YvBackBase.h"
#import "YvXGroupChatMsg.h"
#import "YvXP2PChatMsg.h"

@interface YvIMCloudMSGNotify : YvBackBase

@property(strong, nonatomic) NSString       *   source;//来源(SYSTEM 系统消息 PUSH 推送消息 userId 好友消息)
@property(assign, nonatomic) UInt32             c_id;//若是好友消息, 则为好友ID
@property(assign, nonatomic) UInt32             beginid;//开始索引
@property(assign, nonatomic) UInt32             endid;//结束索引
@property(assign, nonatomic) UInt32             time;//结束索引时间
@property(strong, nonatomic) id                 packet;//结束索引内容 xP2PChatMsg,  xGroupChatMsg
@property(assign, nonatomic) UInt32             unread;//未读条数

@property(assign, nonatomic) UInt32             NUreadnum;//未读条数  mark by czw
@end
