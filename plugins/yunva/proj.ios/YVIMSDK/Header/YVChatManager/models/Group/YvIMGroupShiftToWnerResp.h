//
//  YvIMGroupShiftToWnerResp.h
//  YvIMSDKAPITestDemo
//
//  Created by liwenjie on 15-3-17.
//  Copyright (c) 2015年 com.yunva.yaya. All rights reserved.
//


//转移群主响应
//#define IM_GROUP_SHIFTOWNER_RESP   0x13017
//namespace x13017 {
//    enum {
//        /*uint32*/ result  = 1, //结果信息
//        /*string*/ msg     = 2, //错误信息
//        /*uint32*/ groupid = 3, //群ID
//        /*uint32*/ userid  = 4, //用户ID
//        /*uint32*/ shiftid = 5,  //转移对象
//    };
//}


#import "YvBackBase.h"

@interface YvIMGroupShiftToWnerResp : YvBackBase

@property(assign, nonatomic) UInt32 result;//结果信息
@property(strong, nonatomic) NSString * msg;//错误信息
@property(assign, nonatomic) UInt32 groupid;//群ID
@property(assign, nonatomic) UInt32 userid;//用户ID
@property(assign, nonatomic) UInt32 shiftid;//转移对象

@end
