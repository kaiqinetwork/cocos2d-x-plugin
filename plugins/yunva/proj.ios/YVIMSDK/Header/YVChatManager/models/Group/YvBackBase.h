//
//  YvBackBase.h
//  YvIMSDKAPITestDemo
//
//  Created by liwenjie on 15-3-17.
//  Copyright (c) 2015年 com.yunva.yaya. All rights reserved.
//

//#define IM_CLOUDMSG_LIMIT_NOTIFY   0x15005
//#define /*uint32*/	  CLOUDMSG_ID				110				//云消息ID
//#define /*string*/	  CLOUDMSG_SOURCE			111				//云标示符 {来源ID SYSTEM系统消息  PUSH 推送消息}

#import <Foundation/Foundation.h>


@interface YvBackBase : NSObject
//云消息确认
@property(assign, nonatomic) UInt32             CLOUDMSG_ID;//云消息ID
@property(strong, nonatomic) NSString       *   CLOUDMSG_SOURCE;//云标示符 {来源ID SYSTEM系统消息  PUSH 推送消息}

+ (BOOL)CompareWithCmdId:(NSInteger)cmdid;
@end
