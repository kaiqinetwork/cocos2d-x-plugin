//
//  YvIMUserGetInfoResp.h
//  YvIMSDKAPI
//  个人信息回应
//  Created by liwenjie on 15-3-9.
//  Copyright (c) 2015年 com.yunva.yaya. All rights reserved.
//

//#define IM_USER_GETINFO_RESP            0x19014

#import "YvBackBase.h"

@interface YvIMUserGetInfoResp : YvBackBase
@property(assign, nonatomic) UInt32             userid;//用户id
@property(assign, nonatomic) UInt8              sex;//性别
@property(strong, nonatomic) NSString *         nickname;//昵称
@property(strong, nonatomic) NSString *         headicon;//图像地址

@end
