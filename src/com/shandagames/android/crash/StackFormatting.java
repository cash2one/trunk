package com.shandagames.android.crash;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

public class StackFormatting {
	
	public static String getStackMessageString(Throwable e) {
		StringBuffer message = new StringBuffer();
		StackTraceElement[] stack = e.getStackTrace();
		StackTraceElement stackLine = stack[(stack.length - 1)];
		message.append(stackLine.getFileName());
		message.append(":");
		message.append(stackLine.getLineNumber());
		message.append(":");
		message.append(stackLine.getMethodName());
		message.append(" ");
		message.append(e.getMessage());
		return message.toString();
	}

	public static String getStackTraceString(Throwable tr) {
		if (tr == null) {
			return "";
		}
		
		try {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			tr.printStackTrace(pw);
			String error = sw.toString();
			sw.close();
			pw.close();
			return error;
		} catch (IOException e) {
			return e.getMessage();
		}
	}
}