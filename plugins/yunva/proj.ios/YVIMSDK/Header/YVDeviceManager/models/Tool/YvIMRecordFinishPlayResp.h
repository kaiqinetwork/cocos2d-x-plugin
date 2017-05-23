//
//  YvIMRecordFinishPlayResp.h
//  YvIMSDKAPI
//  播放语音完成
//  Created by liwenjie on 15-3-9.
//  Copyright (c) 2015年 com.yunva.yaya. All rights reserved.
//

//#define	IM_RECORD_FINISHPLAY_RESP	  0x19004

#import "YvBackBase.h"

@interface YvIMRecordFinishPlayResp : YvBackBase

@property(assign, nonatomic) UInt32         result;//播放完成为 0， 失败为1
@property(strong, nonatomic) NSString *     describe;//描述
@property(strong, nonatomic) NSString *     ext;//扩展标记

@end
