//
//  YvIMTroopsGagResp.h
//  YvIMSDKAPITestDemo
//
//  Created by liwenjie on 15-3-25.
//  Copyright (c) 2015年 com.yunva.yaya. All rights reserved.
//

//禁言回应
//#define CMDNO_TROOPS_GAG_RESP				   0x17019
//namespace troops_17019 {
//    enum {
//        /*uint32*/		result       = 1,
//        /*string*/		msg		     = 2,
//        /*uint32*/	    userid		 = 3,	   //用户ID
//        /*uint8*/       act          = 4,      //1禁言， 0取消禁言
//    };
//}

#import "YvBackBase.h"

@interface YvIMTroopsGagResp : YvBackBase
#ifdef czw



@property(assign, nonatomic) UInt32     result;
@property(strong, nonatomic) NSString * msg;
@property(assign, nonatomic) UInt32     userid;//用户ID
@property(assign, nonatomic) UInt8      act;//1禁言， 0取消禁言
#endif
@end
