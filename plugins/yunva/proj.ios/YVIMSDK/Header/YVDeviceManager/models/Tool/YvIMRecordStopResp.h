//
//  YvIMRecordStopResp.h
//  YvIMSDKAPI
//  停止录音返回  回调返回录音文件路径名
//  Created by liwenjie on 15-3-9.
//  Copyright (c) 2015年 com.yunva.yaya. All rights reserved.
//

//#define	IM_RECORD_STOP_RESP		0x19002

#import "YvBackBase.h"

@interface YvIMRecordStopResp : YvBackBase

@property(assign, nonatomic) UInt32         time;//录音时长
@property(strong, nonatomic) NSString *     strfilepath;//录音保存文件路径名
@property(strong, nonatomic) NSString *     ext;//扩展标记

@end
