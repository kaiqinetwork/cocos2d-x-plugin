//
//  YvIMGroupUserMDYNotify.h
//  YvIMSDKAPITestDemo
//
//  Created by liwenjie on 15-3-17.
//  Copyright (c) 2015年 com.yunva.yaya. All rights reserved.
//

////修改资料通知
//#define IM_GROUP_USERMDY_NOTIFY	   0x13001
//namespace x13001 {
//    enum {
//        /*uint32*/  groupid         = 1, //群ID
//        /*uint32*/	userId			= 2, //用户ID
//        /*string*/	name			= 3, //群名称
//        /*string*/	icon			= 4, //群图标
//        /*string*/	announcement	= 5, //群公告
//        /*uint8*/	verify			= 6, //验证方式
//        /*uint8*/	msg_offline		= 7, //是否支持离线消息
//        /*string*/	alias			= 8, //名片修改
//    };
//}

#import "YvBackBase.h"

@interface YvIMGroupUserMDYNotify : YvBackBase

@property(assign, nonatomic) UInt32     groupid;//群ID
@property(assign, nonatomic) UInt32     userId;//用户ID
@property(strong, nonatomic) NSString * name;//群名称
@property(strong, nonatomic) NSString * icon;//群图标
@property(strong, nonatomic) NSString * announcement;//群公告
@property(assign, nonatomic) UInt8      verify;//验证方式
@property(assign, nonatomic) UInt8      msg_offline;//是否支持离线消息
@property(strong, nonatomic) NSString * alias;//名片修改

@end
