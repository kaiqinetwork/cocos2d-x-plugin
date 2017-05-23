//
//  YvIMGroupInviteRespon.h
//  YvIMSDKDemo
//
//  Created by mac on 15-8-23.
//  Copyright (c) 2015年 yunva. All rights reserved.
//


////邀请好友入群回应
//#define IM_GROUP_INVITE_RESPON   0x13041
//namespace x13041 {
//    enum {
//        /*uint32*/ result  = 1, //结果信息
//        /*string*/ msg     = 2, //错误信息
//        /*uint32*/ groupid = 3, //群ID
//        /*uint32*/ invitedid  = 4, //被邀请用户ID
//    };
//}
#import "YvBackBase.h"

@interface YvIMGroupInviteRespon : YvBackBase
@property(assign, nonatomic) UInt32     result; //结果信息
@property(strong, nonatomic) NSString * msg;//错误信息
@property(assign, nonatomic) UInt32     groupid; //群ID
@property(assign, nonatomic) UInt32     invitedid;//被邀请用户ID
@end
