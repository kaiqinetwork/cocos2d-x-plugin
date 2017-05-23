//
//  YvXRecentUser.h
//  YvIMSDKAPITestDemo
//
//  Created by liwenjie on 15-3-16.
//  Copyright (c) 2015年 com.yunva.yaya. All rights reserved.
//

#import <Foundation/Foundation.h>

@class YvIMCloudMSGNotify;
@class YvXNearChatInfo;
@interface YvXRecentUser : NSObject
//
///*uint32*/        endId    = 1,
///*uint32*/        unread   = 2,
///*xP2PChatMsg*/   msg      = 3,
///*xNearChatInfo*/ user     = 4,
@property(assign, nonatomic) UInt32 endId;
@property(assign, nonatomic) UInt32 unread;
@property(strong, nonatomic) YvXP2PChatMsg      * xP2PChatMsg;
@property(strong, nonatomic) YvXNearChatInfo    * xNearChatInfo;
- (id)initWithCloudMsgNotify:(YvIMCloudMSGNotify *)cloudMsgNotify;
@end
