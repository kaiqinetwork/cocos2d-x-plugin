//
//  YvIMFriendAddResp.h
//  YvIMSDKAPI
//  添加好友回应，对方接受/拒绝
//  Created by liwenjie on 15-3-9.
//  Copyright (c) 2015年 com.yunva.yaya. All rights reserved.
//

//enum e_addfriend_affirm{
//    af_refuse    = 0, //拒绝
//    af_agree     = 1, //同意加好友(单项)
//    af_agree_add = 2, //同意加好友并加对方为好友(双向)
//};
//
//#define IM_FRIEND_ADD_RESP      0x12001
//添加好友回应，对方接受/拒绝
#import "YvBackBase.h"

@interface YvIMFriendAddResp : YvBackBase

@property(assign, nonatomic) UInt32     affirm; //返回结果 e_addfriend_affirm
@property(assign, nonatomic) UInt32     userid; //用户ID
@property(strong, nonatomic) NSString * name; //用户名称
@property(strong, nonatomic) NSString * url; //头像地址
@property(strong, nonatomic) NSString * greet; //问候语

@end
