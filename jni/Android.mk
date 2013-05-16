# 必须位于Android.mk文件的最开始, 用来定位源文件的位置，$(call my-dir)的作用就是返回当前目录的路径
# 注意后面一定不能有空格，否则出错
LOCAL_PATH:=$(call my-dir)
include $(CLEAR_VARS) # 清除一些变量的值，LOCAL_PATH除外
LOCAL_MODULE:=myjni 	   # 指定当前待编译模块的名称
LOCAL_SRC_FILES:=myjni.c   # 指定参与编译的源代码文件
include $(BUILD_SHARED_LIBRARY) # 用来指示将当前模块编译为共享库，前缀为lib，后缀为.so; 还有另外一个BUILD_STATIC_LIBRARY，是用来指示将当前模块编译为静态库的，前缀为.a，后缀为.a

# 定义多个模块只需要在根目录放置一个Android.mk文件 include $(call all-subdir-makefiles) 包含所有子目录中的Android.mk文件
