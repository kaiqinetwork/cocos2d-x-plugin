//
//  YvXGroupUser.h
//  YvIMSDKAPITestDemo
//
//  Created by liwenjie on 15-3-17.
//  Copyright (c) 2015年 com.yunva.yaya. All rights reserved.
//

//
//namespace xGroupUser {
//    enum {
//        /*uint32*/	userId			= 1, //用户ID
//        /*string*/  nickname        = 2, //用户昵称
//        /*string*/  iconurl         = 3, //头像
//        /*uint8*/   sex             = 4, //性别
//        /*string*/	alias			= 5, //名片
//        /*uint8*/	role			= 6, //角色
//        /*uint8*/	level			= 7, //等级
//        /*uint32*/	grade			= 8, //积分
//        /*uint32*/	lately_online	= 9, //最后一次上线时间
//        /*uint8*/	online			= 10, //是否在线
//    };
//}


#import <Foundation/Foundation.h>

@interface YvXGroupUser : NSObject

@property(assign, nonatomic) UInt32     userId;//用户ID
@property(strong, nonatomic) NSString * nickname;//用户昵称
@property(strong, nonatomic) NSString * iconurl;//头像
@property(assign, nonatomic) UInt8      sex;//性别
@property(strong, nonatomic) NSString * alias;//名片
@property(assign, nonatomic) UInt8      role;//角色
@property(assign, nonatomic) UInt8      level;//等级
@property(assign, nonatomic) UInt32     grade;//积分
@property(assign, nonatomic) UInt32     lately_online;//最后一次上线时间
@property(assign, nonatomic) UInt8      online;//是否在线

@end
