//
//  YvTroopsCreateResp.h
//  YvIMSDKAPITestDemo
//
//  Created by liwenjie on 15-3-25.
//  Copyright (c) 2015年 com.yunva.yaya. All rights reserved.
//

//创建队伍响应
//#define CMDNO_TROOPS_CREATE_RESP         0x17002
//namespace troops_17002
//{
//    enum
//    {
//        /*uint32*/		result      = 1,
//        /*string*/		msg		    = 2,
//        /*uint32*/      troopsId    = 3,   //队伍ID
//        /*string*/      troopsName  = 4,   //队伍名称
//    };
//}

#import "YvBackBase.h"

@interface YvIMTroopsCreateResp : YvBackBase
#ifdef czw



@property(assign, nonatomic) UInt32     result;
@property(strong, nonatomic) NSString * msg;
@property(assign, nonatomic) UInt32     troopsId;//队伍id
@property(strong, nonatomic) NSString * troopsName;//队伍名称
#endif
@end
