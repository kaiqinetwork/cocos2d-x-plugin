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
#include "ProtocolService.h"
#include "PluginJniHelper.h"
#include <android/log.h>
#include "PluginUtils.h"
#include "PluginJavaData.h"

namespace cocos2d {namespace plugin {

extern "C" {
	JNIEXPORT void JNICALL Java_org_cocos2dx_plugin_ServiceWrapper_nativeOnActionResult(JNIEnv*  env, jobject thiz, jstring className, jint ret, jstring msg)
	{
		std::string strMsg = PluginJniHelper::jstring2string(msg);
		std::string strClassName = PluginJniHelper::jstring2string(className);
		PluginProtocol* pPlugin = PluginUtils::getPluginPtr(strClassName);
		PluginUtils::outputLog(ANDROID_LOG_DEBUG, "ProtocolService", "nativeOnActionResult(), Get plugin ptr : %p", pPlugin);
		if (pPlugin != NULL)
		{
			PluginUtils::outputLog(ANDROID_LOG_DEBUG, "ProtocolService", "nativeOnActionResult(), Get plugin name : %s", pPlugin->getPluginName().c_str());
			ProtocolService* pService = dynamic_cast<ProtocolService*>(pPlugin);
			if (pService != NULL)
			{
				ProtocolService::ProtocolServiceCallback callback = pService->getCallback();
				if (callback)
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

	ProtocolService::ProtocolService()
	{

	}
	ProtocolService::~ProtocolService()
	{
	}

	void ProtocolService::start()
	{
		PluginUtils::callJavaFunctionWithName(this, "start");
	}

	void ProtocolService::exit()
	{
		PluginUtils::callJavaFunctionWithName(this, "exit");
	}

	void ProtocolService::openReceiver(bool bOpen)
	{
		if (!isFunctionSupported("openReceiver"))
			return;

		PluginParam param(bOpen);
		callFuncWithParam("openReceiver", &param, NULL);
	}

	int ProtocolService::getUnreadMsg()
	{
		if (!isFunctionSupported("getUnreadMsg"))
			return 0;

		return PluginUtils::callJavaIntFuncWithName(this, "getUnreadMsg");
	}

	void ProtocolService::openNotification(bool bOpen)
	{
		if (!isFunctionSupported("openNotification"))
			return;

		PluginParam param(bOpen);
		callFuncWithParam("openNotification", &param, NULL);
	}

	void ProtocolService::setUserInfo(TUserInfo userInfo)
	{
		if (!isFunctionSupported("setUserInfo"))
			return;

		PluginJavaData* pData = PluginUtils::getPluginJavaData(this);
		PluginJniMethodInfo t;
		if (PluginJniHelper::getMethodInfo(t
			, pData->jclassName.c_str()
			, "setUserInfo"
			, "(Ljava/util/Hashtable;)V"))
		{
			// generate the hashtable from map
			jobject obj_Map = PluginUtils::createJavaMapObject(&userInfo);

			// invoke java method
			t.env->CallVoidMethod(pData->jobj, t.methodID, obj_Map);
			t.env->DeleteLocalRef(obj_Map);
			t.env->DeleteLocalRef(t.classID);
		}
	}

	}
}

