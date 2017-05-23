//
//  YvChatManagerFriendDelegate.h
//  YvIMSDKManage
//
//  Created by liwenjie on 14/12/24.
//  Copyright (c) 2014年 com.yunva.yaya. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "YVChatManageDelegateBase.h"

#import "YvIMCMDDefHeaders.h"
@protocol YVChatManagerFriendDelegate <YVChatManageDelegateBase>
@optional
//添加好友回应，对方接受/拒绝
- (void)onIMFriendAddResp:(YvIMFriendAddResp *)addResp;
//添加新好友后，服务器返回请求成功/失败
- (void)onIMFriendToSelfAddResp:(YvIMFriendAddToSelfResp *)addResp;
//好友请求通知
- (void)onIMFriendAddNotify:(YvIMFriendAddNotify *)addNotify;
//同意添加好友返回
- (void)onIMFriendAcceptResp:(YvIMFriendAcceptResp *)acceptResp;
//删除好友返回
- (void)onIMFriendDelResp:(YvIMFriendDelResp *)delResp;
//删除好友通知
- (void)onIMFriendDelNotify:(YvIMFriendDelNotify *)delNotify;
//搜索好友返回
- (void)onIMFriendSearchResp:(YvIMFriendSearchResp *)searchResp;
//推荐好友返回
- (void)onIMFriendRecommandResp:(YvIMFriendRecommandResp *)recommandResp;
//好友操作返回(黑名单)
- (void)onIMFriendOperResp:(YvIMFriendOperResp *)operResp;
//好友列表推送
- (void)onIMFriendListNotify:(YvIMFriendListNotify *)friendListNotify;
//黑名单列表推送
- (void)onIMFriendBlackListNotify:(YvIMFriendBlackListNotify *)blackListNotify;
//最近联系人推送
- (void)onIMFriendNearListNotify:(YvIMFriendNearListNotify *)nearListNotify;
//好友状态推送
- (void)onIMFriendStatusNotify:(YvIMFriendStatusNotify *)statusNotify;
//设置好友信息返回
- (void)onIMFriendInfoSetResp:(YvIMFriendInfoSetResp *)infoSetResp;
//获取个人信息返回
- (void)onIMUserGetInfoResp:(YvIMUserGetInfoResp *)userGetInfoResp;
//获取好友列表返回
- (void)onIMfriendListResp:(YvIMFriendListResp *)friendListResp;
//获取黑名单列表返回
- (void)onIMFriendBlackListResp:(YvIMFriendBlackListResp *)friendBlackListResp;
//获取最近联系人列表返回
- (void)onIMFriendNearListResp:(YvIMFriendNearListResp *)friendNearListResp;
@end
