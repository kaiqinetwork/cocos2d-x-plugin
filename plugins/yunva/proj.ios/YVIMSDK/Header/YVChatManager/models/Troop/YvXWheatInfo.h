//
//  YvWheatInfo.h
//  YvIMSDKAPITestDemo
//
//  Created by liwenjie on 15-3-25.
//  Copyright (c) 2015年 com.yunva.yaya. All rights reserved.
//
//麦序用户属性
//namespace yvWheatInfo
//{
//    enum
//    {
//        /*uint32*/	userId		= 1,
//        /*uint8*/	role		= 2,		//角色
//        /*string*/	nickname	= 3,		//昵称
//    };
//}
#import <Foundation/Foundation.h>

@interface YvXWheatInfo : NSObject
#ifdef czw
@property(assign, nonatomic) UInt32     userId;
@property(assign, nonatomic) UInt8      role;//角色
@property(strong, nonatomic) NSString * nickname;//昵称

#endif


@end
