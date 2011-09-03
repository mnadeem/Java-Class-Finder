package com.nadeem.app.finder.listener.impl;

import com.nadeem.app.finder.listener.LogListener;

public class DefaultConsoleLogger implements LogListener {

	public void onLog(String message) {
		System.out.println(message);
	}
}
