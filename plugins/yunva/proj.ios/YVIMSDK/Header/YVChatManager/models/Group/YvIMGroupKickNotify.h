//
//  YvIMGroupKickNotify.h
//  YvIMSDKAPITestDemo
//
//  Created by liwenjie on 15-3-17.
//  Copyright (c) 2015年 com.yunva.yaya. All rights reserved.
//

//踢除群成员通知
//#define IM_KGROUP_KICK_NOTIFY   0x13019
//namespace x13019 {
//    enum {
//        /*uint32*/ groupid   = 1, //群ID
//        /*uint32*/ userid    = 2, //用户ID
//        /*uint32*/ kickid    = 3, //被踢成员ID
//        /*string*/ groupname = 4, //群名称
//    };
//}


#import "YvBackBase.h"

@interface YvIMGroupKickNotify : YvBackBase

@property(assign, nonatomic) UInt32 groupid;//群ID
@property(assign, nonatomic) UInt32 userid;//用户ID
@property(assign, nonatomic) UInt32 kickid;//被踢成员ID
@property(strong, nonatomic) NSString * groupname;//群名称

@end
