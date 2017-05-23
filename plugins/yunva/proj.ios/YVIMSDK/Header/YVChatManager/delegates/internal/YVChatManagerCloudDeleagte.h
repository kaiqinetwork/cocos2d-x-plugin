//
//  YVChatManagerCloudDeleagte.h
//  YvImSDKOCWapper
//
//  Created by dada on 15/10/8.
//  Copyright (c) 2015年 yunva. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "YVChatManageDelegateBase.h"
@protocol YVChatManagerCloudDeleagte <YVChatManageDelegateBase>
//云消息通知   会话列表信息通知
- (void)onIMCloudMSGNotify:(YvIMCloudMSGNotify *)cloudMSGNotify;
//云消息回应通知   私聊和群聊界面消息云通知
- (void)onIMCloudMSGLimitNotify:(YvIMCloudMSGLimitNotify *)cloudMSGLimitNotify;
@end
