//
//  YvXUserInfo.h
//  YvIMSDKAPI
//  用户信息结构体
//  Created by liwenjie on 15-3-9.
//  Copyright (c) 2015年 com.yunva.yaya. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "YvXP2PChatMsg.h"
@interface YvXUserInfo : NSObject

@property(strong, nonatomic) NSString * nickname; //用户昵称
@property(assign, nonatomic) UInt32     userid; //用户ID
@property(strong, nonatomic) NSString * iconurl; //用户图像地址
@property(assign, nonatomic) UInt8      online; //是否在线
@property(strong, nonatomic) NSString * userlevel; //用户等级
@property(strong, nonatomic) NSString * viplevel; //vip等级
@property(strong, nonatomic) NSString * ext; //扩展字段
@property(assign, nonatomic) UInt8      shieldmsg; //是否屏蔽聊天消息
@property(assign, nonatomic) UInt8      sex; //性别
@property(strong, nonatomic) NSString * group; //所在组名称
@property(strong, nonatomic) NSString * remark; //备注

-(id)initWithP2PMsg:(YvXP2PChatMsg *)p2pChatMsg;
@end
