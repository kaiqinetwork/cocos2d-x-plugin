//
//  YvXSearchInfo.h
//  YvIMSDKAPI
//  搜索用户信息结构体
//  Created by liwenjie on 15-3-9.
//  Copyright (c) 2015年 com.yunva.yaya. All rights reserved.
//

#import <Foundation/Foundation.h>
@interface YvXSearchInfo : NSObject

@property(assign, nonatomic) UInt32     yunvaId;
@property(strong, nonatomic) NSString * userId; //用户ID
@property(strong, nonatomic) NSString * nickName; //用户昵称
@property(strong, nonatomic) NSString * iconUrl; //用户图像地址
@property(strong, nonatomic) NSString * level; //用户等级
@property(strong, nonatomic) NSString * vip; //用户VIP等级
@property(strong, nonatomic) NSString * Ext; //扩展字段

@end
