//
//  YvIMFriendNearListNotify.h
//  YvIMSDKAPI
//  最近联系人列表推送
//  Created by liwenjie on 15-3-9.
//  Copyright (c) 2015年 com.yunva.yaya. All rights reserved.
//

//#define IM_FRIEND_NEARLIST_NOTIFY    0x12014

#import "YvBackBase.h"
#import "YvXRecentUser.h"
@interface YvIMFriendNearListNotify : YvBackBase

@property(strong, nonatomic) NSMutableArray *    xRecentUser; //最近联系人用户信息列表

@end
