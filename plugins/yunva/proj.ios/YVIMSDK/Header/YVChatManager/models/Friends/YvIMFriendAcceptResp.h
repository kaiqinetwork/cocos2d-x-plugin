//
//  YvIMFriendAddAcceptResp.h
//  YvIMSDKAPI
//  同意添加好友回应
//  Created by liwenjie on 15-3-9.
//  Copyright (c) 2015年 com.yunva.yaya. All rights reserved.
//

//#define IM_FRIEND_ACCEPT_RESP   0x12004

#import "YvBackBase.h"

@interface YvIMFriendAcceptResp : YvBackBase

@property(assign, nonatomic) UInt32     userid; //用户ID
@property(assign, nonatomic) UInt8      affirm; //返回结果 e_addfriend_affirm
@property(strong, nonatomic) NSString * greet; //问候语

@end
