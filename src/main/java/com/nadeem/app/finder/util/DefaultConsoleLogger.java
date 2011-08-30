package com.nadeem.app.finder.util;

public class DefaultConsoleLogger implements OutputLogger {

	public void logResult(String message) {
		System.out.println(message);		
	}
}
