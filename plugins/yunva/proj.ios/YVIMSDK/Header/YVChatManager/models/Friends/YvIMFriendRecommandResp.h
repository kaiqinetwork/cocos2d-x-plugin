//
//  YvIMFriendRecommandResp.h
//  YvIMSDKAPI
//  推荐好友回应
//  Created by liwenjie on 15-3-9.
//  Copyright (c) 2015年 com.yunva.yaya. All rights reserved.
//

//#define IM_FRIEND_RECOMMAND_RESP   0x12009

#import "YvBackBase.h"
#import "YvXUserInfo.h"
@interface YvIMFriendRecommandResp : YvBackBase

@property(assign, nonatomic) UInt32             result;//结果信息,0成功，其它失败
@property(strong, nonatomic) NSString       *   msg;//错误信息
@property(strong, nonatomic) NSMutableArray *   userinfo;//用户信息

@end
