//
//  YVDeviceManagerMediaDelegate.h
//  YvIMSDKManage
//  语音短信录制接口
//  Created by liwenjie on 14/12/26.
//  Copyright (c) 2014年 com.yunva.yaya. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "YVDeviceManagerDelegateBase.h"
#import "YvIMCMDDefHeaders.h"
@protocol YVDeviceManagerMediaDelegate <YVDeviceManagerDelegateBase>

//停止录音返回  回调返回录音文件路径名
- (void)onIMRecordStopResp:(YvIMRecordStopResp *)recordStopResp;
//播放语音完成返回
- (void)onIMRecordFinishPlayResp:(YvIMRecordFinishPlayResp *)recordFinishPlayResp;
//停止语音识别返回
- (void)onIMSpeechStopResp:(YvIMSpeechStopResp *)speechStopResp;
//上传文件返回
- (void)onIMUploadFileResp:(YvIMUploadFileResp *)uploadFileResp;
//下载文件返回
- (void)onIMDownloadFileResp:(YvIMDownloadFileResp *)downloadFileResp;
//录音声音大小通知
- (void)onIMRecordVolumeNotify:(YvIMRecordVolumeNotify *)volumeNotify;
//播放URL下载进度
- (void)onIMRecordPlayPercentNotify:(YvIMRecordPlayPercentNotify*)recordPlayPercentNotify;
//获取URL对应的文件路径
-(void)onIMGetCacheFileResp:(YvIMGetCacheFileResp*)getCacheFileResp;
//更新地理位置返回
-(void)onIMUploadLocationResp:(YvIMUploadLocationResp *)uploadLocationResp;
//获取定位位置返回
-(void)onIMGetLocationResp:(YvIMGetLocationResp *)getLocationResp;
//获取附近的人返回
-(void)onIMSearchAroundResp:(YvIMSearchAroundResp *)searchAroundResp;
@end

