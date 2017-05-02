/** @file ProtocolService.h
 */
#ifndef  __CCX_PROTOCOL_SERVICE_H__
#define  __CCX_PROTOCOL_SERVICE_H__

#include "PluginProtocol.h"
#include <map>
#include <string>
#include <functional>

namespace cocos2d {
	namespace plugin {
typedef enum 
{
	sInitSuccess = 0,/**< enum value is callback of succeeding in initing sdk. */
	sInitFail,/**< enum  value is callback of failing to init sdk. */
	sNewMsg,
    sServiceExtension = 80000 /**< enum value is  extension code . */
} ServiceResultCode;

typedef std::map<std::string, std::string> TUserInfo;

/**   
 *@class  ProtocolService
 */
class ProtocolService : public PluginProtocol
{
public:

	typedef std::function<void(int, std::string&)> ProtocolServiceCallback;

    /**
    @breif set the result listener
    @param pListener The callback object for custom result
    */

	ProtocolService();
	virtual ~ProtocolService();

	void start();

	void exit();

	void setUserInfo(TUserInfo userInfo);

	void openReceiver(bool bOpen);

	int getUnreadMsg();

	void openNotification(bool bOpen);

	inline void setCallback(const ProtocolServiceCallback &cb)
	{
		_callback = cb;
	}

	inline ProtocolServiceCallback& getCallback()
	{
		return _callback;
	}

protected:
	ProtocolServiceCallback _callback;

};

}} // namespace anysdk { namespace framework {

#endif   /* ----- #ifndef __CCX_PROTOCOL_Custom_H__ ----- */
