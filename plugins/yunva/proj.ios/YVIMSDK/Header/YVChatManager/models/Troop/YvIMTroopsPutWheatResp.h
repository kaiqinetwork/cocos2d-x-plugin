//
//  YvIMTroopsPutWheatResp.h
//  YvIMSDKAPITestDemo
//
//  Created by liwenjie on 15-3-25.
//  Copyright (c) 2015年 com.yunva.yaya. All rights reserved.
//
//#define CMDNO_TROOPS_PUT_WHEAT_RESP				  0x17024
//namespace troops_17024 {
//    enum {
//        /*uint32*/		result       = 1,
//        /*string*/		msg		     = 2,
//    };
//}
#import "YvBackBase.h"

@interface YvIMTroopsPutWheatResp : YvBackBase
#ifdef czw


@property(assign, nonatomic) UInt32     result;
@property(strong, nonatomic) NSString * msg;
#endif

@end
