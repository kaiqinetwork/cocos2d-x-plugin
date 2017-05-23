//
//  YvIMFriendDelNotify.h
//  YvIMSDKAPITestDemo
//
//  Created by liwenjie on 15-3-14.
//  Copyright (c) 2015年 com.yunva.yaya. All rights reserved.
//

////删除好友通知
//#define IM_FRIEND_DEL_NOTIFY   0x12007
//namespace x12006 {
//    enum {
//        /*uint32*/ del_friend	 = 1, //删除好友id
//        /*uint8*/  del_fromlist  = 2, //从自己的好友列表中删除
//    };
//}

#import "YvBackBase.h"
@interface YvIMFriendDelNotify : YvBackBase

@property(assign, nonatomic) UInt32     del_friend;//删除好友id
@property(assign, nonatomic) UInt8      del_fromlist;//从自己的好友列表中删除

@end
