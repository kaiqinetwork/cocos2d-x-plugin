//
//  YvIMRecordPlayPercentNotify.h
//  YvIMSDKAPITestDemo
//
//  Created by whe on 15/5/21.
//  Copyright (c) 2015年 com.yunva.yaya. All rights reserved.
//


// //播放URL下载进度
// #define	IM_RECORD_PLAY_PERCENT_NOTIFY	     0x19016
// namespace x19016{
//	enum{
// /*uint8*/      percent     = 1, //播放URL，下载进度百分比
///*string*/     ext         = 2, //扩展标记
//
//};
//}

#import "YvBackBase.h"

@interface YvIMRecordPlayPercentNotify : YvBackBase
@property(assign,nonatomic) UInt8 percent;
@property(strong,nonatomic) NSString *ext;
@end
