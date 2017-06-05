LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := LuaPluginProtocolStatic

LOCAL_MODULE_FILENAME := libLuaPluginProtocolStatic

LOCAL_SRC_FILES :=\
../../auto/lua_cocos2dx_pluginx_auto.cpp \
../../manual/lua_pluginx_basic_conversions.cpp \
../../manual/lua_pluginx_manual_callback.cpp \
../../manual/lua_pluginx_manual_protocols.cpp

LOCAL_CFLAGS := -std=c++11 -Wno-psabi
LOCAL_EXPORT_CFLAGS := -Wno-psabi

LOCAL_C_INCLUDES := $(LOCAL_PATH)/../../auto \
	$(LOCAL_PATH)/../../manual \
	$(KAIQI_PROJECT_ROOT)/include-android \
	$(KAIQI_PROJECT_ROOT)/include-android/cocos2dx-3.x \
	$(KAIQI_PROJECT_ROOT)/include-android/cocos2dx-3.x/cocos \
	$(KAIQI_PROJECT_ROOT)/include-android/tolua++ \
	$(KAIQI_PROJECT_ROOT)/include-android/luajit \
	$(KAIQI_PROJECT_ROOT)/include-android/cocos2dx-3.x/cocos/scripting/lua-bindings/manual \
	$(KAIQI_PROJECT_ROOT)/include-android/cocos2dx-3.x/plugin/protocols_gnustl_static
LOCAL_EXPORT_C_INCLUDES := $(LOCAL_PATH)/../../auto \
						   $(LOCAL_PATH)/../../manual

LOCAL_LDLIBS := -landroid
LOCAL_LDLIBS += -llog
LOCAL_STATIC_LIBRARIES := android_native_app_glue

include $(BUILD_STATIC_LIBRARY)

$(call import-module,android/native_app_glue)
