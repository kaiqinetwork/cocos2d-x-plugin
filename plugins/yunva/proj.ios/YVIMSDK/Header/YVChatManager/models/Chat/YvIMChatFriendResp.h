//
//  YvIMChatFriendResp.h
//  YvIMSDKAPITestDemo
//
//  Created by liwenjie on 15-3-10.
//  Copyright (c) 2015年 com.yunva.yaya. All rights reserved.
//

//#define IM_CHATT_FRIEND_RESP  0x14004

#import "YvBackBase.h"

@interface YvIMChatFriendResp : YvBackBase

@property(assign, nonatomic) UInt32       result;
@property(strong, nonatomic) NSString *   msg;
@property(assign, nonatomic) UInt32       userid;//好友ID
@property(strong, nonatomic) NSString *   flag;//消息标记

@end
