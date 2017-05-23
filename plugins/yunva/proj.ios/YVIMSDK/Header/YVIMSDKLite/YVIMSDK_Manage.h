//
//  YVIMSDK_Manage.h
//  YVIMSDK_Manage
//
//  Created by dada on 15/9/24.
//  Copyright (c) 2015年 yunva. All rights reserved.
//


#import <Foundation/Foundation.h>
#import "YVChatManager.h"
#import "YVDeviceManager.h"
#import "YvIMCMDDefHeaders.h"

@interface YVIMSDK_Manage : NSObject

#pragma mark- 创建单例
+(instancetype)shareInstance;

#pragma mark - 注册

/*!
 @method
 @brief 初始化SDK
 
 @param appKey
 @param isTest --- 是否是测试环境 YES:测试环境  NO:正式环境
 */
-(void)SDKInitWithAppId:(NSString *)appId isTest:(BOOL)isTest;


/*!
 @property
 @brief 设置日志级别:0--关闭日志  1--error  2--debug（不设置为默认该级别）  3--warn  4--info  5--trace
 */
//-(void)setLogLevel:(int)logLevel;


/*!
 @property
 @brief 聊天管理器, 获取该对象后, 可以做登录、聊天、加好友等操作
 */
@property (nonatomic, readonly, strong) id<YVChatManager> chatManager;



/*!
 @property
 @brief 设备管理器, 获取该对象后, 可以操作相关的接口(语音录制, 语音识别等)
 */
@property (nonatomic, readonly, strong) id<YVDeviceManager> deviceManager;

@end
