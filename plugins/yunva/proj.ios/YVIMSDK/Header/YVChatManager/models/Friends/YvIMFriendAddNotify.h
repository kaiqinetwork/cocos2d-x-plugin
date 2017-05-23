//
//  YvIMFriendAddNotify.h
//  YvIMSDKAPI
//  好友请求通知
//  Created by liwenjie on 15-3-9.
//  Copyright (c) 2015年 com.yunva.yaya. All rights reserved.
//

//#define IM_FRIEND_ADD_NOTIFY    0x12002

#import "YvBackBase.h"

@interface YvIMFriendAddNotify : YvBackBase

@property(assign, nonatomic) UInt32     userid; //用户ID
@property(strong, nonatomic) NSString * name; //用户名称
@property(strong, nonatomic) NSString * greet; //问候语
@property(strong, nonatomic) NSString * sign; //签名
@property(strong, nonatomic) NSString * url; //头像地址



@property(assign, nonatomic) NSInteger sendtime;  //记录消息来的时间  mark by czw 9.17
@property(assign, nonatomic) NSInteger     category;//0.好友1.申请入群2.群主邀请入群3.其他通知
@property(assign, nonatomic) NSInteger     userstatus;//0.拒绝（显示“已拒绝”）1.同意（显示“已同意”）2.为处理（显示拒绝和同意按钮）3.其他通知（不显示按钮）
@end
