//
//  YvIMGroupDissolveResp.h
//  YvIMSDKAPITestDemo
//
//  Created by liwenjie on 15-3-17.
//  Copyright (c) 2015年 com.yunva.yaya. All rights reserved.
//

//解散群响应
//#define IM_GROUP_DISSOLVE_RESP  0x13029
//namespace x13029 {
//    enum {
//        /*uint32*/ result = 1, //结果信息
//        /*string*/ msg    = 2, //错误信息
//        /*uint32*/ grouid = 3, //群ID
//    };
//}

#import "YvBackBase.h"

@interface YvIMGroupDissolveResp : YvBackBase

@property(assign, nonatomic) UInt32     result;//结果信息
@property(strong, nonatomic) NSString * msg;//错误信息
@property(assign, nonatomic) UInt32     groupid;//群ID

@end
