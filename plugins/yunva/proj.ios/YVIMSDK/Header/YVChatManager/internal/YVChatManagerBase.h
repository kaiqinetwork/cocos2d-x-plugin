//
//  YvChatManagerBase.h
//  YvIMSDKManage
//
//  Created by liwenjie on 14/12/24.
//  Copyright (c) 2014年 com.yunva.yaya. All rights reserved.
//

// 是否需要语音识别,打开:需要，注释:不需要
#define kOpenRecognizerDevice

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "YvChatManageDelegate.h"

#define MARK_DEPRECATED __attribute__((deprecated))

/*!
 @protocol
 @brief 聊天的基础协议, 用于注册对象到监听列表和从监听列表中移除对象
 @discussion
 */
@protocol YVChatManagerBase <NSObject>


//创建子目录
- (NSString *)createAudioRecordDir;
//创建目录
- (NSString *)createFileTempDir:(NSString *)directory;
//创建绝对路径
- (NSString *)createRecordAudioFilePath;
//保存图片到document
- (NSString *)saveImage:(UIImage *)tempImage WithName:(NSString *)imageName;
//压缩图片
+ (UIImage *)makeThumbnailFromImage:(UIImage *)srcImage scale:(double)imageScale;
//创建图片
- (NSString*)creatUUID;




@end
