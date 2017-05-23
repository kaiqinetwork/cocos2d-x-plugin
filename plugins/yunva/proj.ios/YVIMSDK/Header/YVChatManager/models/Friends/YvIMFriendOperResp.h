//
//  YvIMFriendOperResp.h
//  YvIMSDKAPI
//  好友操作回应(黑名单)
//  Created by liwenjie on 15-3-9.
//  Copyright (c) 2015年 com.yunva.yaya. All rights reserved.
//

////操作好友黑名单
//enum e_oper_friend_act
//{
//    oper_add_blacklist = 3, //加入黑名单
//    oper_del_blacklist = 4, //删除黑名单
//};

///*uint32*/	result		= 5,
///*string*/	msg			= 6,

//#define IM_FRIEND_OPER_RESP		0x12011

#import "YvBackBase.h"
@interface YvIMFriendOperResp : YvBackBase

@property(assign, nonatomic) UInt32     userId;//用户ID
@property(assign, nonatomic) UInt32     operId;//操作ID
@property(assign, nonatomic) UInt8      act;//动作   oper_friend_act
@property(assign, nonatomic) UInt8      oper_state;//对方状态

@property(assign, nonatomic) UInt32     result;
@property(strong, nonatomic) NSString    *msg;
@end
