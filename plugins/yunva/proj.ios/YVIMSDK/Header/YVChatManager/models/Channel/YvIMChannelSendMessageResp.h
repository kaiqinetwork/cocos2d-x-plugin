//
//  YvIMChannelSendMessageResp.h
//  YvIMSDKAPITestDemo
//
//  Created by liwenjie on 15-5-20.
//  Copyright (c) 2015年 com.yunva.yaya. All rights reserved.
//

#import "YvBackBase.h"
@class YvXHistoryMsgInfo;
@interface YvIMChannelSendMessageResp : YvBackBase
#ifdef czw
@property(assign, nonatomic) UInt32         result;
@property(strong, nonatomic) NSString *     msg;
@property(assign, nonatomic) UInt8          type;//type= 1 语音  type= 2 文本
@property(strong, nonatomic) NSString *     wildCard;//游戏通道字符串
@property(strong, nonatomic) NSString *     textMsg;//文字消息
@property(strong, nonatomic) NSString *     url;//语音URL
@property(assign, nonatomic) UInt32         voiceDurationTime;//语音消息时长
@property(strong, nonatomic) NSString *     expand;//透传字段
@property(assign, nonatomic) UInt8          channel;//游戏通道
@property(assign, nonatomic) UInt8          shield;//是否有敏感字， 1：存在，0不存在

//- (id)initWithXHistoryMsgInfo:(YvXHistoryMsgInfo *)xHistoryMsgInfo;
#endif
@end
