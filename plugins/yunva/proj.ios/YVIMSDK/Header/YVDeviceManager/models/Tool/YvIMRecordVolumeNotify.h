//
//  YvIMRecordVolumeNotify.h
//  YvIMSDKAPITestDemo_SimpleVersion
//
//  Created by liwenjie on 15-4-28.
//  Copyright (c) 2015年 com.yunva.yaya. All rights reserved.
//
//录音声音大小通知
//#define	IM_RECORD_VOLUME_NOTIFY	        0x19015
//namespace x19015{
//    enum{
//        /*string*/      ext         = 1,  //扩展标记
//        /*uint8*/       volume      = 2, //音量大小(0-100)
//    };
//}

#import "YvBackBase.h"

@interface YvIMRecordVolumeNotify : YvBackBase
@property(strong, nonatomic) NSString * ext;//扩展标记
@property(assign, nonatomic) UInt8     volume;//音量大小(0-100)
@end
