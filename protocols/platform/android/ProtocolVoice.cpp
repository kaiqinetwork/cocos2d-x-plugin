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
#include "ProtocolVoice.h"
#include "PluginJniHelper.h"
#include <android/log.h>
#include "PluginUtils.h"
#include "PluginJavaData.h"

namespace cocos2d {namespace plugin {

extern "C" {
	JNIEXPORT void JNICALL Java_org_cocos2dx_plugin_VoiceWrapper_nativeOnActionResult(JNIEnv*  env, jobject thiz, jstring className, jint ret, jstring msg)
	{
		std::string strMsg = PluginJniHelper::jstring2string(msg);
		std::string strClassName = PluginJniHelper::jstring2string(className);
		PluginProtocol* pPlugin = PluginUtils::getPluginPtr(strClassName);
		PluginUtils::outputLog(ANDROID_LOG_DEBUG, "ProtocolVoice", "nativeOnActionResult(), Get plugin ptr : %p", pPlugin);
		if (pPlugin != NULL)
		{
			PluginUtils::outputLog(ANDROID_LOG_DEBUG, "ProtocolVoice", "nativeOnActionResult(), Get plugin name : %s", pPlugin->getPluginName().c_str());
			ProtocolVoice* pVoice = dynamic_cast<ProtocolVoice*>(pPlugin);
			if (pVoice != NULL)
			{
				ProtocolVoice::ProtocolVoiceCallback callback = pVoice->getCallback();
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

	ProtocolVoice::ProtocolVoice()
	{

	}
	ProtocolVoice::~ProtocolVoice()
	{
	}

	void ProtocolVoice::setUserInfo(std::map<std::string, std::string> userInfo)
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

	void ProtocolVoice::startRecord()
	{
		PluginUtils::callJavaFunctionWithName(this, "startRecord");
	}
	
	void ProtocolVoice::start()
	{
		PluginUtils::callJavaFunctionWithName(this, "login");
	}

	void ProtocolVoice::stopRecord()
	{
		PluginUtils::callJavaFunctionWithName(this, "stopRecord");
	}

	void ProtocolVoice::cancelRecord()
	{
		PluginUtils::callJavaFunctionWithName(this, "cancelRecord");
	}

	void ProtocolVoice::exit()
	{
		PluginUtils::callJavaFunctionWithName(this, "exit");
	}

	std::string ProtocolVoice::getVoiceUrl()
	{
		return PluginUtils::callJavaStringFuncWithName(this, "getVoiceUrl");
	}

	void ProtocolVoice::playVoice(std::string strUrl)
	{
		PluginParam param(strUrl.c_str());
		callFuncWithParam("playVoice", &param, NULL);
	}
	}
}

