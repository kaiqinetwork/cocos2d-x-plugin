//
//  YvXGroupChatMsg.h
//  YvIMSDKAPITestDemo
//
//  Created by liwenjie on 15-3-11.
//  Copyright (c) 2015年 com.yunva.yaya. All rights reserved.
//

//群离线结构
//namespace xGroupChatMsg
//{
//    enum {
//        /*uint32*/ groupid   = 1, //群ID
//        /*uint32*/ sendid    = 2, //发送者ID
//        /*uint32*/ time      = 3, //发送时间
//        /*string*/ groupicon = 4, //群头像地址
//        /*string*/ groupname = 5, //群名称
//        /*uint8*/  type      = 6, //消息类型 e_chat_msgtype
//        /*string*/ data      = 7, //若为文本类型，则是消息内容，若为音频，则是文件地址，若为图像，则是大图像地址
//        /*string*/ imageurl  = 8, //若为图片，则是小图像地址
//        /*uint32*/ audiotime = 9, //若为音频文件, 则为文件播放时长(秒)
//        /*string*/ attach    = 10, //若为音频文件，则为附加文本(没有附带文本时为空)
//        /*string*/ ext1      = 11, //扩展字段
//        /*string*/ sendnickname   = 12, //发送者呢称
//        /*string*/ sendheadurl    = 13, //发送者头像
//    };
//}

#import <Foundation/Foundation.h>
@class YvIMChatGroupNotify;
@interface YvXGroupChatMsg : NSObject

@property(assign, nonatomic) UInt32     groupid;//群ID
@property(assign, nonatomic) UInt32     sendid;//发送者ID
@property(assign, nonatomic) UInt32     time;//消息发送时间
@property(strong, nonatomic) NSString * groupicon;//群图像地址
@property(strong, nonatomic) NSString * groupname;//群名称
@property(assign, nonatomic) UInt8      type;//消息类型 e_chat_msgtype
@property(strong, nonatomic) NSString * data;//若为文本类型，则是消息内容，其他则是文件地址
@property(strong, nonatomic) NSString * imageurl;//若为图片，则是小图像地址
@property(assign, nonatomic) UInt32     audiotime;//若为音频文件, 则为文件播放时长(秒)
@property(strong, nonatomic) NSString * attach;//若为音频文件，则为附加文本(没有附带文本时为空)
@property(strong, nonatomic) NSString * ext1;//扩展字段
@property(strong, nonatomic) NSString * sendnickname;//发送者呢称
@property(strong, nonatomic) NSString * sendheadurl;//发送者头像



- (id)initWithGroupMsgNotify:(YvIMChatGroupNotify*)groupMsgNotify;

@end
