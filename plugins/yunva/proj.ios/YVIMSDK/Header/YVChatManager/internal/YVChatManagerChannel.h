//
//  YVChatManagerChannel.h
//  YvImSDKOCWapper
//
//  Created by dada on 15/10/9.
//  Copyright (c) 2015年 yunva. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "YVChatManagerBase.h"
@protocol YVChatManagerChannel <YVChatManagerBase>
#ifdef czw

//登录频道
- (void)loginChannel:(NSString *)wildCard pgameServiceID:(NSString *)pgameServiceID channelId:(int)channelId;

//退出频道
- (void)logoutChannel;

//获取频道信息
- (void)getChannelInfo;

//获取频道历史消息
- (void)channelHistoryMsg:(UInt32)index count:(UInt32)count wildCard:(NSString *)wildCard;

//频道发送文本信息
- (void)channelTextMsg:(NSString *)textMsg wildCard:(NSString *)wildCard expand:(NSString *)expand;

//频道发送语音信息
- (void)channelVoiceMsg:(NSString *)voiceFilePath voiceDurationTime:(UInt32)voiceDurationTime wildCard:(NSString *)wildCard expand:(NSString *)expand;

//频道发送语音识别信息
- (void)channelVoiceMsg:(NSString *)voiceFilePath voiceDurationTime:(UInt32)voiceDurationTime text:(NSString *)text wildCard:(NSString *)wildCard expand:(NSString *)expand;

/*
 **修改通配符
 **@param operate //0：移除，1：添加
 **@param channel //通道（0-9）
 **@param wildCard //通配符
 */
-(void)setWildCard:(UInt8)operate channel:(UInt8)channel wildCard:(NSString*)wildCard;
#endif
@end
