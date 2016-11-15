/** @file ProtocolCustom.h
 */
#ifndef  __CCX_PROTOCOL_CUSTOM_H__
#define  __CCX_PROTOCOL_CUSTOM_H__

#include "PluginProtocol.h"
#include <map>
#include <string>
#include <functional>

namespace cocos2d {
	namespace plugin {
typedef enum 
{
    kCustomExtension = 80000 /**< enum value is  extension code . */
} CustomResultCode;

/**   
 *@class  ProtocolCustom
 *@brief the interface of custom  
 */
class ProtocolCustom : public PluginProtocol
{
public:

	typedef std::function<void(std::string&, std::string&)> ProtocolCustomCallback;

    /**
    @breif set the result listener
    @param pListener The callback object for custom result
    @wraning Must invoke this interface before custom
    */

	ProtocolCustom();
	virtual ~ProtocolCustom();

	inline void setCallback(const ProtocolCustomCallback &cb)
	{
		_callback = cb;
	}

	inline ProtocolCustomCallback& getCallback()
	{
		return _callback;
	}

protected:
	ProtocolCustomCallback _callback;

};

}} // namespace anysdk { namespace framework {

#endif   /* ----- #ifndef __CCX_PROTOCOL_Custom_H__ ----- */
