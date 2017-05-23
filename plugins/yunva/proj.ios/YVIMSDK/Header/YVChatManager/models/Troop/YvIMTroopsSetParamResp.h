//
//  YvIMTroopsSetParamResp.h
//  YvIMSDKAPITestDemo
//
//  Created by liwenjie on 15-3-25.
//  Copyright (c) 2015年 com.yunva.yaya. All rights reserved.
//
//#define CMDNO_TROOPS_SET_PARAM_RESP							0x17046
//namespace troops_17046 {
//    enum {
//        /*uint32*/	result              = 1,
//        /*string*/	msg		            = 2,
//        /*uint32*/	userId				= 3,				//用户
//        /*string*/	teanName			= 4,				//队伍名称
//        /*string*/	announcement		= 5,				//公告
//        /*uint32*/	sequenceTime		= 6,				//麦序时间
//        /*uint32*/	maxCount			= 7,				//房间在线最大用户数
//        /*string*/	passwd				= 8,				//密码设置
//    };
//}

#import "YvBackBase.h"

@interface YvIMTroopsSetParamResp : YvBackBase
#ifdef czw
@property(assign, nonatomic) UInt32     result;
@property(strong, nonatomic) NSString * msg;
@property(assign, nonatomic) UInt32     userId;//用户
@property(strong, nonatomic) NSString * teanName;//队伍名称
@property(strong, nonatomic) NSString * announcement;//公告
@property(assign, nonatomic) UInt32     sequenceTime;//麦序时间
@property(assign, nonatomic) UInt32     maxCount;//房间在线最大用户数
@property(assign, nonatomic) UInt8      passwd;//密码设置

#endif


@end
