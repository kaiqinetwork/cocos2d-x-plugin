cd /d %~dp0

xcopy obj\local\armeabi\*.a  %KAIQI_PROJECT_ROOT%\lib-android\armeabi\cocos2dx-3.x  /y /i
xcopy ..\auto\*.hpp %KAIQI_PROJECT_ROOT%\include-android\cocos2dx-3.x\plugin\luabindings\auto  /y /i /s 
xcopy ..\manual\*.h %KAIQI_PROJECT_ROOT%\include-android\cocos2dx-3.x\plugin\luabindings\manual  /y /i /s 
:xcopy prebuilt-mk\Android.mk %KAIQI_PROJECT_ROOT%\include-android\cocos2dx-3.x\plugin\protocols_gnustl_static\  /y /i


