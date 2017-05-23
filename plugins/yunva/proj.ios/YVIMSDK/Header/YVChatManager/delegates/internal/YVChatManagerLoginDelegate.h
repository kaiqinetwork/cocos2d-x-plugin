//
//  YvChatManagerLoginDelegate.h
//  YvIMSDKManage
//
//  Created by liwenjie on 14/12/24.
//  Copyright (c) 2014年 com.yunva.yaya. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "YVChatManageDelegateBase.h"

#import "YvIMCMDDefHeaders.h"
@protocol YVChatManagerLoginDelegate <YVChatManageDelegateBase>

//云娃帐号登录返回
- (void)onIMLoginResp:(YvIMLoginResp *)loginResp;
//第三方登录返回
- (void)onIMThirdLoginResp:(YvIMThirdLoginResp *)thirdLoginResp;
//重连成功通知
- (void)onIMReconnectionNotify:(NSNumber*)userid;
-(void)getsdKviertion:(YvIMGetSdkInfoResp *)version;
@end


