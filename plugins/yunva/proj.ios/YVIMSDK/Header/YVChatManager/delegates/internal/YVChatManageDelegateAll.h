//
//  YvChatManageDelegateAll.h
//  YvIMSDKManage
//
//  Created by liwenjie on 14/12/24.
//  Copyright (c) 2014年 com.yunva.yaya. All rights reserved.
//

#import <Foundation/Foundation.h>

#import "YVChatManagerLoginDelegate.h"
#import "YVChatManagerFriendDelegate.h"
#import "YVChatManagerGroupDelegate.h"
#import "YVChatManagerChatDelegate.h"
#import "YVChatManagerCloudDeleagte.h"
//IM应用版demo 没用到
#import "YVChatManagerChannelDelegate.h"
#import "YVChatManagerTroopsDelegate.h"


@protocol YVChatManageDelegateAll <YVChatManagerLoginDelegate,
                                    YVChatManagerFriendDelegate,
                                    YVChatManagerGroupDelegate,
                                    YVChatManagerChatDelegate,
                                    YVChatManagerCloudDeleagte,
                                    YVChatManagerChannelDelegate,
                                    YVChatManagerTroopsDelegate
                               >


@end



