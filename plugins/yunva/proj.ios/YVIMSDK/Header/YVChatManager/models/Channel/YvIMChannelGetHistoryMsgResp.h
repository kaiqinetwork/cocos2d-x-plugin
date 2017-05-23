//
//  YvIMChannelGetHistoryMsgResp.h
//  YvIMSDKAPITestDemo
//
//  Created by liwenjie on 15-3-15.
//  Copyright (c) 2015年 com.yunva.yaya. All rights reserved.
//
//频道获取历史消息返回
//#define IM_CHANNEL_HISTORY_MSG_RESP   0x16006

#import "YvBackBase.h"


@interface YvIMChannelGetHistoryMsgResp : YvBackBase
#ifdef czw
@property(strong, nonatomic) NSMutableArray * xHistoryMsg;//历史消息对象list

#endif
@end
