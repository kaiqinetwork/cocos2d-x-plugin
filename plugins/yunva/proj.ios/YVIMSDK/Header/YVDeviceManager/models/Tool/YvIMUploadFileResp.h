//
//  YvIMUploadFileResp.h
//  YvIMSDKAPITestDemo
//
//  Created by liwenjie on 15-3-18.
//  Copyright (c) 2015年 com.yunva.yaya. All rights reserved.
//

//上传文件回应
//#define IM_UPLOAD_FILE_RESP		         0x19011
//namespace x19011{
//    enum{
//        /*uint32*/		result	 = 1,   //结果
//        /*string*/      msg      = 2,   //错误描述
//        /*string*/      fileid   = 3,   //文件ID
//        /*string*/      fileurl  = 4,   //返回文件地址，若为图像文件，则是大图像地址
//        /*string*/      thumburl = 5,   //返回文件地址，若为图像文件，则是小图像地址，若为音频文件，字段为空
//    };
//}

#import "YvBackBase.h"
@interface YvIMUploadFileResp : YvBackBase

@property(assign, nonatomic) UInt32     result;//结果
@property(strong, nonatomic) NSString * msg;//错误描述
@property(strong, nonatomic) NSString * fileid;//文件ID
@property(strong, nonatomic) NSString * fileurl;//返回文件地址，若为图像文件，则是大图像地址
@property(assign, nonatomic) UInt32     percent; //上传百分比
@end
