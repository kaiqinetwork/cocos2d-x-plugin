//
//  YvIMDownloadFileResp.h
//  YvIMSDKAPITestDemo
//
//  Created by liwenjie on 15-3-18.
//  Copyright (c) 2015年 com.yunva.yaya. All rights reserved.
//

//下载文件回应
//#define IM_DOWNLOAD_FILE_RESP             0x19013
//namespace x19013 {
//    enum {
//        /*uint32*/		result	 = 1,   //结果
//        /*string*/      msg      = 2,   //错误描述
//        /*string*/      filename = 3,   //文件名
//        /*string*/      fileid   = 4,   //文件ID
//    };
//}

#import "YvBackBase.h"
@interface YvIMDownloadFileResp : YvBackBase

@property(assign, nonatomic) UInt32     result;//结果
@property(strong, nonatomic) NSString * msg;//错误描述
@property(strong, nonatomic) NSString * filename;//文件名
@property(strong, nonatomic) NSString * fileid;//文件ID
@property(assign, nonatomic) UInt32     percent;//完成百分比

@end
