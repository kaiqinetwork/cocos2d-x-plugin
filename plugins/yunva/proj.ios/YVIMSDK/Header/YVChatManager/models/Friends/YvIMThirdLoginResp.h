//
//  YvIMThirdLoginResp.h
//  YvIMSDKAPI
//  cp账号登录返回
//  Created by liwenjie on 15-3-9.
//  Copyright (c) 2015年 com.yunva.yaya. All rights reserved.
//

//#define IM_THIRD_LOGIN_RESP				0x11003

#import "YvBackBase.h"
@interface YvIMThirdLoginResp : YvBackBase

@property(assign, nonatomic) UInt32         result; //返回结果 不为0即为失败
@property(strong, nonatomic) NSString *     msg; //错误描述
@property(assign, nonatomic) UInt32         userid; //用户名加密字符串
@property(strong, nonatomic) NSString *     nickName; //用户昵称
@property(strong, nonatomic) NSString *     iconUrl; //用户图像地址
//@property(assign, nonatomic) UInt32         thirdUserId; //第三方用户ID
@property(strong , nonatomic) NSString * thirdUserId; //第三方用户ID
@property(strong, nonatomic) NSString *     thirdUserName; //第三方用户名

@end
