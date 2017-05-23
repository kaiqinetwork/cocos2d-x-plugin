//
//  YvXP2PChatMsg.h
//  YvIMSDKAPITestDemo
//
//  Created by liwenjie on 15-3-11.
//  Copyright (c) 2015年 com.yunva.yaya. All rights reserved.
//

//好友离线结构
//namespace xP2PChatMsg
//{
//    enum
//    {
//        /*uint32*/ userid    = 1, //好友ID
//        /*string*/ name      = 2, //好友名称
//        /*string*/ signature = 3, //好友签名
//        /*string*/ headurl   = 4,  //头像地址
//        /*uint32*/ sendtime  = 5, //发送时间
//        /*uint8*/  type      = 6, //类型 e_chat_msgtype
//        /*string*/ data      = 7, //若为文本类型，则是消息内容，若为音频，则是文件url，若为图像，则是大图像地址
//        /*string*/ imageurl  = 8, //若为图片，则是小图像地址
//        /*uint32*/ audiotime = 9, //若为音频文件, 则是文件播放时长(秒)
//        /*string*/ attach    = 10, //若为音频文件，则是附加文本(没有附带文本时为空)
//        /*string*/ ext1      = 11, //扩展字段
//    };
//}

#import <Foundation/Foundation.h>
@class YvIMChatFriendNotify;
@interface YvXP2PChatMsg : NSObject

@property(assign, nonatomic) UInt32     userid;//用户ID
@property(strong, nonatomic) NSString * name;//用户名称
@property(strong, nonatomic) NSString * signature;//用户签名
@property(strong, nonatomic) NSString * headicon;//图像地址
@property(assign, nonatomic) UInt32     sendtime;//消息发送时间
@property(assign, nonatomic) UInt8      type;//消息类型 e_chat_msgtype
@property(strong, nonatomic) NSString * data;//若为文本类型，则是消息内容，其他则是文件地址
@property(strong, nonatomic) NSString * imageurl; //若为图片，则是小图像地址
@property(assign, nonatomic) UInt32     audiotime;//若为音频文件, 则为文件播放时长(秒)
@property(strong, nonatomic) NSString * attach;//若为音频文件，则是附加文本(没有附带文本时为空)
@property(strong, nonatomic) NSString * ext1;//扩展字段

- (id)initWithIMChatFriendNotify:(YvIMChatFriendNotify *)chatFriendNotify;

@end
