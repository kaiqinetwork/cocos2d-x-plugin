//
//  YvChatManagerInitSdk.h
//  YvImSDKOCWapper
//
//  Created by dada on 15/10/8.
//  Copyright (c) 2015年 yunva. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "YVChatManagerBase.h"

@protocol YvChatManagerInitSdk <YVChatManagerBase>
- (void)initSDKwithAppid:(UInt32)appId AndisTest:(BOOL)isTest;
- (void)initSDKwithAppid:(UInt32)appId AndisTest:(BOOL)isTest isOverSea:(BOOL)isOverSea;

@end
