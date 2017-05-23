//
//  YvIMGroupUserJoinNotify.h
//  YvIMSDKAPITestDemo
//
//  Created by liwenjie on 15-3-17.
//  Copyright (c) 2015年 com.yunva.yaya. All rights reserved.
//

//新成员加入群
//#define IM_GROUP_USERJOIN_NOTIFY  0x13035
//namespace x13035 {
//    enum {
//        /*uint32*/     groupid = 1, //群ID
//        /*xGroupUser*/ xUser   = 2, //用户信息
//    };
//}


#import "YvBackBase.h"
#import "YvXGroupUser.h"

@interface YvIMGroupUserJoinNotify : YvBackBase

@property(assign, nonatomic) UInt32         groupid;//群ID
@property(strong, nonatomic) YvXGroupUser * xGroupUser;//用户信息


@property(assign, nonatomic) NSInteger sendtime;  //记录消息来的时间  mark by czw 9.17
@property(assign, nonatomic) NSInteger     category;//0.好友1.申请入群2.群主邀请入群3.其他通知
@property(assign, nonatomic) NSInteger     userstatus;//0.拒绝（显示“已拒绝”）1.同意（显示“已同意”）2.为处理（显示拒绝和同意按钮）3.其他通知（不显示按钮）

@end
