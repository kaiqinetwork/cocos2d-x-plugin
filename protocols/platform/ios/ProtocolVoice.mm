/****************************************************************************
Copyright (c) 2012-2013 cocos2d-x.org

http://www.cocos2d-x.org

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
#include "ProtocolVoice.h"
#include "PluginUtilsIOS.h"
#import "InterfaceVoice.h"

namespace cocos2d {
    namespace plugin {
        ProtocolVoice::ProtocolVoice(){
            
        }
        
        ProtocolVoice::~ProtocolVoice(){
            
        }
        
        void ProtocolVoice::setUserInfo(std::map<std::string, std::string> userInfo){
            if (userInfo.empty()){
                PluginUtilsIOS::outputLog("The Voice userInfo of %s is empty!", this->getPluginName().c_str());
                return;
            }
            
            PluginOCData* pData = PluginUtilsIOS::getPluginOCData(this);
            assert(pData != NULL);
            
            id ocObj = pData->obj;
            if ([ocObj conformsToProtocol:@protocol(InterfaceVoice)]) {
                NSObject<InterfaceVoice>* curObj = ocObj;
                NSMutableDictionary * curInfo = PluginUtilsIOS::createDictFromMap(&userInfo);
                [curObj setUserInfo:curInfo];
            }
        }
        
        void ProtocolVoice::startRecord(){
            PluginUtilsIOS::callOCFunctionWithName(this, "startRecord");
        }
        
        void ProtocolVoice::stopRecord(){
            PluginUtilsIOS::callOCFunctionWithName(this, "stopRecord");
        }
        
        void ProtocolVoice::cancelRecord(){
            PluginUtilsIOS::callOCFunctionWithName(this, "cancelRecord");
        }
        
        void ProtocolVoice::exit(){
            PluginUtilsIOS::callOCFunctionWithName(this, "exit");
        }
        
        void ProtocolVoice::start(){
            PluginUtilsIOS::callOCFunctionWithName(this, "start");
        }
        
        std::string ProtocolVoice::getVoiceUrl(){
            return PluginUtilsIOS::callOCStringFunctionWithName(this, "getVoiceUrl");
        }
        
        void ProtocolVoice::playVoice(std::string strUrl){
            PluginOCData* pData = PluginUtilsIOS::getPluginOCData(this);
            assert(pData != NULL);
            
            id ocObj = pData->obj;
            if ([ocObj conformsToProtocol:@protocol(InterfaceVoice)]) {
                NSObject<InterfaceVoice>* curObj = ocObj;
                NSString* url = [NSString stringWithUTF8String:strUrl.c_str()];
                [curObj playVoice:url];
            }
        }
    } //namespace plugin {
} // namespace cocos2d {
