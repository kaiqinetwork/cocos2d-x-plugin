//
//  YvIMTroopsAppointNotify.h
//  YvIMSDKAPITestDemo
//
//  Created by liwenjie on 15-3-25.
//  Copyright (c) 2015年 com.yunva.yaya. All rights reserved.
//
//队长转让通知
//#define CMDNO_TROOPS_APPOINT_NOTIFY          0x17017
//namespace troops_17017
//{
//    enum
//    {
//        /*uint32*/		oldId       = 1,      //原队长ID
//        /*uint32*/      userId      = 2,      //新队长ID
//    };
//}

#import "YvBackBase.h"

@interface YvIMTroopsAppointNotify : YvBackBase
#ifdef czw



@property(assign, nonatomic) UInt32 oldId;//原队长ID
@property(assign, nonatomic) UInt32 userId;//新队长ID
#endif
@end
