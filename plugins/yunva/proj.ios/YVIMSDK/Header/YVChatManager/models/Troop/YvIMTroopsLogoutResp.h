//
//  YvIMTroopsLogoutResp.h
//  YvIMSDKAPITestDemo
//
//  Created by liwenjie on 15-3-25.
//  Copyright (c) 2015年 com.yunva.yaya. All rights reserved.
//

//登出队伍响应

#import "YvBackBase.h"

@interface YvIMTroopsLogoutResp : YvBackBase
#ifdef czw


@property(assign, nonatomic) UInt32     result;
@property(strong, nonatomic) NSString * msg;
@property(assign, nonatomic) UInt32     troopsId;//队伍ID
#endif

@end
