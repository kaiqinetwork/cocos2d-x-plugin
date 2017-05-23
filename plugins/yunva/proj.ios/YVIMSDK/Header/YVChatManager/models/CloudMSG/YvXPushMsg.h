//
//  YvXPushMsg.h
//  YvIMSDKAPITestDemo
//
//  Created by liwenjie on 15-3-11.
//  Copyright (c) 2015年 com.yunva.yaya. All rights reserved.
//

#import <Foundation/Foundation.h>
//#define IM_MSG_PUSH   0x15006

@interface YvXPushMsg : NSObject

@property(assign, nonatomic) UInt32     appid;
@property(strong, nonatomic) NSString * data;//json

@end
