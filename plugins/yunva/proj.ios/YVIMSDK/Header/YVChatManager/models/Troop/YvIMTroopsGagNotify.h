//
//  YvIMTroopsGagNotify.h
//  YvIMSDKAPITestDemo
//
//  Created by liwenjie on 15-3-25.
//  Copyright (c) 2015年 com.yunva.yaya. All rights reserved.
//

//#define CMDNO_TROOPS_GAG_NOTIFY				   0x17020
//namespace troops_17020 {
//    enum {
//        /*uint32*/	op_userId		= 1,		//操作ID
//        /*uint32*/	gagId			= 2,		//禁言ID
//        /*uint8*/   act             = 3,        //1禁言， 0取消禁言
//    };
//}


#import "YvBackBase.h"

@interface YvIMTroopsGagNotify : YvBackBase
#ifdef czw



@property(assign, nonatomic) UInt32 op_userId;//操作ID
@property(assign, nonatomic) UInt32 gagId;//禁言ID
@property(assign, nonatomic) UInt8  act;//1禁言， 0取消禁言
#endif
@end
