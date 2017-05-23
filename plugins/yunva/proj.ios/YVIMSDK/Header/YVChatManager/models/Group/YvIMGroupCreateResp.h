//
//  YvIMGroupCreateResp.h
//  YvIMSDKAPITestDemo
//
//  Created by liwenjie on 15-3-17.
//  Copyright (c) 2015年 com.yunva.yaya. All rights reserved.
//
///*创建群回应*/
//#define IM_GROUP_CREATE_RESP      0x13003
//namespace x13003 {
//    enum {
//        /*uint32*/ result  = 1, //创建结果
//        /*string*/ msg     = 2, //错误信息
//        /*uint32*/ groupid = 3, //群ID
//    };
//}
#import "YvBackBase.h"

@interface YvIMGroupCreateResp : YvBackBase

@property(assign, nonatomic) UInt32     result;//创建结果
@property(strong, nonatomic) NSString * msg;//错误信息
@property(assign, nonatomic) UInt32     groupid;//群ID

@end
