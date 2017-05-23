//
//  YvIMTroopsUserLogoutNotify.h
//  YvIMSDKAPITestDemo
//
//  Created by liwenjie on 15-3-25.
//  Copyright (c) 2015年 com.yunva.yaya. All rights reserved.
//
//用户登出通知
//#define CMDNO_TROOPS_USERLOGOUT_NOTIFY          0x17008
//namespace troops_17008
//{
//    enum
//    {
//        /*uint32*/      userId      = 1,       //用户ID
//    };
//}
#import "YvBackBase.h"

@interface YvIMTroopsUserLogoutNotify : YvBackBase
#ifdef czw

@property(assign, nonatomic) UInt32 userId;//用户ID

#endif

@end
