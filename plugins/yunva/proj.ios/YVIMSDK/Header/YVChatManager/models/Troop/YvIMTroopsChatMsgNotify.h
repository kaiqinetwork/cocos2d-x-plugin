//
//  YvIMTroopsChatMsgNotify.h
//  YvIMSDKAPITestDemo
//
//  Created by liwenjie on 15-3-25.
//  Copyright (c) 2015年 com.yunva.yaya. All rights reserved.
//
//聊天信息通知
//#define CMDNO_TROOPS_CHATMSG_NOTIFY          0x17014
//namespace troops_17014
//{
//    enum
//    {
//        /*uint8*/       type        = 1,       //聊天类型（chat_type）
//        /*uint32*/      userId      = 2,       //用户ID
//        /*string*/      nickname    = 3,       //用户昵称
//        /*string*/      text        = 4,       //语音，图片聊天是为URL， 图片聊天时为  缩略图|原图
//        /*uint32*/      times       = 5,       //录音时长(秒)
//        /*string*/      ext1        = 6,       //扩展字段
//    };
//}

#import "YvBackBase.h"

@interface YvIMTroopsChatMsgNotify : YvBackBase
#ifdef czw



@property(assign, nonatomic) UInt8      type;//聊天类型（chat_type）
@property(assign, nonatomic) UInt32     userId;//用户ID
@property(strong, nonatomic) NSString * nickname;//用户昵称
@property(strong, nonatomic) NSString * text;//语音，图片聊天是为URL， 图片聊天时为  缩略图|原图
@property(assign, nonatomic) UInt32     times;//录音时长(秒)
@property(assign, nonatomic) NSString * ext1;//扩展字段
#endif
@end
