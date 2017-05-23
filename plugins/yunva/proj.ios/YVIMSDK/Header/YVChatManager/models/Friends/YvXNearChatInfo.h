//
//  YvXNearChatInfo.h
//  YvIMSDKAPI
//  最近联系人信息结构体
//  Created by liwenjie on 15-3-9.
//  Copyright (c) 2015年 com.yunva.yaya. All rights reserved.
//

#import <Foundation/Foundation.h>
@class YvIMCloudMSGNotify;
@interface YvXNearChatInfo : NSObject

@property(strong, nonatomic) NSString * nickname; //用户昵称
@property(assign, nonatomic) UInt32     userid; //用户ID
@property(strong, nonatomic) NSString * iconurl; //用户图像地址
@property(assign, nonatomic) UInt8      online; //是否在线
@property(strong, nonatomic) NSString * userlevel; //用户等级
@property(strong, nonatomic) NSString * viplevel; //vip等级
@property(strong, nonatomic) NSString * ext; //扩展字段
@property(assign, nonatomic) UInt8      shieldmsg; //是否屏蔽聊天消息
@property(assign, nonatomic) UInt8      sex; //性别
@property(strong, nonatomic) NSString * group; //所在组名称
@property(strong, nonatomic) NSString * remark; //备注
@property(assign, nonatomic) UInt32     times; //最近聊天时间,单位（秒）

//为了配合未读消息通知，加上一个附带的结构体子字段
@property(assign, nonatomic) UInt32     endid;//结束索引
@property(assign, nonatomic) UInt32     unread;//未读条数
@property(assign, nonatomic) UInt8      type;//消息类型 e_chat_msgtype
@property(strong, nonatomic) NSString * data;//若为文本类型，则是消息内容，其他则是文件地址
@property(assign, nonatomic) UInt32     audiotime;//若为音频文件, 则为文件播放时长(秒)

- (id)initWithCloudMsgNotify:(YvIMCloudMSGNotify *)cloudMsgNotify;

@end
