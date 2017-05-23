//
//  YvIMGetSdkInfoResp.h
//  YvIMSDKDemo
//
//  Created by dada on 15/9/11.
//  Copyright (c) 2015年 yunva. All rights reserved.
//


//#define IM_GET_SDKINFO_RESP                   0x11018
//namespace x11018 {
//    enum {
//        /*string*/ versions        = 1,     //版本号
//        /*uint8*/  netstate        = 2,     //网络状态
//    };
//}
#import "YvBackBase.h"

@interface YvIMGetSdkInfoResp : YvBackBase
@property(strong, nonatomic) NSString * versions;//版本号
@property(assign, nonatomic) UInt32     netstate;//网络状态
@end
