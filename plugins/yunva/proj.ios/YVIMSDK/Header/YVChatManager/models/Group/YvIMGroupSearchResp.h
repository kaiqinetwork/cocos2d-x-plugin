//
//  YvIMGroupSearchResp.h
//  YvIMSDKAPITestDemo
//
//  Created by liwenjie on 15-3-17.
//  Copyright (c) 2015年 com.yunva.yaya. All rights reserved.
//


///*搜索群回应*/
//#define IM_GROUP_SEARCH_RESP      0x13005
//namespace x13005 {
//    enum {
//        /*uint32*/ result       = 1, //结果信息
//        /*string*/ msg          = 2, //错误信息
//        /*uint32*/ groupid      = 3, //群ID
//        /*uint32*/ verify       = 4, //群验证方式
//        /*string*/ name         = 5, //群名称
//        /*string*/ iconurl      = 6, //图标
//        /*uint32*/ numbercount  = 7, //总共人数
//        /*uint32*/ currentnum   = 8, //加入人数
//        /*uint32*/ ownerid      = 9, //拥有者ID
//        /*string*/ announcement = 10,//群宣言
//        
//    };
//}

#import "YvBackBase.h"

@interface YvIMGroupSearchResp : YvBackBase

@property(assign, nonatomic) UInt32     result;//结果信息
@property(strong, nonatomic) NSString * msg;//错误信息
@property(assign, nonatomic) UInt32     groupid;//群ID
@property(assign, nonatomic) UInt32     verify;//群验证方式
@property(strong, nonatomic) NSString * name;//群名称
@property(strong, nonatomic) NSString * iconurl;//图标
@property(assign, nonatomic) UInt32     numbercount;//总共人数
@property(assign, nonatomic) UInt32     currentnum;//加入人数
@property(assign, nonatomic) UInt32     ownerid;//拥有者ID
@property(strong, nonatomic) NSString * announcement;//群宣言

@end
