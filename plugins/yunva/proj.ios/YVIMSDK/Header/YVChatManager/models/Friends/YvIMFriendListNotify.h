//
//  YvIMFriendListNotify.h
//  YvIMSDKAPI
//  好友列表推送
//  Created by liwenjie on 15-3-9.
//  Copyright (c) 2015年 com.yunva.yaya. All rights reserved.
//

//#define IM_FRIEND_LIST_NOTIFY		0x12012

#import "YvBackBase.h"
#import "YvXUserInfo.h"
@interface YvIMFriendListNotify : YvBackBase

@property(strong, nonatomic) NSMutableArray *    xUserInfo; //用户信息列表

@end
