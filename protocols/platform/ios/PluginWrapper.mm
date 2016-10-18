#import "PluginWrapper.h"
#include <vector>
#include <map>
#include <tinyxml2/tinyxml2.h>
#include <string>

@implementation PluginWrapper

NSMutableArray* s_supportPlugins = [[NSMutableArray alloc] init];
NSMutableDictionary* s_pluginParams = [[NSMutableDictionary alloc] init];
NSMutableDictionary* s_appInfo = [[NSMutableDictionary alloc] init];

+ (void) analysisDeveloperInfo
{
    NSString* fullpath = [[NSBundle mainBundle] pathForResource:@"DeveloperInfo.xml"
                                                         ofType:nil
                                                    inDirectory:@"plugins"];
    if (fullpath == nil) {
        return;
    }
    
    std::string path = [fullpath UTF8String];
    tinyxml2::XMLDocument doc;
    if (tinyxml2::XML_SUCCESS == doc.LoadFile(path.c_str())) {
        tinyxml2::XMLElement* rootEle = doc.RootElement();
        const tinyxml2::XMLAttribute* attr = rootEle->FirstAttribute();
        while (attr) {
            [s_pluginParams setValue:[NSString stringWithUTF8String:attr->Name()]forKey:[NSString stringWithUTF8String:attr->Value()]];
            attr = attr->Next();
        }
        
        tinyxml2::XMLElement* ele = rootEle->FirstChildElement("PluginList");
        if (ele) {
            tinyxml2::XMLElement* childEle = ele->FirstChildElement("Plugin");
            while (childEle) {
                const char* className = childEle->Attribute("className");
                if (className) {
                    [s_supportPlugins addObject:[NSString stringWithUTF8String:className]];
                }
                
                childEle = childEle->NextSiblingElement("Plugin");
            }
        }
        
        ele = rootEle->FirstChildElement("ParamList");
        if (ele) {
            tinyxml2::XMLElement* childEle = ele->FirstChildElement("Param");
            while (childEle) {
                const char* name = childEle->Attribute("name");
                const char* value = childEle->Attribute("value");
                if (name != nullptr && value != nullptr) {
                    [s_pluginParams setValue:[NSString stringWithUTF8String:value]forKey:[NSString stringWithUTF8String:name]];
                }
                
                childEle = childEle->NextSiblingElement("Param");
            }
        }
    }
}

+ (NSMutableArray*) getSupportPlugins
{
    return s_supportPlugins;
}

+ (NSDictionary*) getPluginParams
{
    return s_pluginParams;
}

+ (NSDictionary*) getAppInfo
{
    return s_appInfo;
}

@end
