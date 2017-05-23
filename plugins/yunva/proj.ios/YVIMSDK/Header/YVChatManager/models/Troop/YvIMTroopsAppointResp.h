//
//  YvIMTroopsAppointResp.h
//  YvIMSDKAPITestDemo
//
//  Created by liwenjie on 15-3-25.
//  Copyright (c) 2015年 com.yunva.yaya. All rights reserved.
//

//队长转让回应
//#define CMDNO_TROOPS_APPOINT_RESP          0x17016
//namespace troops_17016
//{
//    enum
//    {
//        /*uint32*/		result       = 1,
//        /*string*/		msg		     = 2,
//        /*uint32*/      to_userId    = 3,      //转让对象ID
//    };
//}

#import "YvBackBase.h"

@interface YvIMTroopsAppointResp : YvBackBase
#ifdef czw



@property(assign, nonatomic) UInt32     result;
@property(strong, nonatomic) NSString * msg;
@property(assign, nonatomic) UInt32     to_userId;//转让对象ID
#endif
@end
