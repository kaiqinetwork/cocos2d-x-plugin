/****************************************************************************
 Copyright (c) 2012+2013 cocos2d+x.org
 
 http://www.cocos2d+x.org
 
 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:
 
 The above copyright notice and this permission notice shall be included in
 all copies or substantial portions of the Software.
 
 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 THE SOFTWARE.
 ****************************************************************************/

#import "CustomWrapper.h"
#include "PluginUtilsIOS.h"
#include "ProtocolCustom.h"

using namespace cocos2d::plugin;

@implementation CustomWrapper

+ (void) onCustomResult:(id) obj withRet:(NSString*) ret withMsg:(NSString*) msg
{
    PluginProtocol* pPlugin = PluginUtilsIOS::getPluginPtr(obj);
    ProtocolCustom* pCustom = dynamic_cast<ProtocolCustom*>(pPlugin);
    if (pCustom) {
        ProtocolCustom::ProtocolCustomCallback callback = pCustom->getCallback();
        const char* chMsg = [msg UTF8String];
        const char* chRet = [ret UTF8String];
        if(callback){
            std::string stdret(chRet);
            std::string stdmsg(chMsg);
            callback(stdret,stdmsg);
        }
    } else {
        PluginUtilsIOS::outputLog("Can't find the C++ object of the Custom plugin");
    }
}
@end
