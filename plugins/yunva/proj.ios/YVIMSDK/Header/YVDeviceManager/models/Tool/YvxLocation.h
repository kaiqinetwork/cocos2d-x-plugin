//
//  YvxLocation.h
//  YvIMSDKAPITestDemo
//
//  Created by whe on 15/7/14.
//  Copyright (c) 2015年 com.yunva.yaya. All rights reserved.
//

//namespace xLocation{
//    enum{
//        /*string*/ city		= 1,
//        /*string*/ province = 2,
//        /*string*/ district = 3,
//        /*string*/ detail	= 4,	//详细地址
//    };
//}



#import <Foundation/Foundation.h>
@interface YvxLocation : NSObject
@property(strong,nonatomic) NSString *city;
@property(strong,nonatomic) NSString *province;
@property(strong,nonatomic) NSString *district;
@property(strong,nonatomic) NSString *detail;
@end
