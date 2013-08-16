LOCAL_PATH:=$(call all-subdir-makefiles)	#编译时的目录    

include $(CLEAR_VARS) 		#清除之前的一些系统变量
LOCAL_MODULE:=myjni			#编译的目标对象
LOCAL_SRC_FILES:=myjni.c	#编译的源文件
include $(BUILD_SHARED_LIBRARY)  #指明要编译成动态库, BUILD_STATIC_LIBRARY － 指明要编译成静态库

include $(CLEAR_VARS)
LOCAL_MODULE:=NativeSample01
LOCAL_SRC_FILES:=NativeSample01.c
LOCAL_LDLIBS += -llog   #增加 log 函数对应的log 库  liblog.so  libthread_db.a
include $(BUILD_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE:=NativeSample02
LOCAL_SRC_FILES:=NativeSample02.c
LOCAL_LDLIBS += -llog
include $(BUILD_SHARED_LIBRARY)