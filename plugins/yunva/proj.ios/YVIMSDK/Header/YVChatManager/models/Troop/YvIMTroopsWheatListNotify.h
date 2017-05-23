//
//  YvIMTroopsWheatListNotify.h
//  YvIMSDKAPITestDemo
//
//  Created by liwenjie on 15-3-25.
//  Copyright (c) 2015年 com.yunva.yaya. All rights reserved.
//
//#define CMDNO_TTROOPS_WHEAT_LIST_NOTIFY			  0x17025
//namespace troops_17025 {
//    enum {
//        /*uint16*/	wheat_time		= 1,	//列表第一个的麦序时间(终端可以通过这个时间效对)
//        /*uint32[]*/wheat_list		= 2,	//麦序列表成员 userId
//        /*uint8*/	control_wheat	= 3,	//控卖
//        /*uint8*/	disable_wheat	= 4,	//禁麦
//        /*array*/	xWheatInfo		= 5,	//麦序用户信息对像
//        /*object*/	xWheatOper		= 6,	//变动原
//    };
//}
#import "YvBackBase.h"
#import "YvXWheatInfo.h"

@interface YvIMTroopsWheatListNotify : YvBackBase
#ifdef czw
@property(assign, nonatomic) UInt16             wheat_time;//列表第一个的麦序时间(终端可以通过这个时间效对)
@property(strong, nonatomic) NSMutableArray *   wheat_list;//麦序列表成员 userId
@property(assign, nonatomic) UInt8              control_wheat;//控麦
@property(assign, nonatomic) UInt8              disable_wheat;//禁麦
@property(strong, nonatomic) NSMutableArray *   xWheatInfo;//麦序用户信息对像
@property(strong, nonatomic) id                 xWheatOper;//变动原

#endif


@end
