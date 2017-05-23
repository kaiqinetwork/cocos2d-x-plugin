//
//  YvIMGroupInviteAccesptResp.h
//  YvIMSDKDemo
//
//  Created by mac on 15-8-23.
//  Copyright (c) 2015年 yunva. All rights reserved.
//

////被邀请者同意或拒绝群邀请响应
//#define IM_GROUP_INVITE_ACCEPT_RESP   0x13042
//namespace x13042 {
//    enum {
//        /*uint32*/ result    = 1, //结果信息
//        /*string*/ msg       = 2, //错误信息
//        /*uint32*/ groupid   = 3, //群ID
//        /*uint32*/ inviteid  = 4, //邀请用户ID
//    };
//}
#import "YvBackBase.h"

@interface YvIMGroupInviteAccesptResp : YvBackBase
@property(assign, nonatomic) UInt32     result;//结果信息
@property(strong, nonatomic) NSString * msg;//错误信息
@property(assign, nonatomic) UInt32     groupid;//群ID
@property(assign, nonatomic) UInt32     inviteid;//邀请用户ID
@end
