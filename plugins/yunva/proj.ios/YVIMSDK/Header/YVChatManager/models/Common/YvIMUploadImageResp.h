//
//  YvIMUploadImageResp.h
//  YvIMSDKAPI
//  上传图片文件回应
//  Created by liwenjie on 15-3-9.
//  Copyright (c) 2015年 com.yunva.yaya. All rights reserved.
//

//#define IM_UPLOAD_IMAGE_RESP		   0x19011

#import "YvBackBase.h"

@interface YvIMUploadImageResp : YvBackBase

@property(assign, nonatomic) UInt32             result;//0为成功,其它为失败
@property(strong, nonatomic) NSString *         msg;//返回的错误描述
@property(strong, nonatomic) NSString *         url1;//小图
@property(strong, nonatomic) NSString *         url2;//原图

@end
