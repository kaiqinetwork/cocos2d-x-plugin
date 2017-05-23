//
//  YvIMGroupDissolveNotify.h
//  YvIMSDKAPITestDemo
//
//  Created by liwenjie on 15-3-17.
//  Copyright (c) 2015年 com.yunva.yaya. All rights reserved.
//

//修改他人名片通知
//#define IM_GROUP_SETOTHER_NOTIFY   0x13031
//namespace x13031 {
//    enum {
//        /*uint32*/ groupid = 1, //群ID
//        /*uint32*/ userid  = 2, //用户ID
//        /*string*/ alias   = 3, //用户名片
//    };
//}


#import "YvBackBase.h"

@interface YvIMGroupSetOtherNotify : YvBackBase

@property(assign, nonatomic) UInt32     groupid;//群ID
@property(assign, nonatomic) UInt32     userid;//用户ID
@property(strong, nonatomic) NSString * alias;//用户名片

@end
