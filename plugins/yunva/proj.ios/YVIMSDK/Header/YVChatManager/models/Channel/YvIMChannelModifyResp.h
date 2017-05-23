//
//  YvIMChannelModifyResp.h
//  YvIMSDKAPITestDemo
//
//  Created by whe on 15/5/29.
//  Copyright (c) 2015年 com.yunva.yaya. All rights reserved.
//


//修改通配符
// #define IM_CHANNEL_MODIFY_RESP           0x16012
// namespace x16012{
// enum {
// /*uint32*/ result               = 1,
///*string*/ msg                  = 2,
//};
//}

#import "YvBackBase.h"

@interface YvIMChannelModifyResp : YvBackBase
#ifdef czw
@property(assign,nonatomic) UInt32 result;
@property(strong,nonatomic) NSString *msg;
#endif
@end
