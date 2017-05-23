//
//  YvIMTroopsTopWheatResp.h
//  YvIMSDKAPITestDemo
//
//  Created by liwenjie on 15-3-25.
//  Copyright (c) 2015年 com.yunva.yaya. All rights reserved.
//
//#define CMDNO_TROOPS_TOP_WHEAT_RESP						0x17037
//namespace troops_17037 {
//    enum {
//        /*uint32*/		result       = 1,
//        /*string*/		msg		     = 2,
//        /*uint32*/	    userId       = 3,			  //被操作者userId
//    };
//}
#import "YvBackBase.h"

@interface YvIMTroopsTopWheatResp : YvBackBase
#ifdef czw
@property(assign, nonatomic) UInt32     result;
@property(strong, nonatomic) NSString * msg;
@property(assign, nonatomic) UInt32     userId;//被操作者userId

#endif


@end
