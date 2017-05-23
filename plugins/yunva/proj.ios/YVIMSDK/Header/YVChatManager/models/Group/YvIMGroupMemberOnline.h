//
//  YvIMGroupMemberOnline.h
//  YvIMSDKAPITestDemo
//
//  Created by liwenjie on 15-3-17.
//  Copyright (c) 2015年 com.yunva.yaya. All rights reserved.
//

//群成员上线
//#define IM_GROUP_MEMBER_ONLINE   0x13034
//namespace x13034 {
//    enum {
//        /*uint32*/ groupid = 1, //群ID
//        /*uint32*/ userid  = 2, //用户ID
//        /*uint8*/  online  = 3, //用户是否在线 group_member_online
//    };
//}

#import "YvBackBase.h"

@interface YvIMGroupMemberOnline : YvBackBase

@property(assign, nonatomic) UInt32 groupid;//群ID
@property(assign, nonatomic) UInt32 userid;//用户ID
@property(assign, nonatomic) UInt8  online;//用户是否在线 group_member_online

@end
