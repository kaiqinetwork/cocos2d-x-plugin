//
//  YVChatManagerGroup.h
//  YvImSDKOCWapper
//
//  Created by dada on 15/9/29.
//  Copyright (c) 2015年 yunva. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "YVChatManagerBase.h"
@protocol YVChatManagerGroup <YVChatManagerBase>

@required
#pragma mark -- 获取群列表
- (NSMutableArray *)getGroupList;
#pragma mark -- 退出登入删除 群列表
- (void)deleteGroupList;
#pragma mark -- 获取群属性
- (YvIMGroupPropertyNotify *)getGroupProperty:(UInt32)groupid;
#pragma mark -- 获取群用户列表
- (NSMutableArray *)getGroupUserList:(UInt32)groupid;
#pragma mark -- 删除群成员
- (void)deleteGroupUser:(UInt32)groupid deleteUserid:(UInt32)delUserid;
#pragma mark -- 获取群邀请列表
- (NSMutableArray *)getGroupInviteList;
#pragma mark -- 获取待审核的成员列表
- (NSMutableArray *)getGroupJoinList;
#pragma mark -- 更新待审核的成员列表（保留）
- (void)deleteGroupJoinListWithGroupId:(UInt32)groupid userid:(UInt32)userid;
#pragma mark -- 更新群邀请通知列表（保留）
- (void)deleteGroupInviteListWithGroupId:(UInt32)groupid userid:(UInt32)userid;
#pragma mark -- 更新群的未读消息个数（保留）
- (void)updateUnReadNumWithGroupId:(UInt32)groupid unReadNum:(UInt32)unReadNum;
#pragma mark -- 添加群成员到群成员列表 创建群的时候用到
- (void)addGroupMemberIntoGroupListwith:(YvIMGroupPropertyNotify *)propertyNotify;




#pragma mark -- 创建群
- (void)groupCreateWithVerify:(UInt8)verify name:(NSString *)name iconUrl:(NSString *)iconUrl;//

#pragma mark -- 搜索群
- (void)groupSearchReq:(UInt32)goupid;//

#pragma mark -- 加入群
- (void)groupJoinReq:(UInt32)groupid greet:(NSString *)greet;//

#pragma mark -- 同意拒绝加群
- (void)groupJoinAccept:(UInt32)groupid userid:(UInt32)userid agree:(UInt8)agree greet:(NSString *)greet;

#pragma mark -- 退群
- (void)groupExitReq:(UInt32)groupid;//

#pragma mark -- 修改群属性
- (void)groupModifyWithGroupid:(UInt32)groupid name:(NSString *)name
                          icon:(NSString *)icon announcement:(NSString *)announcement
                        verify:(UInt8)verify msg_set:(UInt8)msg_set alias:(NSString *)alias;//?？

#pragma mark -- 转移群主（保留）
- (void)groupShiftToWnerWithGroupId:(UInt32)groupid userid:(UInt32)userid;//

#pragma mark -- 踢除群成员
- (void)groupKickReq:(UInt32)groupid userid:(UInt32)userid;//

#pragma mark -- 邀请好友入群
- (void)groupInviteReq:(UInt32)groupid userid:(UInt32)userid greet:(NSString *)greet;//

#pragma mark -- 被邀请者同意或拒绝群邀请
- (void)groupInviteAcceptWithInviteid:(UInt32)inviteid agree:(UInt32)agree greet:(NSString *)greet groupid:(UInt32)groupid invitename:(NSString*)invitename;

#pragma mark -- 设置群成员角色请求（保留）
- (void)groupSetRoleReq:(UInt32)groupid userid:(UInt32)uerid role:(UInt8)role;//

#pragma mark -- 解散群
- (void)groupDissolveReq:(UInt32)groupid;//

#pragma mark -- 管理员修改他人名片（保留）
- (void)groupSetOtherReq:(UInt32)groupid userid:(UInt32)userid alias:(NSString *)alias;//
@end
