/****************************************************************************
 Copyright (c) 2014 Chukong Technologies Inc.

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
 
#include <vector>

#include "AgentManager.h"
#include "PluginManager.h"
#include "PluginUtils.h"

namespace cocos2d{ namespace plugin{

static AgentManager* s_AgentManager = nullptr;

AgentManager::AgentManager() : _pUser(nullptr), _pShare(nullptr), _pSocial(nullptr), _pAds(nullptr), _pAnalytics(nullptr), _pCrash(nullptr), _pCustom(nullptr), _pService(nullptr), _pVoice(nullptr)
{

}

AgentManager::~AgentManager()
{
	this->unloadAllPlugins();
}

void AgentManager::unloadAllPlugins()
{
	delete _pUser;
	delete _pShare;
	delete _pSocial;
	delete _pAds;
	delete _pAnalytics;
	delete _pCrash;
	delete _pCustom;
	delete _pService;
	delete _pVoice;
	for(std::map<std::string, ProtocolIAP*>::iterator iter = _pluginsIAPMap.begin(); iter != _pluginsIAPMap.end(); ++iter)
	{
		delete iter->second;
	}
	_pluginsIAPMap.clear();
}

AgentManager* AgentManager::getInstance()
{
	if(nullptr == s_AgentManager)
	{
		s_AgentManager = new (std::nothrow) AgentManager();
		//s_AgentManager->init();
	}
	return s_AgentManager;
}

void AgentManager::end()
{
	if(s_AgentManager)
	{
		delete s_AgentManager;
		s_AgentManager = nullptr;
	}
}

bool AgentManager::loadAllPlugins()
{
	std::vector<std::string> plugins = getSupportPlugins();
	return loadPlugins(plugins);	
}

bool AgentManager::loadPlugins(const std::vector<std::string>& plugins)
{
	PluginProtocol* protocol;
	ProtocolUser* pUser;
	ProtocolShare* pShare;
	ProtocolSocial* pSocial;
	ProtocolAds* pAds;
	ProtocolAnalytics* pAnalytics;
	ProtocolCrash* pCrash;
	ProtocolIAP* pIAP;
	ProtocolCustom* pCustom;
	ProtocolService* pService;
	ProtocolVoice* pVoice;
	for (int i = 0; i < plugins.size(); ++i)
	{
		protocol = dynamic_cast<PluginProtocol *>(PluginManager::getInstance()->loadPlugin(plugins[i].c_str()));
		pUser = dynamic_cast<ProtocolUser *>(protocol);
		if (pUser)
		{
			if (_pUser)
			{
				delete _pUser;
			}
			_pUser = pUser;
		}
		pShare = dynamic_cast<ProtocolShare *>(protocol);
		if (pShare)
		{
			if (_pShare)
			{
				delete _pShare;
			}
			_pShare = pShare;
		}
		pSocial = dynamic_cast<ProtocolSocial *>(protocol);
		if (pSocial)
		{
			if (_pSocial)
			{
				delete _pSocial;
			}
			_pSocial = pSocial;
		}
		pAds = dynamic_cast<ProtocolAds *>(protocol);
		if (pAds)
		{
			if (_pAds)
			{
				delete _pAds;
			}
			_pAds = pAds;
		}
		pAnalytics = dynamic_cast<ProtocolAnalytics *>(protocol);
		if (pAnalytics)
		{
			if (_pAnalytics)
			{
				delete _pAnalytics;
			}
			_pAnalytics = pAnalytics;
		}
		pCrash = dynamic_cast<ProtocolCrash *>(protocol);
		if (pCrash)
		{
			if (pCrash)
			{
				delete pCrash;
			}
			_pCrash = pCrash;
		}
		pIAP = dynamic_cast<ProtocolIAP *>(protocol);
		if (pIAP)
		{
			ProtocolIAP*& dummy = _pluginsIAPMap[pIAP->getPluginName()];
			if (dummy)
			{
				delete dummy;
			}
			dummy = pIAP;
		}
		pCustom = dynamic_cast<ProtocolCustom *>(protocol);
		if (pCustom)
		{
			if (_pCustom)
			{
				delete _pCustom;
			}
			_pCustom = pCustom;
		}
		pService = dynamic_cast<ProtocolService *>(protocol);
		if (pService)
		{
			if (_pService)
			{
				delete _pService;
			}
			_pService = pService;
		}
		pVoice = dynamic_cast<ProtocolVoice *>(protocol);
		if (pVoice)
		{
			if (_pVoice)
			{
				delete _pVoice;
			}
			_pVoice = pVoice;
		}
	}

	return true;
}

std::vector<std::string> AgentManager::getSupportPlugins()
{
	std::vector<std::string> plugins;

	PluginJniMethodInfo t;
	JNIEnv* env = PluginUtils::getEnv();

	if(PluginJniHelper::getStaticMethodInfo(t, "org/cocos2dx/plugin/PluginWrapper", "getSupportPlugins", "()Ljava/util/Vector;"))
	{
		jobject jvector = t.env->CallStaticObjectMethod(t.classID, t.methodID);
		PluginJniMethodInfo tSizeMethod;
		PluginJniMethodInfo tGetMethod;
		if (PluginJniHelper::getMethodInfo(tGetMethod, "java/util/Vector", "get", "(I)Ljava/lang/Object;"))
		{
			jint jSize = 0;
			jstring jValue; 
			std::string stdValue;

			if (PluginJniHelper::getMethodInfo(tSizeMethod, "java/util/Vector", "size", "()I"))
			{
				jSize = env->CallIntMethod(jvector, tSizeMethod.methodID);

				env->DeleteLocalRef(tSizeMethod.classID);
			}

			for (int i = 0; i < jSize; i++)
			{
				jValue = (jstring)(env->CallObjectMethod(jvector, tGetMethod.methodID, (jint)i));
				stdValue = PluginJniHelper::jstring2string(jValue);
				if (!stdValue.empty())
					plugins.push_back(stdValue);
				tGetMethod.env->DeleteLocalRef(jValue);
			}

			env->DeleteLocalRef(tGetMethod.classID);
		}
		env->DeleteLocalRef(jvector);
		env->DeleteLocalRef(t.classID);
	}
	
	return plugins;
}

}}
