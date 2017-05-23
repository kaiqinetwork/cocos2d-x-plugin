//
//  YvIMChatFriendNotify.h
//  YvIMSDKAPITestDemo
//
//  Created by liwenjie on 15-3-10.
//  Copyright (c) 2015年 com.yunva.yaya. All rights reserved.
//

//#define IM_CHAT_FRIEND_NOTIFY 0x14003
//#define /*uint32*/	  CLOUDMSG_ID				110				//云消息ID
//#define /*string*/	  CLOUDMSG_SOURCE			111				//云标示符 {来源ID SYSTEM系统消息  PUSH 推送消息}

#import "YvBackBase.h"

@interface YvIMChatFriendNotify : YvBackBase

@property(assign, nonatomic) UInt32       userid;//好友ID
@property(strong, nonatomic) NSString *   name;//好友名称
@property(strong, nonatomic) NSString *   signature;//好友签名
@property(strong, nonatomic) NSString *   headicon;//图像地址
@property(assign, nonatomic) UInt32       sendtime;//发送时间
@property(assign, nonatomic) UInt8        type;//类型 e_chat_msgtype
@property(strong, nonatomic) NSString *   data;//若为文本类型，则是消息内容，其他则是文件地址,图像地址格式为(xxx|xxx)
@property(strong, nonatomic) NSString *   imageurl; //若为音频，则是小图像地址
@property(assign, nonatomic) UInt32       audiotime;//若为音频文件, 则为文件播放时长(秒)
@property(strong, nonatomic) NSString *   attach;//若为音频文件，则是附加文本(没有附带文本时为空)
@property(strong, nonatomic) NSString *   ext1;//扩展字段
@property(assign, nonatomic) UInt8        shield;//是否有敏感字， 1：存在，0不存在
@end
