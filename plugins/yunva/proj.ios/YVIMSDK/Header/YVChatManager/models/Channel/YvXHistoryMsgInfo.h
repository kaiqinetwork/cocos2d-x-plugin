//
//  YvXHistoryMsgInfo.h
//  YvIMSDKAPITestDemo
//
//  Created by liwenjie on 15-3-15.
//  Copyright (c) 2015年 com.yunva.yaya. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface YvXHistoryMsgInfo : NSObject
#ifdef czw
@property(assign, nonatomic) UInt32         index;//消息索引
@property(strong, nonatomic) NSString *     ctime;//消息时间 2015-02 10:50:13
@property(assign, nonatomic) UInt32         user_id;//用户ID
@property(strong, nonatomic) NSString *     message_body;//消息内容
@property(strong, nonatomic) NSString *     nickname;//消息内容
@property(strong, nonatomic) NSData   *     ext1;//扩展1
@property(strong, nonatomic) NSData   *     ext2;//扩展2
@property(assign, nonatomic) UInt8          channel;//游戏通道
@property(strong, nonatomic) NSString *     wildcard;//游戏通道字符串
@property(assign, nonatomic) UInt32         message_type;//消息类型
@property(assign, nonatomic) UInt32         voiceDuration;//语音消息时长
@property(strong, nonatomic) NSString *     attach;//语音消息的附带文本(可选)
@property(assign, nonatomic) UInt8          shield;//是否有敏感字， 1：存在，0不存在

#endif
@end
