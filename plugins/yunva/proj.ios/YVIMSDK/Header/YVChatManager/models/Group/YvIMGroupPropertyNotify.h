//
//  YvIMGroupPropertyNotify.h
//  YvIMSDKAPITestDemo
//
//  Created by liwenjie on 15-3-17.
//  Copyright (c) 2015年 com.yunva.yaya. All rights reserved.
//

//群属性通知(群列表)
//#define IM_GROUP_PROPERTY_NOTIFY   0x13033
//namespace x13033 {
//    enum {
//        /*uint32*/  groupid         = 1, //群ID
//        /*string*/	name			= 2, //群名称
//        /*string*/	icon			= 3, //群图标
//        /*string*/	announcement	= 4, //群公告
//        /*uint8*/	level			= 5, //群等级
//        /*uint8*/	verify			= 6, //验证方式
//        /*uint32*/	number_limit	= 7, //人数限制
//        /*uint32*/	owner			= 8, //群所有者
//        /*uint8*/	msg_set			= 9, //群消息设置
//        /*uint8*/	msg_offline		= 10, //是否支持离线消息 e_group_msg
//    };
//}

#import "YvBackBase.h"
#import "YvIMGroupUserListNotify.h"
#import "YvXGroupChatMsg.h"
@interface YvIMGroupPropertyNotify : YvBackBase

@property(assign, nonatomic) UInt32     groupid;//群ID
@property(strong, nonatomic) NSString * name;//群名称
@property(strong, nonatomic) NSString * icon;//群图标
@property(strong, nonatomic) NSString * announcement;//群公告
@property(assign, nonatomic) UInt8      level;//群等级
@property(assign, nonatomic) UInt8      verify;//验证方式
@property(assign, nonatomic) UInt32     number_limit;//人数限制
@property(assign, nonatomic) UInt32     owner;//群所有者
@property(assign, nonatomic) UInt8      msg_set;//群消息设置
@property(strong, nonatomic) NSMutableArray * groupUserList;//群用户列表
//根据业务需求，额外添加的属性
@property(assign, nonatomic) UInt32     unReadNum;//未读消息条数

-(id)initWithGroupMsg:(YvXGroupChatMsg *)xgroupMsg;
@end
