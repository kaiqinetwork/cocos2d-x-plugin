//
//  YvIMGroupExitResp.h
//  YvIMSDKAPITestDemo
//
//  Created by liwenjie on 15-3-17.
//  Copyright (c) 2015年 com.yunva.yaya. All rights reserved.
//

//退群响应
//#define IM_GROUP_EXIT_RESP   0x13011
//namespace x13011 {
//    enum {
//        /*uint32*/ result  = 1, //结果信息
//        /*string*/ msg     = 2, //错误信息
//        /*uint32*/ groupid = 3, //群名称
//        /*uint32*/ userid  = 4, //用户ID
//    };
//}

#import "YvBackBase.h"

@interface YvIMGroupExitResp : YvBackBase

@property(assign, nonatomic) UInt32     result;//结果信息
@property(strong, nonatomic) NSString * msg; //错误信息
@property(assign, nonatomic) UInt32     groupid;//群名称
@property(assign, nonatomic) UInt32     userid;//用户ID

@end
