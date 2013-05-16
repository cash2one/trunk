package com.shandagames.android.log;

public class Log {

	// set the LEVEL with VERBOSE value for inner debug version
	// set the LEVEL with WARN value to disable debug message for release version
	private static int LEVEL = android.util.Log.VERBOSE;

    /** if debug is true, the app used debug version ; otherwise release version */	
	static public void setLevel(boolean debug) {
		if (debug) {
			LEVEL = android.util.Log.VERBOSE;
		} else {
			LEVEL = android.util.Log.WARN;
		}
	}
	
	// verbose log
	static public void v(String msgFormat) {
		if (LEVEL <= android.util.Log.VERBOSE) {
			StackTraceElement ste = new Throwable().getStackTrace()[1];
			String fileName = ste.getFileName();
			String traceInfo = ste.getClassName() + "::";
			traceInfo += ste.getMethodName();
			traceInfo += "@" + ste.getLineNumber() + ">>>";

			android.util.Log.v(fileName, traceInfo + msgFormat);
		}
	}

	static public void v(String tag, String msgFormat) {
		if (LEVEL <= android.util.Log.VERBOSE) {
			StackTraceElement ste = new Throwable().getStackTrace()[1];
			String traceInfo = ste.getClassName() + "::";
			traceInfo += ste.getMethodName();
			traceInfo += "@" + ste.getLineNumber() + ">>>";

			android.util.Log.v(tag, traceInfo + msgFormat);
		}
	}

	static public void v(String tag, String msgFormat, Throwable t) {
		if (LEVEL <= android.util.Log.VERBOSE) {
			android.util.Log.v(tag, msgFormat, t);
		}
	}

	
	// debug log
	static public void d(String msgFormat) {
		if (LEVEL <= android.util.Log.DEBUG) {
			StackTraceElement ste = new Throwable().getStackTrace()[1];
			String fileName = ste.getFileName();
			String traceInfo = ste.getClassName() + "::";
			traceInfo += ste.getMethodName();
			traceInfo += "@" + ste.getLineNumber() + ">>>";

			android.util.Log.d(fileName, traceInfo + msgFormat);
		}
	}

	static public void d(String tag, String msgFormat) {
		if (LEVEL <= android.util.Log.DEBUG) {
			StackTraceElement ste = new Throwable().getStackTrace()[1];
			String traceInfo = ste.getClassName() + "::";
			traceInfo += ste.getMethodName();
			traceInfo += "@" + ste.getLineNumber() + ">>>";
			
			android.util.Log.d(tag, traceInfo + msgFormat);
		}
	}

	static public void d(String tag, String msgFormat, Throwable t) {
		if (LEVEL <= android.util.Log.DEBUG) {
			android.util.Log.d(tag, msgFormat, t);
		}
	}

	// info log
	static public void i(String msgFormat) {
		if (LEVEL <= android.util.Log.INFO) {
			StackTraceElement ste = new Throwable().getStackTrace()[1];
			String fileName = ste.getFileName();
			String traceInfo = ste.getClassName() + "::";
			traceInfo += ste.getMethodName();
			traceInfo += "@" + ste.getLineNumber() + ">>>";

			android.util.Log.i(fileName, traceInfo + msgFormat);
		}
	}
	
	static public void i(String tag, String msgFormat) {
		if (LEVEL <= android.util.Log.INFO) {
			StackTraceElement ste = new Throwable().getStackTrace()[1];
			String traceInfo = ste.getClassName() + "::";
			traceInfo += ste.getMethodName();
			traceInfo += "@" + ste.getLineNumber() + ">>>";

			android.util.Log.i(tag, traceInfo + msgFormat);
		}
	}

	static public void i(String tag, String msgFormat, Throwable t) {
		if (LEVEL <= android.util.Log.INFO) {
			android.util.Log.i(tag, msgFormat, t);
		}
	}

	// Warning log
	static public void w(String msgFormat) {
		if (LEVEL <= android.util.Log.WARN) {
			StackTraceElement ste = new Throwable().getStackTrace()[1];
			String fileName = ste.getFileName();
			String traceInfo = ste.getClassName() + "::";
			traceInfo += ste.getMethodName();
			traceInfo += "@" + ste.getLineNumber() + ">>>";

			android.util.Log.w(fileName, traceInfo + msgFormat);
		}
	}
	
	static public void w(String tag, String msgFormat) {
		if (LEVEL <= android.util.Log.WARN) {
			StackTraceElement ste = new Throwable().getStackTrace()[1];
			String traceInfo = ste.getClassName() + "::";
			traceInfo += ste.getMethodName();
			traceInfo += "@" + ste.getLineNumber() + ">>>";

			android.util.Log.w(tag, traceInfo + msgFormat);
		}
	}

	static public void w(String tag, String msgFormat, Throwable t) {
		if (LEVEL <= android.util.Log.WARN) {
			android.util.Log.w(tag, msgFormat, t);
		}
	}

	// Error log
	static public void e(String msgFormat) {
		if (LEVEL <= android.util.Log.ERROR) {
			StackTraceElement ste = new Throwable().getStackTrace()[1];
			String fileName = ste.getFileName();
			String traceInfo = ste.getClassName() + "::";
			traceInfo += ste.getMethodName();
			traceInfo += "@" + ste.getLineNumber() + ">>>";

			android.util.Log.e(fileName, traceInfo + msgFormat);
		}
	}
	
	static public void e(String tag, String msgFormat) {
		if (LEVEL <= android.util.Log.ERROR) {
			StackTraceElement ste = new Throwable().getStackTrace()[1];
			String traceInfo = ste.getClassName() + "::";
			traceInfo += ste.getMethodName();
			traceInfo += "@" + ste.getLineNumber() + ">>>";

			android.util.Log.e(tag, traceInfo + msgFormat);
		}
	}

	static public void e(String tag, String msgFormat, Throwable t) {
		if (LEVEL <= android.util.Log.ERROR) {
			android.util.Log.d(tag, msgFormat, t);
		}
	}

	
	// verbose log
	static public void v(String tag, String msgFormat, Object... args) {
		if (LEVEL <= android.util.Log.VERBOSE) {
			android.util.Log.v(tag, String.format(msgFormat, args));
		}
	}

	static public void v(String tag, Throwable t, String msgFormat,
			Object... args) {
		if (LEVEL <= android.util.Log.VERBOSE) {
			android.util.Log.v(tag, String.format(msgFormat, args), t);
		}
	}

	// debug log
	static public void d(String tag, String msgFormat, Object... args) {
		if (LEVEL <= android.util.Log.DEBUG) {
			android.util.Log.d(tag, String.format(msgFormat, args));
		}
	}

	static public void d(String tag, Throwable t, String msgFormat,
			Object... args) {
		if (LEVEL <= android.util.Log.DEBUG) {
			android.util.Log.d(tag, String.format(msgFormat, args), t);
		}
	}

	// info log
	static public void i(String tag, String msgFormat, Object... args) {
		if (LEVEL <= android.util.Log.INFO) {
			android.util.Log.i(tag, String.format(msgFormat, args));
		}
	}

	static public void i(String tag, Throwable t, String msgFormat,
			Object... args) {
		if (LEVEL <= android.util.Log.INFO) {
			android.util.Log.i(tag, String.format(msgFormat, args), t);
		}
	}

	// Warning log
	static public void w(String tag, String msgFormat, Object... args) {
		if (LEVEL <= android.util.Log.WARN) {
			android.util.Log.w(tag, String.format(msgFormat, args));
		}
	}

	static public void w(String tag, Throwable t, String msgFormat,
			Object... args) {
		if (LEVEL <= android.util.Log.WARN) {
			android.util.Log.w(tag, String.format(msgFormat, args), t);
		}
	}

	// Error log
	static public void e(String tag, String msgFormat, Object... args) {
		if (LEVEL <= android.util.Log.ERROR) {
			android.util.Log.e(tag, String.format(msgFormat, args));
		}
	}

	static public void e(String tag, Throwable t, String msgFormat,
			Object... args) {
		if (LEVEL <= android.util.Log.ERROR) {
			android.util.Log.e(tag, String.format(msgFormat, args), t);
		}
	}

}
