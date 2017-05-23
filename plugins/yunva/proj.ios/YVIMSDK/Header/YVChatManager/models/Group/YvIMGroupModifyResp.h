//
//  YvIMGroupModifyResp.h
//  YvIMSDKAPITestDemo
//
//  Created by liwenjie on 15-3-17.
//  Copyright (c) 2015年 com.yunva.yaya. All rights reserved.
//

//修改群属性响应
//#define IM_GROUP_MODIFY_RESP  0x13014
//namespace x13014 {
//    enum {
//        /*uint32*/ result       = 1, //结果信息
//        /*string*/ msg          = 2, //错误信息
//        /*uint32*/ groupid      = 3, //群ID
//        /*string*/ name         = 4, //群名称
//        /*string*/ icon         = 5, //群图标
//        /*string*/ announcement = 6, //群公告
//        /*uint8*/  verify       = 7, //验证方式
//        /*uint8*/  msg_set      = 8, //群消息设置
//        /*string*/ alias        = 10,//名片修改
//    };
//}

#import "YvBackBase.h"

@interface YvIMGroupModifyResp : YvBackBase

@property(assign, nonatomic) UInt32 result;//结果信息
@property(strong, nonatomic) NSString * msg;//错误信息
@property(assign, nonatomic) UInt32 groupid;//群ID
@property(strong, nonatomic) NSString * name;//群名称
@property(strong, nonatomic) NSString * icon;//群图标
@property(strong, nonatomic) NSString * announcement;//群公告
@property(assign, nonatomic) UInt8 verify;//验证方式
@property(assign, nonatomic) UInt8 msg_set;//群消息设置
@property(strong, nonatomic) NSString * alias;//名片修改

@end
