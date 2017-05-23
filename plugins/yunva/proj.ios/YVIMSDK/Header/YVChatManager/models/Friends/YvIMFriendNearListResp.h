//
//  YvFriendNearListResp.h
//  YvIMSDKAPITestDemo
//
//  Created by whe on 15/7/2.
//  Copyright (c) 2015年 com.yunva.yaya. All rights reserved.
//


// //最近联系人列表查询回应
// #define IM_FRIEND_NEARLIST_RESP		0x12033
// namespace x12033{
//	enum {
// /*xRecentConactList[]*/ recent = 1, //用户信息
//};
//}

#import "YvBackBase.h"
@interface YvIMFriendNearListResp : YvBackBase
@property(strong,nonatomic) NSMutableArray *xRecentConactList;  //用户信息
@end
