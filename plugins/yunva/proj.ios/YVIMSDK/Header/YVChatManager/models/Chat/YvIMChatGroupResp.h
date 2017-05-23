//
//  YvIMChatGroupResp.h
//  YvIMSDKAPITestDemo
//
//  Created by liwenjie on 15-3-17.
//  Copyright (c) 2015年 com.yunva.yaya. All rights reserved.
//

//群聊消息发送响应
//#define IM_CHAT_GROUPMSG_RESP  0x14010
//namespace x14010 {
//    enum {
///*uint32*/ result       = 1,
///*string*/ msg          = 2,
///*uint32*/ groupid      = 3, //群ID
///*uint32*/ index        = 4, //消息序号
//  /*string*/ flag			= 5, //消息标记
//    };
//}

//@property(assign, nonatomic) UInt32     groupid;//群ID
//@property(assign, nonatomic) UInt32     userid; //用户ID
//@property(strong, nonatomic) NSString * session;//会话记录ID
#import "YvBackBase.h"

@interface YvIMChatGroupResp : YvBackBase

@property(assign, nonatomic) UInt32     result;//
@property(strong, nonatomic) NSString * msg;//
@property(assign, nonatomic) UInt32     groupid;//群ID
@property(assign, nonatomic) UInt32     index;////消息序号
@property(strong, nonatomic) NSString * flag;////消息标记

@end
