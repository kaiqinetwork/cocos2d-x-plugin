//
//  YvIMGetCacheFileResp.h
//  YvIMSDKAPITestDemo
//
//  Created by whe on 15/6/8.
//  Copyright (c) 2015年 com.yunva.yaya. All rights reserved.
//


// #define IM_GET_CACHE_FILE_RESP           0x19019
// namespace x19019{
//	enum
//	{
// /*uint32*/		result			= 1,   //结果
///*string*/      msg				= 2,   //错误描述
///*string*/      url             = 3,
///*string*/      filepath            = 4,   //获取返回文件
//};
//}
#import "YvBackBase.h"

@interface YvIMGetCacheFileResp : YvBackBase
@property(assign,nonatomic) UInt32 result;
@property(strong,nonatomic) NSString *msg;
@property(strong,nonatomic) NSString *url;
@property(strong,nonatomic) NSString *filepath;
@end
