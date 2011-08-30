package com.nadeem.app.finder.util;

public enum ResultType {
	
	ARCHIVE("Archive Match"), DIRECTORY("Directory Match"), INVALID("Invalid path"), ABORTED("Aborted !!!");
	
	private String name;
	
	private ResultType(String name) {
		this.name = name;
	}
	
	public String buildMessage(String message) {
		return name + " <> " + message;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
