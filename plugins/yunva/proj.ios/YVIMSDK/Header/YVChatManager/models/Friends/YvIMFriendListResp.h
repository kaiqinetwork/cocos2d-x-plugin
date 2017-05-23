//
//  YvIMFriendListResp.h
//  YvIMSDKAPITestDemo
//
//  Created by whe on 15/6/25.
//  Copyright (c) 2015年 com.yunva.yaya. All rights reserved.
//

////好友列表查询回应
//#define IM_FRIEND_LIST_RESP		     0x12029
//namespace x12029{
//    enum {
//        /*xUserInfo[]*/ userinfo = 1, //用户信息
//    };
//}


#import "YvBackBase.h"
@interface YvIMFriendListResp : YvBackBase
@property(strong,nonatomic) NSMutableArray *friendList;
@end
