//
//  YvIMFriendBlackListResp.h
//  YvIMSDKAPITestDemo
//
//  Created by whe on 15/6/25.
//  Copyright (c) 2015年 com.yunva.yaya. All rights reserved.
//


////黑名单列表查询回应
//#define IM_FRIEND_BLACKLIST_RESP		 0x12031
//namespace x12031{
//    enum {
//        /*xUserInfo[]*/ userinfo = 1, //用户信息
//    };
//}


#import "YvBackBase.h"
@interface YvIMFriendBlackListResp : YvBackBase
@property(strong,nonatomic) NSMutableArray *friendBlackList;
@end
