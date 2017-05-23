//
//  YvIMGroupSetRoleResp.h
//  YvIMSDKAPITestDemo
//
//  Created by liwenjie on 15-3-17.
//  Copyright (c) 2015年 com.yunva.yaya. All rights reserved.
//

//设置群成员角色返回
//#define IM_GROUP_SETROLE_RESP    0x13026
//namespace x13026 {
//    enum {
//        /*uint32*/ result  = 1, //结果信息
//        /*string*/ msg     = 2, //错误信息
//        /*uint32*/ groupid = 3, //群ID
//    };
//}

#import "YvBackBase.h"

@interface YvIMGroupSetRoleResp : YvBackBase

@property(assign, nonatomic) UInt32     result;//结果信息
@property(strong, nonatomic) NSString * msg;//错误信息
@property(assign, nonatomic) UInt32     groupid;//群ID

@end
