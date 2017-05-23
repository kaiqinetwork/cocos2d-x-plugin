/*!
 @header YVChatManager.h
 @abstract 此接口提供了聊天的基本操作
 @author yunva Inc.
 
 */

#import <Foundation/Foundation.h>
#import "YvChatManagerInitSdk.h" //初始化sdk，只有请求，没有对应的代理delegate；
#import "YVChatManagerLogin.h"
#import "YVChatManagerFriend.h"
#import "YVChatManagerGroup.h"
#import "YVChatManagerChat.h"
#import "YVChatManagerCloud.h"
//IM应用版demo 没用到
#import "YVChatManagerChannel.h"
#import "YVChatManagerTroops.h"

/*!
 @protocol
 @brief 登录、聊天、等协议的集合
 @discussion 可以通过YVIMSDKManage类获得此接口的实例, 示例代码如下:
                [[YVIMSDKManage sharedInstance] chatManager]
 */
#import "YVChatManageDelegate.h"
@protocol YVChatManager <
                        YvChatManagerInitSdk,
                        YVChatManagerLogin,
                        YVChatManagerFriend,
                        YVChatManagerGroup,
                        YVChatManagerChat,
                        YVChatManagerCloud,
                        YVChatManagerChannel,
                        YVChatManagerTroops
>
+(instancetype)shareInstance;

- (void)releaseInstance;
#pragma mark - YVChatManagerBase handle
/*!
 @method
 @brief 注册一个监听对象到监听列表中
 @discussion 把监听对象添加到监听列表中准备接收相应的事件
 @param delegate 需要注册的监听对象
 @result
 */
- (void)addDelegate:(id<YVChatManageDelegate>)delegate;

/*!
 @method
 @brief 从监听列表中移除一个监听对象
 @discussion 把监听对象从监听列表中移除,取消接收相应的事件
 @param delegate 需要移除的监听对象
 @result
 */
- (void)removeDelegate:(id<YVChatManageDelegate>)delegate;
@required

@end
