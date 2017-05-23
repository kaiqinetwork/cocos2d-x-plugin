//
//  YvIMTroopsUserListNotify.h
//  YvIMSDKAPITestDemo
//
//  Created by liwenjie on 15-3-25.
//  Copyright (c) 2015年 com.yunva.yaya. All rights reserved.
//
//用户列表通知
//#define CMDNO_TROOPS_USERLIST_NOTIFY          0x17009
//namespace troops_17009
//{
//    enum
//    {
//        /*yvUserMember[]*/   xUserMember	= 1,	//房间成员
//    };
//}

#import "YvBackBase.h"
#import "YvXUserMember.h"

@interface YvIMTroopsUserListNotify : YvBackBase
#ifdef czw

@property(strong, nonatomic) NSMutableArray * xUserMember;

#endif

@end
