package com.nadeem.app.finder.engine;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import com.nadeem.app.finder.util.FileType;
import com.nadeem.app.finder.util.LogListener;
import com.nadeem.app.finder.util.ResultType;
import com.nadeem.app.finder.util.SearchAbortedException;

public class SearchEngine {
	
	private static char FILE_SEPERATOR 	= System.getProperty("file.separator").toCharArray()[0];

	private Boolean abortSearch 		= Boolean.FALSE;
	private LogListener logListener;

	public SearchEngine(LogListener outputLogger) {
		this.logListener = outputLogger;
	}

	public void searchForClass(Set<String> paths, String className) {
		for (String path : paths) {
			searchForClass(path, className);
		}
	}

	public void searchForClass(String path, String className) {

		File searchPath = searchPath(path);
		if (!searchPath.exists()) {
			logListener.onLog(ResultType.INVALID.buildMessage(path));
		} else if (FileType.isCompressedFile(searchPath.getName())) {
			nonRecursivelySearchInArchiveFile(searchPath, className);
		} else if (searchPath.isDirectory()) {
			recursivelySearchInDirectory(searchPath, className);
		} else {
			logIfCurrentFileMatched(searchPath, className);
		}
	}


	private void recursivelySearchInDirectory(File searchPath, String className) {

		try {
			File[] allFiles = searchPath.listFiles();
			if (allFiles == null || allFiles.length == 0) {
				return;
			}
			for (File currentFile : allFiles) {
				throwExceptionIfAborted();
				if (FileType.isCompressedFile(currentFile.getName())) {
					nonRecursivelySearchInArchiveFile(currentFile, className);
				} else if (currentFile.isDirectory()) {
					recursivelySearchInDirectory(currentFile, className);
				} else {
					logIfCurrentFileMatched(currentFile, className);
				}
			}
		} catch (SearchAbortedException e) {
			logListener.onLog(ResultType.ABORTED.toString());
		}
	}

	private void throwExceptionIfAborted() {
		if (abortSearch) {
			throw new SearchAbortedException();
		}
	}


	private void logIfCurrentFileMatched(File fileSystem, String className) {
		if (getSimpleFileName(fileSystem.getName()).equalsIgnoreCase(className)) {
		      this.logListener.onLog(ResultType.DIRECTORY.buildMessage(className + " Found in : " +  fileSystem.getAbsolutePath()));
		}
	}

	private String getSimpleFileName(String fileName) {
		int dot = lastIndexOfDot(fileName, 0);
	    return fileName.substring(0, dot);
	}

	private String getSimpleZipFileName(String zipEntryName) {
		int slash 	= lastIndexOfSlash(zipEntryName);
		int dot 	= lastIndexOfDot(zipEntryName, slash);
	    return zipEntryName.substring(slash + 1, dot);
	}

	private int lastIndexOfSlash(String zipEntryName) {
		return zipEntryName.lastIndexOf(FILE_SEPERATOR);
	}

	private int lastIndexOfDot(String fileName, int fromIndex) {
		int dot = fileName.indexOf('.', fromIndex);
		 dot = (dot == -1) ? fileName.length() : dot;
		return dot;
	}

	private void nonRecursivelySearchInArchiveFile(File currentFile, String className) {

		try {
			ZipFile archiveFile = newZipFile(currentFile);
			for (ZipEntry zipEntry : Collections.list(archiveFile.entries())) {
				throwExceptionIfAborted();

				//TODO : Add support to read from a archive file which is inside a archive file.
				
				if (!zipEntry.getName().endsWith(String.valueOf(FILE_SEPERATOR)) && getSimpleZipFileName(zipEntry.getName()).equalsIgnoreCase(className)) {
				      this.logListener.onLog(ResultType.ARCHIVE.buildMessage(zipEntry.getName() + " Found in : " +  currentFile.getAbsolutePath()));
				}
				
			}
		} catch (ZipException e) {
			// Ignore
		} catch (IOException e) {
			// Ignore
		}
	}

	protected ZipFile newZipFile(File currentFile) throws ZipException, IOException {
		return new ZipFile(currentFile);
	}

	protected File searchPath(String path) {
		return new File(path);
	}

	public void abortSearch() {
		this.abortSearch = Boolean.TRUE;
	}

}