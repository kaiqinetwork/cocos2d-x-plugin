//
//  YvIMGroupInviteResp.h
//  YvIMSDKAPITestDemo
//
//  Created by liwenjie on 15-3-17.
//  Copyright (c) 2015年 com.yunva.yaya. All rights reserved.
//

//邀请好友入群响应
//#define IM_GROUP_INVITE_RESP   0x13024
//namespace x13024 {
//    enum {
//        /*uint32*/ result    = 1, //结果信息
//        /*string*/ msg       = 2, //错误信息
//        /*uint32*/ groupid   = 3, //群ID
//        /*uint32*/ inviteid  = 4, //被邀请用户ID
//        /*string*/ groupname = 5, //群名称
//        /*uint8*/  agree     = 6, //是否同意入群 e_group_invite
//        /*string*/ greet	 = 7, //问候语
//    };
//}

#import "YvBackBase.h"

@interface YvIMGroupInviteResp : YvBackBase

@property(assign, nonatomic) UInt32     result;//结果信息
@property(strong, nonatomic) NSString * msg;//错误信息
@property(assign, nonatomic) UInt32     groupid;//群ID
@property(assign, nonatomic) UInt32     inviteid;//被邀请用户ID
@property(strong, nonatomic) NSString * groupname;//群名称
@property(assign, nonatomic) UInt8      agree;//是否同意入群 e_group_invite
@property(strong, nonatomic) NSString * greet;//问候语

@end
