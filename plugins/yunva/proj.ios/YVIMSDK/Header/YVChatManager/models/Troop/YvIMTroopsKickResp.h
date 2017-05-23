//
//  YvIMTroopsKickResp.h
//  YvIMSDKAPITestDemo
//
//  Created by liwenjie on 15-3-25.
//  Copyright (c) 2015年 com.yunva.yaya. All rights reserved.
//

//踢人回应
//#define CMDNO_TROOPS_KICK_RESP								0x17043
//namespace troops_17043 {
//    enum {
//        /*uint32*/		result       = 1,
//        /*string*/		msg		     = 2,
//        /*uint32*/	    userid	     = 3,					//被踢出用户ID
//    };
//}

#import "YvBackBase.h"

@interface YvIMTroopsKickResp : YvBackBase
#ifdef czw



@property(assign, nonatomic) UInt32     result;
@property(strong, nonatomic) NSString * msg;
@property(assign, nonatomic) UInt32     userId;//被踢出用户ID
#endif
@end
