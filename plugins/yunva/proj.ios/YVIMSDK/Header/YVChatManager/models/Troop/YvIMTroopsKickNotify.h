//
//  YvIMTroopsKickNotify.h
//  YvIMSDKAPITestDemo
//
//  Created by liwenjie on 15-3-25.
//  Copyright (c) 2015年 com.yunva.yaya. All rights reserved.
//
//踢人通知
//#define CMDNO_TROOPS_KICK_NOTIFY							0x17044
//namespace troops_17044 {
//    enum {
//        /*uint32*/	op_userId		= 1,					//操作ID
//        /*uint32*/	to_userId		= 2,					//被踢出ID
//    };
//}
#import "YvBackBase.h"

@interface YvIMTroopsKickNotify : YvBackBase
#ifdef czw



@property(assign, nonatomic) UInt32 op_userId;//操作ID
@property(assign, nonatomic) UInt32 to_userId;//被踢出ID
#endif
@end
