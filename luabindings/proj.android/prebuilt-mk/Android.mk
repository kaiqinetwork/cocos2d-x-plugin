LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE := LuaPluginProtocolStatic
LOCAL_MODULE_FILENAME := libPluginProtocolStatic
LOCAL_SRC_FILES := ../../../../lib-android/$(TARGET_ARCH_ABI)/cocos2dx-3.x/libLuaPluginProtocolStatic.a
LOCAL_EXPORT_C_INCLUDES := $(LOCAL_PATH)
LOCAL_EXPORT_LDLIBS += -llog
LOCAL_EXPORT_LDLIBS += -lz

include $(PREBUILT_STATIC_LIBRARY)
