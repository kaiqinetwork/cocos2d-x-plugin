//
//  YVDeviceManagerMedia.h
//  YvIMSDKManage
//  语音录制和播放接口
//  Created by liwenjie on 14/12/26.
//  Copyright (c) 2014年 com.yunva.yaya. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "YVDeviceManagerBase.h"
#import "YvIMCMDDefHeaders.h"
#import "YVDeviceManagerMediaDelegate.h"
@protocol YVDeviceManagerMedia <YVDeviceManagerBase>

@required


/*!
 @method
 @brief 开始录音
 @discussion
 @param strfilepath -- 文件的绝对路径，可以通过 [self createRecordAudioFilePath] 方法来获取
 @param ext -- 扩展字段，相当于透传作用
 */
- (void)startRecord:(NSString *)strfilepath ext:(NSString *)ext delegate:(id<YVDeviceManagerMediaDelegate>)delegate;

/*!
 @method
 @brief 停止录音
 @discussion
 */
- (void)stopRecord;

/*!
 @method
 @brief 播放录音，两个参数二选一，如果两个都传，默认播放文件的绝对路径(本地路径)
 @discussion
 @param strUrl -- 文件的url
 @param strfilepath -- 文件的绝对路径
 @param ext -- 扩展字段，相当于透传作用
 */
- (void)playAudioWithUrl:(NSString *)strUrl filepath:(NSString *)strfilepath ext:(NSString *)ext delegate:(id<YVDeviceManagerMediaDelegate>)delegate;

/*!
 @method
 @brief 停止播放录音
 @discussion
 */
- (void)stopPlayAudio;

/*!
 @method
 @brief 设置语音识别语言
 @discussion
 @param speechLanguage -- 语音类型, 请参考YvImspeech_Language
 @ im_speech_zn = 1, //中文
 @ im_speech_ct = 2, //粤语
 @ im_speech_en = 3, //英语
 */
- (void)setSpeechLanguage:(int)speechLanguage;

//开始语音识别
/*!
 @method
 @brief 开始语音识别
 @discussion
 @param strfilepath -- 文件的绝对路径，可以通过 [self createRecordAudioFilePath] 方法来获取
 @param ext -- 扩展字段，相当于透传作用
 @param type -- 识别类型 参考 R_Speech_Type 0文件识别  1文件识别返回url   2url识别   3实时语音识别(未完成)
 @param url -- 识别URL
 */
- (void)startRecognize:(NSString *)strfilepath ext:(NSString *)ext type:(R_Speech_Type)type url:(NSString *)url delegate:(id<YVDeviceManagerMediaDelegate>)delegate;

/*!
 @method
 @brief 停止语音识别
 @discussion
 */
- (void)stopRecognize;

/*!
 @method
 @brief 上传文件
 @discussion
 @param filename -- 文件的本地绝对路径
 @param fileId -- 文件id,作为该文件在上传中的唯一标志
 */
- (void)uploadFileReq:(NSString *)filename fileId:(NSString *)fileId;//（保留）

/*!
 @method
 @brief 下载文件
 @discussion
 @param url -- 文件的服务器地址
 @param filename -- 文件下载在本地的绝对路径
 @param fileId -- 文件id,作为该文件在下载中的唯一标志
 */
- (void)downloadFileReq:(NSString *)url filename:(NSString *)filename fileId:(NSString *)fileId;//（保留）

/*!
 @method
 @brief 设置录音信息
 @discussion
 @param times -- 录音最大时长，默认为60秒
 @param volume -- 录音音量回调， 1：开启， 0：关闭
 @param rate -- 录音音量回调， 录音码率，0：低  1：中  2：高 默认为2
 */
- (void)setRecordInfoReqWithMinTime:(UInt32)times volume:(UInt8)volume rate:(UInt8)rate;

#warning  --预备中
/*!
 @method
 @brief 判断URL文件是否存在
 @discussion
 @param url --
 */
-(void)isExistURLFile:(NSString *)url;  //（保留）

/*!
 @method
 @brief 获取URL对应的文件路径
 @discussion
 @param url --
 */
-(void)getCacheFileReq:(NSString *)url;//（保留）

/*!
 @method
 @brief 清除所以缓存
 @discussion
 */
-(void)clearCache;//（保留）
#warning --end 预备中

/*!
 @method
 @brief 刷新界面
 @discussion
 */
-(void)updateLocationReq;//（保留）
/*!
 @method
 @brief 获取定位位置
 @discussion
 */
-(void)getLocationReq;//（保留）







/*!
 @method
 @brief 搜索附近的人
 @discussion
 @param range //搜索范围，单位：米   city 城市 sex 性别 pageSize页面大小 pageNum页数
 */
-(void)searchAroundReq:(UInt32)range city:(NSString *)city sex:(UInt8)sex pageSize:(UInt32)pageSize pageNum:(UInt32)pageNum;//（保留）


@end


