//
//  YvIMUploadLocationResp.h
//  YvIMSDKAPITestDemo
//
//  Created by whe on 15/7/14.
//  Copyright (c) 2015年 com.yunva.yaya. All rights reserved.
//


//#define IM_UPLOAD_LOCATION_RESP			0x19031
//namespace x19031{
//    enum
//    {
//        /*uint32*/		result			= 1,   //结果
//        /*string*/      msg				= 2,   //错误描述
//    };
//}




#import "YvBackBase.h"
@interface YvIMUploadLocationResp : YvBackBase
@property(assign,nonatomic) UInt32 result;
@property(strong,nonatomic) NSString *msg;
@end
