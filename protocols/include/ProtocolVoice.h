/** @file ProtocolVoice.h
 */
#ifndef  __CCX_PROTOCOL_Voice_H__
#define  __CCX_PROTOCOL_Voice_H__

#include "PluginProtocol.h"
#include <map>
#include <string>
#include <functional>

namespace cocos2d {
	namespace plugin {
typedef enum 
{
	kVoiceInitSuccess = 0,/**< enum value is callback of succeeding in initing sdk. */
	kVoiceInitFail,/**< enum  value is callback of failing to init sdk. */
	kVoiceLoginSuccess,
	kVoiceLoginFail,
	kVoiceReconnectSuccess,
	kVoiceRecordError,
	kVoiceUploadSuccess,
	kVoiceUploadFail,
	kVoicePlaySuccess,
	kVoicePlayFail,
    kVoiceExtension = 90000 /**< enum value is  extension code . */
} VoiceResultCode;

/**   
 *@class  ProtocolVoice
 *@brief the interface of Voice  
 */
class ProtocolVoice : public PluginProtocol
{
public:

	typedef std::function<void(int, std::string&)> ProtocolVoiceCallback;

    /**
    @breif set the result listener
    @param pListener The callback object for Voice result
    @wraning Must invoke this interface before Voice
    */

	ProtocolVoice();
	virtual ~ProtocolVoice();

	void setUserInfo(std::map<std::string, std::string> userInfo);
	void startRecord();
	void stopRecord();
	void cancelRecord();
	void exit();
	void start();
	std::string getVoiceUrl();
	void playVoice(std::string strUrl);

	inline void setCallback(const ProtocolVoiceCallback &cb)
	{
		_callback = cb;
	}

	inline ProtocolVoiceCallback& getCallback()
	{
		return _callback;
	}

protected:
	ProtocolVoiceCallback _callback;

};

}} // namespace anysdk { namespace framework {

#endif   /* ----- #ifndef __CCX_PROTOCOL_Voice_H__ ----- */
