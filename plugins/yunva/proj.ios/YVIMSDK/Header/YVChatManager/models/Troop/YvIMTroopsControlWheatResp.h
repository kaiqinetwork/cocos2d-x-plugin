//
//  YvIMTroopsControlWheatResp.h
//  YvIMSDKAPITestDemo
//
//  Created by liwenjie on 15-3-25.
//  Copyright (c) 2015年 com.yunva.yaya. All rights reserved.
//

//#define CMDNO_TROOPS_CONTROL_WHEAT_RESP				0x17027
//namespace troops_17027 {
//    enum {
//        /*uint32*/		result       = 1,
//        /*string*/		msg		     = 2,
//        /*uint8*/	    control		 = 3,			//1控麦 0放麦
//    };
//}

#import "YvBackBase.h"

@interface YvIMTroopsControlWheatResp : YvBackBase
#ifdef czw



@property(assign, nonatomic) UInt32     result;
@property(strong, nonatomic) NSString * msg;
@property(assign, nonatomic) UInt8      control;//1控麦 0放麦
#endif
@end
