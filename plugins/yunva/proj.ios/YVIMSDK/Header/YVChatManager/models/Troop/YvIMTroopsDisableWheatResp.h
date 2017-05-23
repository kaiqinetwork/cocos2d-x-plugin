//
//  YvIMTroopsDisableWheatResp.h
//  YvIMSDKAPITestDemo
//
//  Created by liwenjie on 15-3-25.
//  Copyright (c) 2015年 com.yunva.yaya. All rights reserved.
//
//#define CMDNO_TROOPS_DISABLE_WHEAT_RESP				0x17029
//namespace troops_17029{
//    enum {
//        /*uint32*/		result       = 1,
//        /*string*/		msg		     = 2,
//        /*uint8*/	    disable		 = 3,			//1禁麦 0开麦
//    };
//}
#import "YvBackBase.h"

@interface YvIMTroopsDisableWheatResp : YvBackBase
#ifdef czw



@property(assign, nonatomic) UInt32     result;
@property(strong, nonatomic) NSString * msg;
@property(assign, nonatomic) UInt8      disable;//1禁麦 0开麦
#endif
@end
