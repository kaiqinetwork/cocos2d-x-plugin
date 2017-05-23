//
//  YvIMTroopsClearWheatResp.h
//  YvIMSDKAPITestDemo
//
//  Created by liwenjie on 15-3-25.
//  Copyright (c) 2015年 com.yunva.yaya. All rights reserved.
//
//#define CMDNO_TROOPS_CLEAR_WHEAT_RESP				  0x17035
//namespace troops_17035 {
//    enum {
//        /*uint32*/		result       = 1,
//        /*string*/		msg		     = 2,
//    };
//}
#import "YvBackBase.h"

@interface YvIMTroopsClearWheatResp : YvBackBase
#ifdef czw





@property(assign, nonatomic) UInt32     result;
@property(strong, nonatomic) NSString * msg;
#endif
@end
