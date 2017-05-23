//
//  YvIMSpeechStopResp.h
//  YvIMSDKAPI
//  停止语音识别返回
//  Created by liwenjie on 15-3-9.
//  Copyright (c) 2015年 com.yunva.yaya. All rights reserved.
//

//#define IM_SPEECH_STOP_RESP				0x19009

#import "YvBackBase.h"
@interface YvIMSpeechStopResp : YvBackBase

@property(assign, nonatomic) uint               err_id;//0为成功,其它为失败
@property(strong, nonatomic) NSString *         err_msg;//返回的错误描述
@property(strong, nonatomic) NSString *         result;//结果
@property(strong, nonatomic) NSString *         ext;//扩展标记
@property(strong, nonatomic) NSString *         url;//识别URL

@end
