//
//  YvIMLoginResp.h
//  YvIMSDKAPI
//  云娃登录返回
//  Created by liwenjie on 15-3-9.
//  Copyright (c) 2015年 com.yunva.yaya. All rights reserved.
//

//#define IM_LOGIN_RESP	0x11001

#import "YvBackBase.h"
@interface YvIMLoginResp : YvBackBase

@property(assign, nonatomic) UInt32         result; //返回结果 不为0即为失败
@property(strong, nonatomic) NSString *     msg; //错误描述
@property(strong, nonatomic) NSString *     nickname; //用户昵称
@property(assign, nonatomic) UInt32         userId; //用户ID
@property(strong, nonatomic) NSString *     iconurl; //用户图像地址

@end
