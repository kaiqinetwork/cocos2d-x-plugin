//
//  YvxAroundUser.h
//  YvIMSDKAPITestDemo
//
//  Created by whe on 15/7/14.
//  Copyright (c) 2015年 com.yunva.yaya. All rights reserved.
//

////附件用户
//namespace xAroundUser{
//    enum{
//        /*uint32*/      id				= 1,	//用户ID
//        /*string*/		nickname		= 2,	//昵称
//        /*string*/		city			= 3,	//所在城市
//        /*uint8*/		sex				= 4,	//性别
//        /*uint32*/		distance		= 5,	//距离，单位：米
//    };
//}
//


#import <Foundation/Foundation.h>
@interface YvxAroundUser : NSObject
@property(assign,nonatomic) UInt32 userid;
@property(strong,nonatomic) NSString *nickname;
@property(strong,nonatomic) NSString *city;
@property(assign,nonatomic) UInt8 sex;
@property(assign,nonatomic) UInt32 distance;
@end
