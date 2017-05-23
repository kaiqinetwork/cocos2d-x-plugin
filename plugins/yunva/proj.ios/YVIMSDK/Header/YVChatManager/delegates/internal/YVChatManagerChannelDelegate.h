//
//  YVChatManagerChannelDelegate.h
//  YvImSDKOCWapper
//
//  Created by dada on 15/10/9.
//  Copyright (c) 2015年 yunva. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "YVChatManageDelegateBase.h"
@protocol YVChatManagerChannelDelegate <YVChatManageDelegateBase>
#ifdef czw
//获取频道信息返回
- (void)onIMChannelGetInfoResp:(YvIMChannelGetInfoResp *)getInfoResp;
//获取频道历史信息返回
- (void)onIMChannelGetHistoryMsgResp:(YvIMChannelGetHistoryMsgResp *)getHistoryMsgResp;
//频道收到消息通知
- (void)onIMChannelMessageNotify:(YvIMChannelMessageNotify *)messageNotify;
//频道发送消息返回
- (void)onIMChannelSendMessageResp:(YvIMChannelSendMessageResp *)sendMsgResp;
//修改通配符返回
- (void)onIMChannelModifyResp:(YvIMChannelModifyResp *)modifyResp;
#endif
@end
