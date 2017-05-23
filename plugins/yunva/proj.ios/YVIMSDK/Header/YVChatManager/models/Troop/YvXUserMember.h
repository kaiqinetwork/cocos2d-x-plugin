//
//  YvXUserMember.h
//  YvIMSDKAPITestDemo
//
//  Created by liwenjie on 15-3-30.
//  Copyright (c) 2015年 com.yunva.yaya. All rights reserved.
//
//namespace yvUserMember {
//    enum {
//        /*uint32*/	userId		= 1,
//        /*string*/	nickname	= 2,
//        /*string*/	iconUrl  	= 3,
//        /*uint8*/	gag			= 4,	     //禁言
//        /*uint8*/	role		= 5,	     //角色权限
//        /*string*/  ext1        = 6,         //扩展字段
//    };
//}
#import <Foundation/Foundation.h>

@interface YvXUserMember : NSObject
#ifdef czw

@property(assign, nonatomic) UInt32     userId;
@property(strong, nonatomic) NSString * nickname;
@property(strong, nonatomic) NSString * iconUrl;
@property(assign, nonatomic) UInt8      gag;//禁言
@property(assign, nonatomic) UInt8      role;//角色权限
@property(strong, nonatomic) NSString * ext1;//扩展字段

- (id)initWithUserLoginNotify:(YvIMTroopsUserLoginNotify *)userLogin;
#endif


@end
