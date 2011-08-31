package com.nadeem.app.finder.util;


public enum FileType {
	
	ZIP(".zip"), JAR(".jar"), WAR(".war"), EAR(".ear"), UNKNOWN("");
	
	private FileType(String extension) {
		this.extension = extension;
	}
	
	private final String extension;	
	
	public static Boolean isZipFile(final String fileName) {
		return fileName.toLowerCase().endsWith(ZIP.getExtension());
	}
	
	public static Boolean isJavaArchiveFile(final String fileName) {
		return fileName.toLowerCase().endsWith(JAR.getExtension())
						|| fileName.toLowerCase().endsWith(WAR.getExtension())
						|| fileName.toLowerCase().endsWith(EAR.getExtension());
	}

	public static Boolean isCompressedFile(final String fileName) {
		return isJavaArchiveFile(fileName) || isZipFile(fileName);
	}

	public String getExtension() {
		return extension;
	}
}
