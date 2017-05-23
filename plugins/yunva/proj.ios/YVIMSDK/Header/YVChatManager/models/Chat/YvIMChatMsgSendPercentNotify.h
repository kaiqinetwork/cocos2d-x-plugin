//
//  YvIMChatMsgSendPercentNotify.h
//  YvImSDKOCWapper
//
//  Created by dada on 15/10/24.
//  Copyright (c) 2015年 yunva. All rights reserved.
//

////发送进度（目前仅支持图片消息）
//#define IM_CHAT_MSG_SEND_PERCENT_NOTIFY 0x14011
//namespace x14011{
//    enum{
//        /*string*/ flag			= 1, //消息标记
//        /*uint32*/ percent      = 2, //百分比
//    };
//}
#import "YvBackBase.h"

@interface YvIMChatMsgSendPercentNotify : YvBackBase
@property(strong, nonatomic) NSString * flag;//消息标记
@property(assign, nonatomic) UInt32     percent;//百分比

@end
