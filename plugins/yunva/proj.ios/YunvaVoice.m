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

#import "YunvaVoice.h"
#import "VoiceWrapper.h"
#import "YVIMSDK_Manage.h"


#define OUTPUT_LOG(...)     if (_debug) NSLog(__VA_ARGS__);

@implementation YunvaVoice

NSString *_userId;
NSString *_userPassowrd;
NSString *_voicePath;

BOOL _cancel;
BOOL _debug;

- (void) configDeveloperInfo: (NSMutableDictionary*) devInfo{
    if ([devInfo objectForKey:@"YayaAppId"]){
        NSString *appId = [devInfo objectForKey:@"YayaAppId"];
        [[YVIMSDK_Manage shareInstance]SDKInitWithAppId:(NSString *)appId isTest:false];
        _voicePath = [[[YVIMSDK_Manage shareInstance] chatManager] createRecordAudioFilePath];
        [VoiceWrapper onVoiceResult:self withRet:kVoiceInitSuccess withMsg:@""];
    }else {
        OUTPUT_LOG(@"YayaAppId is null");
        [VoiceWrapper onVoiceResult:self withRet:kVoiceInitFail withMsg:@""];
    }
}

- (void) setUserInfo : (NSMutableDictionary*) userInfo{
    if ([userInfo objectForKey:@"userId"]) {
        _userId = [userInfo objectForKey:@"userId"];
    }else{
        OUTPUT_LOG(@"Yunva Voice setUserInfo : userId is null");
        _userId = @"userId";
    }
    
    if ([userInfo objectForKey:@"nickName"]) {
        _userPassowrd = [userInfo objectForKey:@"nickName"];
    }else{
        OUTPUT_LOG(@"Yunva Voice setUserInfo: nickName is null");
        _userPassowrd = @"nickName";
    }
}

- (void) startRecord{
    OUTPUT_LOG(@"Yunva Voice startRecord");
    [[[YVIMSDK_Manage shareInstance] deviceManager] removeDelegate:self];
    [[[YVIMSDK_Manage shareInstance]deviceManager] stopPlayAudio];
    
    NSString* filepath = [[[YVIMSDK_Manage shareInstance] chatManager] createRecordAudioFilePath];
    double dateTime = [[NSDate date] timeIntervalSince1970];
    NSString *flag = [NSString stringWithFormat:@"%f",dateTime];
    [[[YVIMSDK_Manage shareInstance] deviceManager] addDelegate:self];
    [[[YVIMSDK_Manage shareInstance] deviceManager] startRecord:filepath ext:flag delegate:nil];
}

- (void) stopRecord{
    OUTPUT_LOG(@"Yunva Voice stopRecord");
    [[[YVIMSDK_Manage shareInstance]deviceManager] stopRecord];
    _cancel = false;
}

- (void) cancelRecord{
    OUTPUT_LOG(@"Yunva Voice cancelRecord");
    [[[YVIMSDK_Manage shareInstance]deviceManager] stopRecord];
    _cancel = true;
}

- (void) start{
    OUTPUT_LOG(@"Yunva Voice start");
    [[[YVIMSDK_Manage shareInstance] chatManager] addDelegate:self];
    [[[YVIMSDK_Manage shareInstance] deviceManager] addDelegate:self];
    
    UInt32 userId = [_userId intValue];
    NSString* serviceId = @"1";
    NSArray* willCardId = @[@"0x001"];
    [[[YVIMSDK_Manage shareInstance] chatManager] loginWithUserId:userId password:_userPassowrd pgameServiceID:serviceId wildCard:willCardId readstatus:1];
//    [[[YVIMSDK_Manage shareInstance] deviceManager] setSpeechLanguage:1];
}

- (void) exit{
    OUTPUT_LOG(@"Yunva Voice exit");
    [[[YVIMSDK_Manage shareInstance]chatManager]logout];
    [[[YVIMSDK_Manage shareInstance]chatManager]removeDelegate:self];
    [[[YVIMSDK_Manage shareInstance]chatManager]releaseInstance];
    
    [[[YVIMSDK_Manage shareInstance]deviceManager] stopPlayAudio];
    [[[YVIMSDK_Manage shareInstance]deviceManager]removeDelegate:self];
    [[[YVIMSDK_Manage shareInstance]deviceManager]releaseInstance];
}

- (NSString*) getVoiceUrl{
    return _voicePath;
}

- (void) playVoice: (NSString*) strUrl{
    OUTPUT_LOG(@"Yunva Voice playVoice");
    [[[YVIMSDK_Manage shareInstance]deviceManager] stopPlayAudio];
    
    _voicePath = strUrl;
    if ([_voicePath hasPrefix:@"http://"]) {
        [[[YVIMSDK_Manage shareInstance] deviceManager] playAudioWithUrl:_voicePath filepath:nil ext:nil delegate:nil];
    }else{
        [[[YVIMSDK_Manage shareInstance] deviceManager] playAudioWithUrl:nil filepath:_voicePath ext:nil delegate:nil];
    }
}

- (void) setDebugMode: (BOOL) debug{
    _debug = debug;
}

- (NSString*) getSDKVersion{
    return @"1.0.3";
}

- (NSString*) getPluginVersion{
    return @"1.0";
}

- (NSString*) getPluginName{
    return @"Yunva";
}

- (void) onIMLoginResp:(YvIMLoginResp *)loginResp{
    if (loginResp.result == 0) {
        [VoiceWrapper onVoiceResult:self withRet:kVoiceLoginSuccess withMsg:@""];
    }else{
        [VoiceWrapper onVoiceResult:self withRet:kVoiceLoginFail withMsg:@""];
    }
}

- (void) onIMRecordStopResp:(YvIMRecordStopResp *)recordStopResp{
    if (recordStopResp.time < 1000){
        [VoiceWrapper onVoiceResult:self withRet:kVoiceRecordError withMsg:@""];
        return;
    }
    
    if (recordStopResp.strfilepath) {
        NSString* strFileParh = recordStopResp.strfilepath;
        double dateTime = [[NSDate date] timeIntervalSince1970];
        NSString *fileId = [NSString stringWithFormat:@"%f",dateTime];
        if (!_cancel) {
            [[[YVIMSDK_Manage shareInstance] deviceManager] uploadFileReq:strFileParh fileId:fileId];
        }else{
            [VoiceWrapper onVoiceResult:self withRet:kVoiceRecordError withMsg:@""];
        }
    }
}

- (void)onIMUploadFileResp:(YvIMUploadFileResp *)uploadFileResp{
    if (uploadFileResp.result == 0) {
        [VoiceWrapper onVoiceResult:self withRet:kVoiceUploadSuccess withMsg:@""];
        _voicePath = uploadFileResp.fileurl;
    }else{
        [VoiceWrapper onVoiceResult:self withRet:kVoiceUploadFail withMsg:@""];
    }
}

- (void) onIMRecordFinishPlayResp:(YvIMRecordFinishPlayResp *)recordFinishPlayResp{
    if (recordFinishPlayResp.result == 0) {
        [VoiceWrapper onVoiceResult:self withRet:kVoicePlaySuccess withMsg:@""];
    }else{
        [VoiceWrapper onVoiceResult:self withRet:kVoicePlayFail withMsg:@""];
    }
}

-(void)onIMReconnectionNotify:(NSNumber *)userid{
    [VoiceWrapper onVoiceResult:self withRet:kVoiceReconnectSuccess withMsg:@""];
}
@end
