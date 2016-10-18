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

#ifndef __AGENTMANAGER_H__
#define __AGENTMANAGER_H__

#include <functional>
#include <string>
#include <map>

#include "ProtocolShare.h"
#include "ProtocolSocial.h"
#include "ProtocolIAP.h"
#include "ProtocolAds.h"
#include "ProtocolUser.h"
#include "ProtocolAnalytics.h"
#include "ProtocolCrash.h"

namespace cocos2d{namespace plugin{

class AgentManager{
public:
	virtual ~AgentManager();
	static AgentManager* getInstance();
	static void end();
    void init();
	virtual bool loadPlugins(const std::vector<std::string>& plugins);
	virtual bool loadAllPlugins();
	virtual void unloadAllPlugins();

	inline ProtocolUser* getUserPlugin()
	{
		return _pUser;
	}

	inline ProtocolShare* getSharePlugin()
	{
		return _pShare;
	}

	inline ProtocolSocial* getSocialPlugin()
	{
		return _pSocial;
	}

	inline ProtocolAds* getAdsPlugin()
	{
		return _pAds;
	}

	inline ProtocolAnalytics* getAnalyticsPlugin()
	{
		return _pAnalytics;
	}

	inline ProtocolCrash* getCrashPlugin()
	{
		return _pCrash;
	}

	inline std::map<std::string, ProtocolIAP*>* getIAPPlugin()
	{
		return &_pluginsIAPMap;
	}

protected:
	AgentManager();
	std::vector<std::string> getSupportPlugins();

	ProtocolUser* _pUser;
	ProtocolShare* _pShare;
	ProtocolSocial* _pSocial;
	ProtocolAds* _pAds;
	ProtocolAnalytics* _pAnalytics;
	ProtocolCrash* _pCrash;
	std::map<std::string, ProtocolIAP*> _pluginsIAPMap;
};
}}
#endif
