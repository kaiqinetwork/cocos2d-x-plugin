//
//  YvIMGroupExitNotify.h
//  YvIMSDKAPITestDemo
//
//  Created by liwenjie on 15-3-17.
//  Copyright (c) 2015年 com.yunva.yaya. All rights reserved.
//

//退群通知
//#define IM_GROUP_EXIT_NOTIFY   0x13012
//namespace x13012 {
//    enum {
//        /*uint32*/ groupid = 1, //群名称
//        /*uint32*/ userid  = 2, //用户ID
//    };
//}
#import "YvBackBase.h"

@interface YvIMGroupExitNotify : YvBackBase

@property(assign, nonatomic) UInt32 groupid;//群名称
@property(assign, nonatomic) UInt32 userid;//用户ID

@end
