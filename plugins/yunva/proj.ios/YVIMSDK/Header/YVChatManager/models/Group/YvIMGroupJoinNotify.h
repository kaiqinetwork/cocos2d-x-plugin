//
//  YvIMGroupJoinNotify.h
//  YvIMSDKAPITestDemo
//
//  Created by liwenjie on 15-3-17.
//  Copyright (c) 2015年 com.yunva.yaya. All rights reserved.
//

//加入群通知
//#define IM_GROUP_JOIN_NOTIFY    0x13007
//namespace x13007 {
//    enum {
//        /*uint32*/ groupid   = 1, //群ID
//        /*uint32*/ userid    = 2, //用户ID
//        /*string*/ username  = 3, //用户名
//        /*string*/ groupname = 4, //群名
//        /*string*/ greet     = 5, //问候语
//        /*string*/ iconurl   = 6, //图像地址
//    };
//}



#import "YvBackBase.h"

@interface YvIMGroupJoinNotify : YvBackBase

@property(assign, nonatomic) UInt32 groupid;//群ID
@property(assign, nonatomic) UInt32 userid;//用户ID
@property(strong, nonatomic) NSString * username;//用户名
@property(strong, nonatomic) NSString * groupname;//群名
@property(strong, nonatomic) NSString * greet;//问候语
@property(strong, nonatomic) NSString * iconurl;//图像地址

@property(assign, nonatomic) NSInteger sendtime;  //记录消息来的时间  mark by czw 9.17
@property(assign, nonatomic) NSInteger     category;//0.好友1.申请入群2.群主邀请入群3.其他通知
@property(assign, nonatomic) NSInteger     userstatus;//0.拒绝（显示“已拒绝”）1.同意（显示“已同意”）2.为处理（显示拒绝和同意按钮）3.其他通知（不显示按钮）
@end
