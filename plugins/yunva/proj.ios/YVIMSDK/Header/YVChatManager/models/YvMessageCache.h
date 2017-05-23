//
//  YvMessageCache.h
//  YvIMSDKAPITestDemo
//
//  Created by liwenjie on 15-5-20.
//  Copyright (c) 2015年 com.yunva.yaya. All rights reserved.
//

#import <Foundation/Foundation.h>
@class YvMessageCache;
@protocol YvMessageCacheDelegate <NSObject>
@optional
-(void)stopSendMessageByFailed:(YvMessageCache*)cache;
@end
@interface YvMessageCache : NSObject
@property(strong, nonatomic) NSString * text;
@property(strong, nonatomic) NSString * filePath;
@property(assign, nonatomic) int type;
@property(assign, nonatomic) int voiceDuration;
@property(strong, nonatomic) NSString * attch;
@property(strong, nonatomic) NSString * cacheId;//缓存唯一的标志id
@property(strong ,nonatomic) NSTimer *time;
@property(assign ,nonatomic) BOOL isSend; //是否发送

@property(nonatomic,weak) id<YvMessageCacheDelegate> delegate;
-(void)startTime;
@end
