/** @file ProtocolCrash.h
 */
#ifndef __CCX_PROTOCOL_CRASH_H__
#define __CCX_PROTOCOL_CRASH_H__

#include "PluginProtocol.h"
#include <map>
#include <string>

namespace cocos2d { namespace plugin {

class ProtocolCrash : public PluginProtocol
{
public:
	ProtocolCrash();
	virtual ~ProtocolCrash();

	/**
	 *  set user identifier
	 *
	 *  @param userInfo
	 */
	virtual void setUserIdentifier(const char* identifier);

	/**
	 *  The uploader captured in exception information
	 *
	 *	@param message   Set the custom information
	 *  @param exception  The exception
	 */
	virtual void reportException(const char* message, const char* exception);

	/**
	 *  customize logging
	 *
	 *  @param string Custom log
	 */
	virtual void leaveBreadcrumb(const char* breadcrumb);
	
};

}} // namespace cocos2d { namespace plugin {

#endif /* __CCX_PROTOCOL_CRASH_H__ */
