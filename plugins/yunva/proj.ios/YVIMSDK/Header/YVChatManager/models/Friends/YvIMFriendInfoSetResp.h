//
//  YvIMFriendInfoSetResp.h
//  YvIMSDKAPI
////  设置好友信息回应
//  Created by liwenjie on 15-3-9.
//  Copyright (c) 2015年 com.yunva.yaya. All rights reserved.
//

//#define IM_FRIEND_INFOSET_RESP	0x12017

#import "YvBackBase.h"
@interface YvIMFriendInfoSetResp : YvBackBase

@property(assign, nonatomic) UInt32     friendId; //好友ID
@property(strong, nonatomic) NSString * group; //好友所在组
@property(strong, nonatomic) NSString * note; //好友备注

@end
