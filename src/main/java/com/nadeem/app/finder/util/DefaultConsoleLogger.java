package com.nadeem.app.finder.util;

public class DefaultConsoleLogger implements LogListener {

	public void onLog(String message) {
		System.out.println(message);
	}
}
