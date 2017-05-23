//
//  YvIMTroopsLoginResp.h
//  YvIMSDKAPITestDemo
//
//  Created by liwenjie on 15-3-25.
//  Copyright (c) 2015年 com.yunva.yaya. All rights reserved.
//

//登陆队伍响应
//#define CMDNO_TROOPS_LOGIN_RESP         0x17004

#import "YvBackBase.h"

@interface YvIMTroopsLoginResp : YvBackBase
#ifdef czw



@property(assign, nonatomic) UInt32     result;
@property(strong, nonatomic) NSString * msg;
@property(assign, nonatomic) UInt32     troopsId;//队伍id
@property(strong, nonatomic) NSString * troopsName;//队伍名称
@property(assign, nonatomic) UInt32     maxCount;//最大用户数
@property(strong, nonatomic) NSString * nickName;//用户昵称
@property(assign, nonatomic) UInt32     userCount;//用户数
@property(assign, nonatomic) UInt8      troopsMode;//房间模式(troopsm_mode)
@property(assign, nonatomic) UInt8      role;//你的角色
@property(strong, nonatomic) NSString * announcement;//队伍公告
#endif
@end
