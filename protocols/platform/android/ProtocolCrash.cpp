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
#include "ProtocolCrash.h"
#include "PluginJniHelper.h"
#include <android/log.h>
#include "PluginUtils.h"
#include "PluginJavaData.h"

namespace cocos2d { namespace plugin {

ProtocolCrash::ProtocolCrash()
{
}

ProtocolCrash::~ProtocolCrash()
{
}

void ProtocolCrash::setUserIdentifier(const char* identifier)
{
	PluginUtils::callJavaFunctionWithName_oneParam(this, "setUserIdentifier", "(Ljava/lang/String;)V", identifier);
}

void ProtocolCrash::reportException(const char* message, const char* exception)
{
	PluginJavaData* pData = PluginUtils::getPluginJavaData(this);
	PluginJniMethodInfo t;
	if (PluginJniHelper::getMethodInfo(t
		, pData->jclassName.c_str()
		, "reportException"
		, "(Ljava/lang/String;Ljava/lang/String;)V"))
	{
		jstring strMessage = PluginUtils::getEnv()->NewStringUTF(message);
		jstring strException = PluginUtils::getEnv()->NewStringUTF(exception);

		// invoke java method
		t.env->CallVoidMethod(pData->jobj, t.methodID, strMessage, strException);
		t.env->DeleteLocalRef(strMessage);
		t.env->DeleteLocalRef(strException);
		t.env->DeleteLocalRef(t.classID);
	}
}

void ProtocolCrash::leaveBreadcrumb(const char* breadcrumb)
{
	return PluginUtils::callJavaFunctionWithName_oneParam(this, "leaveBreadcrumb", "(Ljava/lang/String;)V", breadcrumb);
}


}} //namespace cocos2d { namespace plugin {
