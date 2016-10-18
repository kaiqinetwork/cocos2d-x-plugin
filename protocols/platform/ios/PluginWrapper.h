#import <Foundation/Foundation.h>
#include <map>

@interface PluginWrapper : NSObject
{
    
}

+ (void) analysisDeveloperInfo;
+ (NSMutableArray*) getSupportPlugins;
+ (NSMutableDictionary*) getAppInfo;
+ (NSMutableDictionary*) getPluginParams;
@end
