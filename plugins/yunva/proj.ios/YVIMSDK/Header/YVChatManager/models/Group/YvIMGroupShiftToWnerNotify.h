//
//  YvIMGroupShiftToWnerNotify.h
//  YvIMSDKAPITestDemo
//
//  Created by liwenjie on 15-3-17.
//  Copyright (c) 2015年 com.yunva.yaya. All rights reserved.
//

//转移群主通知
//#define IM_GROUP_SHIFTOWNER_NOTIFY   0x13016
//namespace x13016 {
//    enum {
//        
//        /*uint32*/ groupid = 1, //群ID
//        /*uint32*/ userid  = 2, //用户ID
//        /*uint32*/ shiftid = 3, //转移对象
//    };
//}

#import "YvBackBase.h"

@interface YvIMGroupShiftToWnerNotify : YvBackBase

@property(assign, nonatomic) UInt32 groupid;//群ID
@property(assign, nonatomic) UInt32 userid;//用户ID
@property(assign, nonatomic) UInt32 shiftid;//转移对象

@end
