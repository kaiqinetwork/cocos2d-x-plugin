//
//  YvChatManagerLogin.h
//  YvIMSDKManage
//
//  Created by liwenjie on 14/12/24.
//  Copyright (c) 2014年 com.yunva.yaya. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "YVChatManagerBase.h"
/*!
 @protocol
 @brief 本协议包括：登录、退出、等方法
 @discussion 需要先将要接受回调的对象注册到delegate中, 示例代码如下:
 [[[YvIMSDKManage sharedInstance] chatManager] addDelegate:self ]
 */
@protocol YVChatManagerLogin <YVChatManagerBase>

@required



//帐号登录
- (void)loginWithUserId:(UInt32)userid password:(NSString *)passowrd pgameServiceID:(NSString *)pgameServiceID wildCard:(NSArray *)wildCard readstatus:(UInt8)readstatus;

//第三方登录
- (void)thirdLoginWithTT:(NSString *)tt pgameServiceID:(NSString *)pgameServiceID wildCard:(NSArray *)wildCard readstatus:(UInt8)readstatus;

//设置设备信息
- (void)setDeviceInfoWithUUID:(NSString *)uuid appVersion:(NSString *)appVersion;

//获取第三方信息
- (void)getThirdBindInfo:(UInt32)appid uid:(NSString*)uid;

//注销
- (void)logout;

//释放资源
- (void)destory;

-(void)getSdkVertion;




@end

