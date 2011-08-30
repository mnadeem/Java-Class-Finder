package com.nadeem.app.finder.util;

public class DefaultOutputLogger implements OutputLogger {

	public void logResult(String message) {
		System.out.println(message);		
	}
}
