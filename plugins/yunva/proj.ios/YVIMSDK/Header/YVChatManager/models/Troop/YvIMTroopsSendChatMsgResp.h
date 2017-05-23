//
//  YvIMTroopsSendChatMsgResp.h
//  YvIMSDKAPITestDemo
//
//  Created by liwenjie on 15-3-25.
//  Copyright (c) 2015年 com.yunva.yaya. All rights reserved.
//

//发送聊天消息响应
//#define CMDNO_TROOPS_SENDCHATMSG_RESP          0x17013
//namespace troops_17013
//{
//    enum
//    {
//        /*uint32*/		result      = 1,
//        /*string*/		msg		    = 2,
//        /*uint8*/       type        = 3,       //聊天类型
//    };
//}

#import "YvBackBase.h"

@interface YvIMTroopsSendChatMsgResp : YvBackBase
#ifdef czw



@property(assign, nonatomic) UInt32     result;
@property(strong, nonatomic) NSString * msg;
@property(assign, nonatomic) UInt8      type;//聊天类型
#endif
@end
