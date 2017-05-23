//
//  YvIMTroopsUserLoginNotify.h
//  YvIMSDKAPITestDemo
//
//  Created by liwenjie on 15-3-25.
//  Copyright (c) 2015年 com.yunva.yaya. All rights reserved.
//

//用户登录通知
//#define CMDNO_TROOPS_USERLOGIN_NOTIFY          0x17007
//namespace troops_17007
//{
//    enum
//    {
//        /*uint32*/      userId      = 1,       //用户ID
//        /*string*/      nickname    = 2,       //用户昵称
//        /*string*/      iconUrl     = 3,       //用户头像
//        /*uint8*/	    role		= 4,	   //权限
//        /*string*/      ext1        = 5,       //扩展字段
//    };
//}

#import "YvBackBase.h"

@interface YvIMTroopsUserLoginNotify : YvBackBase
#ifdef czw
@property(assign, nonatomic) UInt32     userId;//用户ID
@property(strong, nonatomic) NSString * nickname;//用户昵称
@property(strong, nonatomic) NSString * iconUrl;//用户头像
@property(assign, nonatomic) UInt8      role;//权限
@property(strong, nonatomic) NSString * ext1;//扩展字段

#endif


@end
