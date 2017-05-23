//
//  YvIMTroopsChangeModeNotify.h
//  YvIMSDKAPITestDemo
//
//  Created by liwenjie on 15-3-25.
//  Copyright (c) 2015年 com.yunva.yaya. All rights reserved.
//
//模式改变通知
//#define CMDNO_TROOPS_CHANGE_MODE_NOTIFY						0x17050
//namespace troops_17050 {
//    enum {
//        /*uint32*/	op_userId			= 1,			//切换人UserID
//        /*uint8*/	mode			= 2,				//房间模式
//    };
//}
#import "YvBackBase.h"

@interface YvIMTroopsChangeModeNotify : YvBackBase
#ifdef czw



@property(assign, nonatomic) UInt32 op_userId;//切换人UserID
@property(assign, nonatomic) UInt8  mode;//房间模式
#endif
@end
