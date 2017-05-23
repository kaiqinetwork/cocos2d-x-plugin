//
//  YvIMGroupUserListNotify.h
//  YvIMSDKAPITestDemo
//
//  Created by liwenjie on 15-3-17.
//  Copyright (c) 2015年 com.yunva.yaya. All rights reserved.
//

////群用户列表
//#define IM_GROUP_USERLIST_NOTIFY  0x13000
//namespace x13000 {
//    enum {
//        /*uint32*/     groupid     = 1, //群ID
//        /*object[]*/   xGroupUser  = 2,	//用户列表
//    };
//}

#import "YvBackBase.h"
#import "YvXGroupUser.h"

@interface YvIMGroupUserListNotify : YvBackBase

@property(assign, nonatomic) UInt32             groupid;//群ID
@property(strong, nonatomic) NSMutableArray *   xGroupUser;//用户列表

@end
