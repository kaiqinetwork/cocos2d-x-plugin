LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := PluginProtocolStatic

LOCAL_MODULE_FILENAME := libPluginProtocolStatic

LOCAL_SRC_FILES :=\
$(addprefix ../../platform/android/, \
	PluginFactory.cpp \
    PluginJniHelper.cpp \
    PluginUtils.cpp \
    PluginProtocol.cpp \
    ProtocolAnalytics.cpp \
    ProtocolIAP.cpp \
    ProtocolAds.cpp \
    ProtocolShare.cpp \
    ProtocolUser.cpp \
    ProtocolSocial.cpp \
    ProtocolCrash.cpp \
	ProtocolCustom.cpp\
	ProtocolService.cpp\
	ProtocolVoice.cpp\
    AgentManager.cpp \
) \
../../PluginManager.cpp \
../../PluginParam.cpp \
../../../luabindings/auto/lua_cocos2dx_pluginx_auto.cpp \
../../../luabindings/manual/lua_pluginx_basic_conversions.cpp \
../../../luabindings/manual/lua_pluginx_manual_callback.cpp \
../../../luabindings/manual/lua_pluginx_manual_protocols.cpp

LOCAL_CFLAGS := -std=c++11 -Wno-psabi
LOCAL_EXPORT_CFLAGS := -Wno-psabi

LOCAL_C_INCLUDES := $(LOCAL_PATH)/../../include \
	$(LOCAL_PATH)/../../platform/android \
	$(LOCAL_PATH)/../../../luabindings/auto \
	$(LOCAL_PATH)/../../../luabindings/manual \
	$(K7GAME_PROJECT_ROOT)/Game/include-android \
	$(KAIQI_PROJECT_ROOT)/include-android \
	$(KAIQI_PROJECT_ROOT)/include-android/cocos2dx-3.x \
	$(KAIQI_PROJECT_ROOT)/include-android/cocos2dx-3.x/cocos \
	$(KAIQI_PROJECT_ROOT)/include-android/tolua++ \
	$(KAIQI_PROJECT_ROOT)/include-android/luajit \
	$(KAIQI_PROJECT_ROOT)/include-android/cocos2dx-3.x/cocos/scripting/lua-bindings/manual
LOCAL_EXPORT_C_INCLUDES := $(LOCAL_PATH)/../../include $(LOCAL_PATH)/../../platform/android

LOCAL_LDLIBS := -landroid
LOCAL_LDLIBS += -llog
LOCAL_STATIC_LIBRARIES := android_native_app_glue

include $(BUILD_STATIC_LIBRARY)

$(call import-module,android/native_app_glue)
