/****************************************************************************
Copyright (c) 2013 cocos2d-x.org

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
#include "ProtocolUser.h"
#include "PluginJniHelper.h"
#include <android/log.h>
#include "PluginUtils.h"
#include "PluginJavaData.h"

namespace cocos2d { namespace plugin {

extern "C" {
JNIEXPORT void JNICALL Java_org_cocos2dx_plugin_UserWrapper_nativeOnActionResult(JNIEnv*  env, jobject thiz, jstring className, jint ret, jstring msg)
{
    std::string strMsg = PluginJniHelper::jstring2string(msg);
    std::string strClassName = PluginJniHelper::jstring2string(className);
    PluginProtocol* pPlugin = PluginUtils::getPluginPtr(strClassName);
	PluginUtils::outputLog(ANDROID_LOG_DEBUG, "ProtocolUser", "nativeOnActionResult(), Get plugin ptr : %p", pPlugin);
    if (pPlugin != NULL)
    {
		PluginUtils::outputLog(ANDROID_LOG_DEBUG, "ProtocolUser", "nativeOnActionResult(), Get plugin name : %s", pPlugin->getPluginName().c_str());
        ProtocolUser* pUser = dynamic_cast<ProtocolUser*>(pPlugin);
        if (pUser != NULL)
        {
            ProtocolUser::ProtocolUserCallback callback = pUser->getCallback();
            if(callback)
			{
				callback(ret, strMsg);
			}
			else
			{
				PluginUtils::outputLog(ANDROID_LOG_DEBUG, "Listener of plugin %s not set correctly", pPlugin->getPluginName().c_str());
			}
        }
    }
}

}

ProtocolUser::ProtocolUser()
{
}

ProtocolUser::~ProtocolUser()
{
}

void ProtocolUser::login()
{
    PluginUtils::callJavaFunctionWithName(this, "login");
}

void ProtocolUser::logout()
{
    PluginUtils::callJavaFunctionWithName(this, "logout");
}

bool ProtocolUser::isLoggedIn()
{
    return PluginUtils::callJavaBoolFuncWithName(this, "isLoggedIn");
}

std::string ProtocolUser::getUserId()
{
    return PluginUtils::callJavaStringFuncWithName(this, "getUserId");
}

std::string ProtocolUser::getAccessToken()
{
	return PluginUtils::callJavaStringFuncWithName(this, "getAccessToken");
}

void ProtocolUser::showToolbar(int position)
{
	if (!isFunctionSupported("showToolbar"))
		return;
	
	PluginParam param(position);
	callFuncWithParam("showToolbar", &param, NULL);
}

void ProtocolUser::hideToolbar()
{
	if (!isFunctionSupported("hideToolbar"))
		return;

	PluginUtils::callJavaFunctionWithName(this, "hideToolbar");
}

void ProtocolUser::enterPlatform()
{
	if (!isFunctionSupported("enterPlatform"))
		return;

	PluginUtils::callJavaFunctionWithName(this, "enterPlatform");
}

void ProtocolUser::exit()
{
	if (!isFunctionSupported("exit"))
		return;

	PluginUtils::callJavaFunctionWithName(this, "exit");
}

void ProtocolUser::accountSwitch()
{
	if (!isFunctionSupported("accountSwitch"))
		return;

	PluginUtils::callJavaFunctionWithName(this, "accountSwitch");
}

void ProtocolUser::realNameRegister()
{
	if (!isFunctionSupported("realNameRegister"))
		return;

	PluginUtils::callJavaFunctionWithName(this, "realNameRegister");
}

void ProtocolUser::antiAddictionQuery()
{
	if (!isFunctionSupported("antiAddictionQuery"))
		return;

	PluginUtils::callJavaFunctionWithName(this, "antiAddictionQuery");
}

void ProtocolUser::submitLoginGameRole(StringMap *data)
{
	if (data == NULL || !isFunctionSupported("submitLoginGameRole"))
		return;

	PluginParam param(data);
	callFuncWithParam("submitLoginGameRole", &param, NULL);
}

}} // namespace cocos2d { namespace plugin {

