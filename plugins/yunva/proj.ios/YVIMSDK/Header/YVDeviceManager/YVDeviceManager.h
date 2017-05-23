//
//  YVDeviceManager.h
//  YvIMSDKManage
//
//  Created by liwenjie on 14/12/26.
//  Copyright (c) 2014年 com.yunva.yaya. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "YVDeviceManagerMedia.h"
#import "YVDeviceManagerDelegate.h"

@protocol YVDeviceManager <YVDeviceManagerMedia

                            >

+(instancetype)shareInstance;
- (void)releaseInstance;
/*!
 @method
 @brief 注册一个监听对象到监听列表中
 @discussion 把监听对象添加到监听列表中准备接收相应的事件
 @param delegate 需要注册的监听对象
 @result
 */
- (void)addDelegate:(id<YVDeviceManagerDelegate>)delegate;

/*!
 @method
 @brief 从监听列表中移除一个监听对象
 @discussion 把监听对象从监听列表中移除,取消接收相应的事件
 @param delegate 需要移除的监听对象
 @result
 */
- (void)removeDelegate:(id<YVDeviceManagerDelegate>)delegate;
@end

