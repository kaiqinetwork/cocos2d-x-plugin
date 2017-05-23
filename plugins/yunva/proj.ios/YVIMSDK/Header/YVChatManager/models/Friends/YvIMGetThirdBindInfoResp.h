//
//  UvIMGetThirdBindInfoResp.h
//  YvIMSDKAPITestDemo
//
//  Created by liwenjie on 15-5-20.
//  Copyright (c) 2015年 com.yunva.yaya. All rights reserved.
//

#import "YvBackBase.h"
//#define IM_GET_THIRDBINDINFO_RESP             0x11015
//namespace x11015 {
//    enum {
//        /*uint32*/ result		= 1,
//        /*string*/ msg			= 2,
//        /*uint32*/ yunvaid      = 3,
//        /*string*/ nickname     = 4,
//        /*string*/ iconUrl      = 5,
//        /*string*/ level        = 6,
//        /*string*/ vip          = 7,
//        /*string*/ ext          = 8,
//    };
//}
@interface YvIMGetThirdBindInfoResp : YvBackBase

@property(assign, nonatomic) UInt32 result;
@property(strong, nonatomic) NSString * msg;
@property(assign, nonatomic) UInt32 yunvaid;
@property(strong, nonatomic) NSString * nickname;
@property(strong, nonatomic) NSString * iconUrl;
@property(strong, nonatomic) NSString * level;
@property(strong, nonatomic) NSString * vip;
@property(strong, nonatomic) NSString * ext;

@end
