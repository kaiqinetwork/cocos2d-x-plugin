//
//  YVChatManagerGroupDelegate.h
//  YvImSDKOCWapper
//
//  Created by dada on 15/9/29.
//  Copyright (c) 2015年 yunva. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "YVChatManageDelegateBase.h"

@protocol YVChatManagerGroupDelegate <YVChatManageDelegateBase>
@optional
//群用户列表推送通知
- (void)onIMGroupUserListNotify:(YvIMGroupUserListNotify *)groupUserListNotify;//
//群用户修改资料通知通知
- (void)onIMGroupUserMDYNotify:(YvIMGroupUserMDYNotify *)groupUserMDYNotify;
//修改群属性回应
- (void)onIMGroupModifyResp:(YvIMGroupModifyResp *)groupModifyResp;
//转移群主回应
- (void)onIMGroupShiftToWnerResp:(YvIMGroupShiftToWnerResp *)groupShiftToWnerResp;//
//转移群主通知
- (void)onIMGroupShiftToWnerNotify:(YvIMGroupShiftToWnerNotify *)groupShiftToWnerNotify;//
//加入群通知
- (void)onIMGroupJoinNotify:(YvIMGroupJoinNotify *)groupJoinNotify;//
//加入群回应
- (void)onIMGroupJoinResp:(YvIMGroupJoinResp *)groupJoinResp;//
//群主同意或拒绝申请者加群
- (void)onimgroupAcceptOrRefuseNotify:(YvIMGroupAcceptOrRefuseNotify*)groupAcceptOrRefuse;
//退出群回应
- (void)onIMGroupExitResp:(YvIMGroupExitResp *)groupExitResp;//
//退出群通知
- (void)onIMGroupExitNotify:(YvIMGroupExitNotify *)groupExitNotify;//
//创建群回应
- (void)onIMGroupCreateResp:(YvIMGroupCreateResp *)groupCreateResp;//
//解散群回应
- (void)onIMGroupDissolveResp:(YvIMGroupDissolveResp *)groupDissolveResp;//
//搜索群回应
- (void)onIMGroupSearchResp:(YvIMGroupSearchResp *)groupSearchResp;//
//踢除群成员回应
- (void)onIMGroupKickResp:(YvIMGroupKickResp *)groupKickResp;//
//踢除群成员通知
- (void)onIMGroupKickNotify:(YvIMGroupKickNotify *)groupKickNotify;//
//邀请好友入群回应
- (void)onIMGroupInviteResp:(YvIMGroupInviteResp *)groupInviteResp;
//邀请好友入群通知
- (void)onIMGroupInviteNotify:(YvIMGroupInviteNotify *)groupInviteNotify;//
//邀请好友入群回应
- (void)onIMGroupInviteRespon:(YvIMGroupInviteRespon*)GroupInviteRespon;
//被邀请者同意或拒绝群邀请响应
- (void)onIMGroupInviteAccesptResp:(YvIMGroupInviteAccesptResp*)MGroupInviteAccesptResp;
//设置群成员角色回应
- (void)onIMGroupSetRoleResp:(YvIMGroupSetRoleResp *)groupSetRoleResp;//
//设置群成员角色通知
- (void)onIMGroupSetRoleNotify:(YvIMGroupSetRoleNotify *)groupSetRoleNotify;//
//修改他人名片回应
- (void)onIMGroupSetOtherResp:(YvIMGroupSetOtherResp *)groupSetOtherResp;
//修改他人名片通知
- (void)onIMGroupSetOtherNotify:(YvIMGroupSetOtherNotify *)groupSetOtherNotify;
//群属性通知(群列表)
- (void)onIMGroupPropertyNotify:(YvIMGroupPropertyNotify *)groupPropertyNotify;//
//群成员上线通知
- (void)onIMGroupMemberOnlineNotify:(YvIMGroupMemberOnline *)groupMemberOnlineNotify;//
//新成员加入群通知
- (void)onIMGroupUserJoinNotify:(YvIMGroupUserJoinNotify *)groupUserJoinNotify;//
@end
