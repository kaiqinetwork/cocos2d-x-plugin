//
//  YvIMTroopsSpeakListNotify.h
//  YvIMSDKAPITestDemo
//
//  Created by liwenjie on 15-3-25.
//  Copyright (c) 2015年 com.yunva.yaya. All rights reserved.
//
//说话人列表通知
//#define CMDNO_TROOPS_SPEAK_LIST_NOTIFY					 0x17040
//namespace troops_17040{
//    enum {
//        /*uint8*/		tag = 1,						//保留
//        /*uint32[]*/	speakId = 2,					//返回当然说话人列表
//    };
//}
#import "YvBackBase.h"

@interface YvIMTroopsSpeakListNotify : YvBackBase
#ifdef czw
@property(assign, nonatomic) UInt8              t_tag;//保留
@property(strong, nonatomic) NSMutableArray *   speakId;//返回当然说话人列表

#endif


@end
