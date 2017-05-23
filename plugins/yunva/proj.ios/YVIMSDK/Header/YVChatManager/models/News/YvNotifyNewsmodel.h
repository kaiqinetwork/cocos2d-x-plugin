//
//  YvNotifyNewsmodel.h
//  YvIMSDKDemo
//
//  Created by mac on 15-9-17.
//  Copyright (c) 2015年 yunva. All rights reserved.
//

#import "YvBackBase.h"

@interface YvNotifyNewsmodel : YvBackBase
@property(strong, nonatomic) NSString * icon;//
@property(strong, nonatomic) NSString * username;//
@property(strong, nonatomic) NSString * groupname;//
@property(strong, nonatomic) NSString * detail;//
@property(assign, nonatomic) NSInteger     category;//0.好友1.申请入群2.群主邀请入群3.其他通知
@property(assign, nonatomic) NSInteger     userstatus;//0.拒绝（显示“已拒绝”）1.同意（显示“已同意”）2.为处理（显示拒绝和同意按钮）3.其他通知（不显示按钮）
@property(assign, nonatomic) UInt32     userid;//
@property(assign, nonatomic) UInt32     groupid;//
@property(assign, nonatomic) UInt32     cloudid;//
@property(strong, nonatomic) NSString * cloudsource;//
@property(assign, nonatomic) NSInteger  sendtime;//

@end
