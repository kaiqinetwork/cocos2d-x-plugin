//
//  YvIMGroupSetOtherResp.h
//  YvIMSDKAPITestDemo
//
//  Created by liwenjie on 15-3-17.
//  Copyright (c) 2015年 com.yunva.yaya. All rights reserved.
//

//修改他人名片返回
//#define IM_GROUP_SETOTHER_RESP   0x13032
//namespace x13032 {
//    enum {
//        /*uint32*/ result  = 1, //结果信息
//        /*string*/ msg     = 2, //错误信息
//        /*uint32*/ groupid = 3, //群ID
//    };
//}

#import "YvBackBase.h"

@interface YvIMGroupSetOtherResp : YvBackBase

@property(assign, nonatomic) UInt32     result;//结果信息
@property(strong, nonatomic) NSString * msg;//错误信息
@property(assign, nonatomic) UInt32     groupid;//群ID

@end
