//
//  YvIMFriendDelResp.h
//  YvIMSDKAPI
//  删除好友回应
//  Created by liwenjie on 15-3-9.
//  Copyright (c) 2015年 com.yunva.yaya. All rights reserved.
//

#import "YvBackBase.h"
//#define IM_FRIEND_DEL_RESP   0x12006
@interface YvIMFriendDelResp : YvBackBase

@property(assign, nonatomic) UInt32         result;//0为操作成功，其它失败
@property(strong, nonatomic) NSString *     msg;//描述信息
@property(assign, nonatomic) UInt32         del_friend;//删除好友id
@property(assign, nonatomic) UInt8          act;//动作 e_delfriend

@end
