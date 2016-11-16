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
#include "ProtocolUser.h"
#include "ProtocolAnalytics.h"
#include "PluginUtilsIOS.h"

#import "PluginWrapper.h"

namespace cocos2d{ namespace plugin{

static AgentManager* s_AgentManager = nullptr;

AgentManager::AgentManager(): _pUser(nullptr), _pShare(nullptr), _pSocial(nullptr), _pAds(nullptr), _pAnalytics(nullptr)
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
    
void AgentManager::init()
{
    [PluginWrapper analysisDeveloperInfo];
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
        pCustom = dynamic_cast<ProtocolCustom *>(protocol);
        if (pCustom)
        {
            if (pCustom)
            {
                delete pCustom;
            }
            _pCustom = pCustom;
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
    }
    
    return true;
}

std::vector<std::string> AgentManager::getSupportPlugins()
{
    std::vector<std::string> ret;
    NSMutableArray* plugins = [PluginWrapper getSupportPlugins];
    for (NSString *name in plugins) {
        ret.push_back([name UTF8String]);
    }
    
	return ret;
}

}}
