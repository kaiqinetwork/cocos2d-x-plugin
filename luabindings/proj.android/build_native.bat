cd /d %~dp0

%NDK_ROOT%/ndk-build.cmd NDK_DEBUG=1 NDK_MODULE_PATH=${KAIQI_PROJECT_ROOT}/include-android
pause