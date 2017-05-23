//
//  YvIMGroupSetRoleNotify.h
//  YvIMSDKAPITestDemo
//
//  Created by liwenjie on 15-3-17.
//  Copyright (c) 2015年 com.yunva.yaya. All rights reserved.
//

//设置群成员角色通知
//#define IM_GROUP_SETROLE_NOTIFY    0x13027
//namespace x13027 {
//    enum {
//        /*uint32*/ groupid  = 1, //群ID
//        /*uint32*/ operid   = 2, //操作者ID
//        /*uint32*/ byuserid = 3, //被被操作者ID
//        /*uint32*/ role     = 4, //修改后角色 e_group_role
//    };
//}

#import "YvBackBase.h"

@interface YvIMGroupSetRoleNotify : YvBackBase

@property(assign, nonatomic) UInt32 groupid;//群ID
@property(assign, nonatomic) UInt32 operid;//操作者ID
@property(assign, nonatomic) UInt32 byuserid;//被被操作者ID
@property(assign, nonatomic) UInt32 role;//修改后角色 e_group_role

@end
