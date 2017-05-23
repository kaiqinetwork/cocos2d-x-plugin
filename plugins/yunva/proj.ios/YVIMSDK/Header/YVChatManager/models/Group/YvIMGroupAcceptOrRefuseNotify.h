//
//  YvIMGroupAcceptOrRefuseNotify.h
//  YvIMSDKDemo
//
//  Created by dada on 15/8/13.
//  Copyright (c) 2015年 yunva. All rights reserved.
//

////同意拒绝加群
//#define IM_GROUP_JOIN_ACCEPT    0x13008
//namespace x13008 {
//    enum {
//        /*uint32*/ groupid = 1, //群ID
//        /*uint32*/ userid  = 2, //用户ID
//        /*uint8*/  agree   = 3, //e_joingroup
//        /*string*/ greet   = 4, //拒绝原因
//    };
//}
#import "YvBackBase.h"

@interface YvIMGroupAcceptOrRefuseNotify : YvBackBase
@property(assign, nonatomic) UInt32 groupid ;//群ID
@property(assign, nonatomic) UInt32 userid  ; //用户ID
@property(assign, nonatomic) UInt8  agree  ; //e_joingroup
@property(strong, nonatomic) NSString * greet   ; //拒绝原因



@end
