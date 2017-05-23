//
//  YvIMFriendBlackListNotify.h
//  YvIMSDKAPI
//  黑名单列表推送
//  Created by liwenjie on 15-3-9.
//  Copyright (c) 2015年 com.yunva.yaya. All rights reserved.
//

//#define IM_FRIEND_BLACKLIST_NOTIFY       0x12013

#import "YvBackBase.h"
#import "YvXUserInfo.h"
@interface YvIMFriendBlackListNotify : YvBackBase

@property(strong, nonatomic) NSMutableArray *    xUserInfo; //用户信息列表

@end
