//
//  YvIMTroopsChangeModeResp.h
//  YvIMSDKAPITestDemo
//
//  Created by liwenjie on 15-3-25.
//  Copyright (c) 2015年 com.yunva.yaya. All rights reserved.
//
//#define CMDNO_TROOPS_CHANGE_MODE_RESP						0x17049
//namespace troops_17049 {
//    enum {
//        /*uint32*/	result              = 1,
//        /*string*/	msg		            = 2,
//        /*uint8*/	mode			= 3,				//队伍模式
//    };
//}
#import "YvBackBase.h"

@interface YvIMTroopsChangeModeResp : YvBackBase
#ifdef czw



@property(assign, nonatomic) UInt32     result;
@property(strong, nonatomic) NSString * msg;
@property(assign, nonatomic) UInt8      mode;//房间模式
#endif
@end
