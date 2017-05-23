//
//  YvChatManagerFriend.h
//  YvIMSDKManage
//
//  Created by liwenjie on 14/12/24.
//  Copyright (c) 2014年 com.yunva.yaya. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "YVChatManagerBase.h"

/*!
 @protocol
 @brief 本协议包括：登录、退出、等方法
 @discussion 需要先将要接受回调的对象注册到delegate中, 示例代码如下:
 [[[YvIMSDKManage sharedInstance] chatManager] addDelegate:self ]
 */
@protocol YVChatManagerFriend <YVChatManagerBase>



@required


#pragma mark -- 获取好友列表
- (NSMutableArray *)getFriendList;
#pragma mark -- 获取黑名单列表
- (NSMutableArray *)getBlackList;
#pragma mark -- 获取最近联系人列表（保留）
- (NSMutableArray *)getNearFriendList;
#pragma mark -- 获取待审核列表（保留）
- (NSMutableArray *)getAffirmList;
#pragma mark --获取申请好友列表
- (NSMutableArray *)getApplyFriendList;
#pragma mark - 获取黑名单除外的好友列表
- (NSMutableArray *)getFriendListWithoutBlackFriend;
#pragma mark - 根据userid获取对应好友YvXUserInfo（保留）
- (YvXUserInfo *)getFriendProperty:(UInt32)userid;

-(void)deleteFriendwithfriendid:(UInt32)userid;





#pragma mark -- 请求添加好友
- (void)addFriend:(UInt32)userid greet:(NSString *)greet;

#pragma mark -- 同意添加好友
- (void)addFriendAccept:(UInt32)userid affirm:(UInt8)affirm greet:(NSString *)greet;

#pragma mark -- 删除好友
- (void)deleteFriend:(UInt32)userid act:(UInt8)act;

#pragma mark -- 搜索好友
- (void)searchFriendWithKeyword:(NSString *)keyword start:(UInt32)start count:(UInt32)count;

#pragma mark -- 推荐好友 （保留）
- (void)recommandFriendWithStart:(UInt32)start count:(UInt32)count;

#pragma mark -- 好友操作请求(黑名单)  //将某个好友拉进黑名单
- (void)operFriendWithUserId:(UInt32)userid operId:(UInt32)operId act:(UInt8)act;

#pragma mark -- 设置好友信息（保留）
- (void)setFriendInfoWithUserId:(UInt32)friendId group:(NSString *)group note:(NSString *)note;

#pragma mark - 获取用户信息（保留）
- (void)getUserInfo:(UInt32)userid;

#pragma mark - 获取好友列表（保留）
- (void)getFriendListWithGroup:(NSString *)group;

#pragma mark - 获取黑名单列表
- (void)getFriendBlackList;

#pragma mark -获取最近联系人（保留）
- (void)getNearListFriendList;

@end

