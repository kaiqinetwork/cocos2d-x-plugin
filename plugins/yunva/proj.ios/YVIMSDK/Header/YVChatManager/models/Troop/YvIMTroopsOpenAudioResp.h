//
//  YvIMTroopsOpenAudioResp.h
//  YvIMSDKAPITestDemo
//
//  Created by liwenjie on 15-3-25.
//  Copyright (c) 2015年 com.yunva.yaya. All rights reserved.
//
//#define CMDNO_TROOPS_OPEN_AUDIO_RESP						0x17039
//namespace troops_17039 {
//    enum {
//        /*uint32*/		result       = 1,
//        /*string*/		msg		     = 2,
//    };
//}
#import "YvBackBase.h"

@interface YvIMTroopsOpenAudioResp : YvBackBase
#ifdef czw


@property(assign, nonatomic) UInt32     result;
@property(strong, nonatomic) NSString * msg;
#endif

@end
