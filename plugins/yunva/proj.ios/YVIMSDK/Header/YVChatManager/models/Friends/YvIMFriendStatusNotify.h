//
//  YvIMFriendStatusNotify.h
//  YvIMSDKAPI
//  好友状态推送
//  Created by liwenjie on 15-3-9.
//  Copyright (c) 2015年 com.yunva.yaya. All rights reserved.
//

#import "YvBackBase.h"
//#define IM_FRIEND_STATUS_NOTIFY    0x12015
@interface YvIMFriendStatusNotify : YvBackBase

@property(assign, nonatomic) UInt32 userid;//用户ID
@property(assign, nonatomic) UInt8  status;//好友状态 e_status

+ (BOOL)CompareWithCmdId:(NSInteger)cmdid;

@end
