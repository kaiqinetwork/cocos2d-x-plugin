//
//  YvIMFriendSearchResp.h
//  YvIMSDKAPI
//  搜索好友回应
//  Created by liwenjie on 15-3-9.
//  Copyright (c) 2015年 com.yunva.yaya. All rights reserved.
//

//#define IM_FRIEND_SEARCH_RESP  0x12019

#import "YvBackBase.h"
#import "YvXSearchInfo.h"
@interface YvIMFriendSearchResp : YvBackBase

@property(assign, nonatomic) UInt32           result; //结果信息
@property(strong, nonatomic) NSString       * msg; //错误信息
@property(strong, nonatomic) NSMutableArray * xSerachinfo; //用户信息

@end
