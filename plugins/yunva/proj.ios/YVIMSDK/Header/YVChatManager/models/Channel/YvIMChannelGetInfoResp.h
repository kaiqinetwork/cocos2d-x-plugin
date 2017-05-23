//
//  YvIMChannelGetInfoReq.h
//  YvIMSDKAPI
//  获取频道信息返回
//  Created by liwenjie on 15-3-9.
//
//

//#define IM_CHANNEL_GETINFO_RESP			0x16001

#import "YvBackBase.h"

@interface YvIMChannelGetInfoResp : YvBackBase
#ifdef czw
@property(strong, nonatomic) NSMutableArray * xGame_channel;//游戏通道
#endif
@end
