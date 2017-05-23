//
//  YvIMGetLocationResp.h
//  YvIMSDKAPITestDemo
//
//  Created by whe on 15/7/14.
//  Copyright (c) 2015年 com.yunva.yaya. All rights reserved.
//

//#define IM_GET_LOCATION_RESP			0x19033
//namespace x19033{
//    enum{
//        /*uint32*/		result			= 1,   //结果
//        /*string*/      msg				= 2,   //错误描述
//        /*xLocation*/	location		= 3,	//所在位置
//    };
//}





#import "YvBackBase.h"

@interface YvIMGetLocationResp : YvBackBase
@property(assign,nonatomic) UInt32 result;
@property(strong,nonatomic) NSString *msg;
@property(strong,nonatomic) id location;
@end
