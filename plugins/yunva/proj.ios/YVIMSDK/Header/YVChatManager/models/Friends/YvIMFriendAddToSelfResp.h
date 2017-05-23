//
//  YvIMFriendAddToSelfResp.h
//  YvIMSDKDemo
//
//  Created by dada on 15/8/15.
//  Copyright (c) 2015年 yunva. All rights reserved.
//


////请求添加好友消息发送回应
//#define IM_FRIEND_ADD_RESPOND    0x12025
//namespace x12025 {
//    enum  {
//        /*uint32*/	result   = 1,
//        /*string*/	msg	= 2,
//        /*uint32*/     userid   = 3,
//    };
//}
#import "YvBackBase.h"

@interface YvIMFriendAddToSelfResp : YvBackBase
@property(assign, nonatomic) UInt32     result;
@property(strong, nonatomic) NSString   * msg;
@property(assign, nonatomic) UInt32     userid;


@end
