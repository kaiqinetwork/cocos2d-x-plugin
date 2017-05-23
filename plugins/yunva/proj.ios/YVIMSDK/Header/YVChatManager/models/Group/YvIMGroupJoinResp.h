//
//  YvIMGroupJoinResp.h
//  YvIMSDKAPITestDemo
//
//  Created by liwenjie on 15-3-17.
//  Copyright (c) 2015年 com.yunva.yaya. All rights reserved.
//

/*申请加群返回结果通知*/
//#define IM_GROUP_JOIN_RESP      0x13009
//namespace x13009 {
//    enum {
//        /*uint32*/ result    = 1, //创建结果
//        /*string*/ msg       = 2, //错误信息
//        /*uint32*/ groupid   = 3, //群ID
//        /*uint32*/ userid    = 4, //用户ID
//        /*uint8*/  agree     = 5, //是否同意加入群 e_joingroup
//        /*string*/ groupname = 6, //群名称
//        /*string*/ greet     = 7, //问候语
//        /*string*/ iconurl   = 8, //用户头像地址
//    };
//}

#import "YvBackBase.h"

@interface YvIMGroupJoinResp : YvBackBase

@property(assign, nonatomic) UInt32     result;//创建结果
@property(strong, nonatomic) NSString * msg;//错误信息
@property(assign, nonatomic) UInt32     groupid;//群ID
@property(assign, nonatomic) UInt32     userid;//用户ID
@property(assign, nonatomic) UInt8      agree;//是否同意加入群 e_joingroup
@property(strong, nonatomic) NSString * groupname;//群名称
@property(strong, nonatomic) NSString * greet;//问候语
@property(strong, nonatomic) NSString * iconurl;//用户头像地址

@end
