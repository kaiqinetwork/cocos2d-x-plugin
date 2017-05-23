//
//  YvIMCloudMSGLimitNotify.h
//  YvIMSDKAPITestDemo
//  云消息回应通知
//  Created by liwenjie on 15-3-11.
//  Copyright (c) 2015年 com.yunva.yaya. All rights reserved.
//

//#define IM_CLOUDMSG_LIMIT_NOTIFY   0x15005
//#define /*uint32*/	  CLOUDMSG_ID				110				//云消息ID
//#define /*string*/	  CLOUDMSG_SOURCE			111				//云标示符 {来源ID SYSTEM系统消息  PUSH 推送消息}


////云消息回应通知
//#define IM_CLOUDMSG_LIMIT_NOTIFY       0x15005
//namespace x15005 {
//    enum {
//        /*string*/		source	= 1, //来源
//        /*uint32*/      id      = 2, //若是好友消息, 则为好友ID
//        /*uint32*/		count	= 3, //消息数
//        /*uint32*/		indexId	= 4, //（返回的第一条）消息索引
//        /*uint32*/		ptime	= 5, //（返回的第一条）消息时间
//        /*xMsg[]*/		packet	= 6, //索引内容list xP2PChatMsg,  xGroupChatMsg
//    };
//}

#import "YvBackBase.h"
#import "YvXGroupChatMsg.h"
#import "YvXP2PChatMsg.h"

@interface YvIMCloudMSGLimitNotify : YvBackBase

@property(strong, nonatomic) NSString       *   source;//来源(SYSTEM 系统消息 PUSH 推送消息 userId 好友消息)
@property(assign, nonatomic) UInt32             c_id;//若是好友消息, 则为好友ID
@property(assign, nonatomic) UInt32             count;//消息数
@property(assign, nonatomic) UInt32             indexId;//当前消息索引
@property(assign, nonatomic) UInt32             ptime;//当前消息时间
@property(strong, nonatomic) NSMutableArray*    packet;//结束索引内容 xP2PChatMsg,  xGroupChatMsg

@end
