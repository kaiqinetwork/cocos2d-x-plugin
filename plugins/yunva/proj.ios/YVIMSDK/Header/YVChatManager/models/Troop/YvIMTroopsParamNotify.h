//
//  YvIMTroopsParamNotify.h
//  YvIMSDKAPITestDemo
//
//  Created by liwenjie on 15-3-25.
//  Copyright (c) 2015年 com.yunva.yaya. All rights reserved.
//
//房间属性通知
//#define CMDNO_TROOPS_PARAM_NOTIFY							0x17047
//namespace troops_17047 {
//    enum {
//        /*uint32*/	op_userId			= 1,				//操作用户ID
//        /*string*/	teanName			= 2,				//队伍名称
//        /*string*/	announcement		= 3,				//公告
//        /*uint32*/	sequenceTime		= 4,				//麦序时间
//        /*uint32*/	maxCount			= 5,				//房间在线最大用户数
//        /*string*/	passwd				= 6,				//密码设置
//    };
//}
#import "YvBackBase.h"

@interface YvIMTroopsParamNotify : YvBackBase
#ifdef czw


@property(assign, nonatomic) UInt32     op_userId;//操作用户ID
@property(strong, nonatomic) NSString * teanName;//队伍名称
@property(strong, nonatomic) NSString * announcement;//公告
@property(assign, nonatomic) UInt32     sequenceTime;//麦序时间
@property(assign, nonatomic) UInt32     maxCount;//房间在线最大用户数
@property(assign, nonatomic) NSString * passwd;//密码设置
#endif

@end
