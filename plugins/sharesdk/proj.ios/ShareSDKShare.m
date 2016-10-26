/****************************************************************************
 Copyright (c) 2013 cocos2d-x.org
 
 http://www.cocos2d-x.org
 
 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:
 
 The above copyright notice and this permission notice shall be included in
 all copies or substantial portions of the Software.
 
 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 THE SOFTWARE.
 ****************************************************************************/

#import "ShareSDKShare.h"
#import "ShareWrapper.h"

#import <ShareSDK/ShareSDK.h>
#import <ShareSDKConnector/ShareSDKConnector.h>

// 腾讯开放平台（对应QQ和QQ空间）SDK头文件
#import <TencentOpenAPI/TencentOAuth.h>
#import <TencentOpenAPI/QQApiInterface.h>

// 微信SDK头文件
#import "WXApi.h"

// 新浪微博SDK头文件
#import "WeiboSDK.h"

#define OUTPUT_LOG(...)     if (_debug) NSLog(__VA_ARGS__);

@implementation ShareSDKShare

BOOL _debug;

- (void) configDeveloperInfo: (NSMutableDictionary*) devInfo
{
    NSString *appKey = [devInfo objectForKey:@"ShareSDKAppKey"];
    NSString *sinaWeiboAppKey = [devInfo objectForKey:@"ShareSDKSinaWeiboAppKey"];
    NSString *sinaWeiboAppSecret = [devInfo objectForKey:@"ShareSDKSinaWeiboAppSecret"];
    NSString *sinaWeiboRedirectUri = [devInfo objectForKey:@"ShareSDKSinaWeiboRedirectUri"];
    NSString *weChatAppId = [devInfo objectForKey:@"ShareSDKWeChatAppId"];
    NSString *weChatAppSecret = [devInfo objectForKey:@"ShareSDKWeChatAppSecret"];
    NSString *qqAppId = [devInfo objectForKey:@"ShareSDKQQAppId"];
    NSString *qqAppKey = [devInfo objectForKey:@"ShareSDKQQAppKey"];

    if (appKey == nil) {
        return;
    }
    [ShareSDK registerApp:appKey
            activePlatforms:@[
                            @(SSDKPlatformTypeSinaWeibo),
                            @(SSDKPlatformTypeMail),
                            @(SSDKPlatformTypeSMS),
                            @(SSDKPlatformTypeCopy),
                            @(SSDKPlatformTypeWechat),
                            @(SSDKPlatformTypeQQ)]
            onImport:^(SSDKPlatformType platformType)
            {
                switch (platformType)
                {
                    case SSDKPlatformTypeWechat:
                        [ShareSDKConnector connectWeChat:[WXApi class]];
                        break;
                    case SSDKPlatformTypeQQ:
                        [ShareSDKConnector connectQQ:[QQApiInterface class] tencentOAuthClass:[TencentOAuth class]];
                        break;
                    case SSDKPlatformTypeSinaWeibo:
                        [ShareSDKConnector connectWeibo:[WeiboSDK class]];
                        break;
                    default:
                        break;
                }
            }
            onConfiguration:^(SSDKPlatformType platformType, NSMutableDictionary *appInfo)
            {
         
                switch (platformType)
                {
                    case SSDKPlatformTypeSinaWeibo:
                        //设置新浪微博应用信息,其中authType设置为使用SSO＋Web形式授权
                        [appInfo SSDKSetupSinaWeiboByAppKey:sinaWeiboAppKey
                                                  appSecret:sinaWeiboAppSecret
                                                redirectUri:sinaWeiboRedirectUri
                                                   authType:SSDKAuthTypeBoth];
                        break;
                    case SSDKPlatformTypeWechat:
                        [appInfo SSDKSetupWeChatByAppId:weChatAppId
                                              appSecret:weChatAppSecret];
                        break;
                    case SSDKPlatformTypeQQ:
                        [appInfo SSDKSetupQQByAppId:qqAppId
                                             appKey:qqAppKey
                                           authType:SSDKAuthTypeBoth];
                        break;
                    default:
                        break;
                }
            }];
}

- (void) share: (NSMutableDictionary*) shareInfo
{
    NSArray* images;
    if ([shareInfo objectForKey:@"imagePath"]) {
        images = @[[UIImage imageNamed:[shareInfo objectForKey:@"imagePath"]]];
    }
    NSMutableDictionary *shareParams = [NSMutableDictionary dictionary];
    [shareParams SSDKSetupShareParamsByText:[shareInfo objectForKey:@"text"]
                                     images:images
                                        url:[NSURL URLWithString:[shareInfo objectForKey:@"url"]]
                                      title:[shareInfo objectForKey:@"title"]
                                       type:SSDKContentTypeAuto];
    
    [ShareSDK share:SSDKPlatformTypeAny
         parameters:shareParams
     onStateChanged:^(SSDKResponseState state, NSDictionary *userData, SSDKContentEntity *contentEntity,  NSError *error) {
                   switch (state) {
                       case SSDKResponseStateSuccess:
                       {
                           [ShareWrapper onShareResult:self withRet:kShareSuccess withMsg:@""];
                           break;
                       }
                       case SSDKResponseStateFail:
                       {
                           [ShareWrapper onShareResult:self withRet:kShareFail withMsg:@""];
                           break;
                       }
                       default:
                           break;
                   }
               }
     ];
}

- (void) setDebugMode: (BOOL) debug{
    _debug = debug;
}

- (NSString*) getSDKVersion{
    return @"3.5.0";
}

- (NSString*) getPluginVersion{
    return @"1.0";
}

- (NSString*) getPluginName{
    return @"ShareSDK";
}
@end
