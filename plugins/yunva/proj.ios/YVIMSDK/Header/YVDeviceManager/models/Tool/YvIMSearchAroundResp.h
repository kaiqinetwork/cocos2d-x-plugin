//
//  YvIMSearchAroundResp.h
//  YvIMSDKAPITestDemo
//
//  Created by whe on 15/7/14.
//  Copyright (c) 2015年 com.yunva.yaya. All rights reserved.
//

//#define IM_SEARCH_AROUND_RESP			0x19035
//namespace x19035{
//    enum
//    {
//        /*uint32*/			result			= 1,	//结果
//        /*string*/			msg				= 2,	//错误描述
//        /*xAroundUser[]*/	user			= 3,	//用户列表
//    };
//}




#import "YvBackBase.h"
@interface YvIMSearchAroundResp : YvBackBase
@property(assign,nonatomic) UInt32 result;
@property(strong,nonatomic) NSString *msg;
@property(strong,nonatomic) NSMutableArray *users;
@end
