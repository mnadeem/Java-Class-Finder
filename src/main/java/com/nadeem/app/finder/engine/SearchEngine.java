package com.nadeem.app.finder.engine;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import com.nadeem.app.finder.util.FileType;
import com.nadeem.app.finder.util.OutputLogger;
import com.nadeem.app.finder.util.ResultType;
import com.nadeem.app.finder.util.SearchAbortedException;

public class SearchEngine {

	private OutputLogger outputLogger;
	private Boolean abortSearch 		= Boolean.FALSE;

	public SearchEngine(OutputLogger outputLogger) {
		this.outputLogger = outputLogger;
	}

	public void searchForClass(String path, String className) {

		File searchPath = searchPath(path);
		if (!searchPath.exists()) {
			outputLogger.logResult(ResultType.INVALID.buildMessage(path));	
		} else if(FileType.isCompressedFile(searchPath.getName())) {
			recursivelySearchInArchiveFile(searchPath, className);
		} else {
			doSearchForClassInDirectory(searchPath, className);
		}
	}

	public void searchForClass(Set<String> paths, String className) {
		for (String path : paths) {
			searchForClass(path, className);
		}
	}

	private void doSearchForClassInDirectory(File searchPath, String className) {

		try {
			File[] allFiles = searchPath.listFiles();
			if (allFiles == null || allFiles.length == 0) {
				return ;
			}
			for (File currentFile : allFiles) {
				throwExceptionIfAborted();
				if (FileType.isCompressedFile(currentFile.getName())) {
					recursivelySearchInArchiveFile(currentFile, className);
				} else if (currentFile.isDirectory()) {
					doSearchForClassInDirectory(currentFile, className);
				} else {
					logIfCurrentFileMatched(currentFile, className);
				}
			}
		} catch (SearchAbortedException e) {
			outputLogger.logResult(ResultType.ABORTED.toString());
		}
	}

	private void throwExceptionIfAborted() {
		if (abortSearch) {
			throw new SearchAbortedException();
		}
	}


	private void logIfCurrentFileMatched(File fileSystem, String className) {
		if (getSimpleFileName(fileSystem.getName()).equalsIgnoreCase(className)) {
		      this.outputLogger.logResult(ResultType.DIRECTORY.buildMessage(className + " Found in : " +  fileSystem.getAbsolutePath()));
		}
	}

	private String getSimpleFileName(String fileName) {
		int dot = lastIndexOfDot(fileName);
	    return fileName.substring(0, dot);
	}

	private String getSimpleZipFileName(String zipEntryName) {
		int dot 	= lastIndexOfDot(zipEntryName);
		int slash 	= lastIndexOfSlash(zipEntryName);
	    return zipEntryName.substring(slash + 1, dot);
	}

	private int lastIndexOfSlash(String zipEntryName) {
		return zipEntryName.lastIndexOf('/');
	}

	private int lastIndexOfDot(String fileName) {
		int dot = fileName.lastIndexOf('.');
		 dot = (dot == -1) ? fileName.length() : dot;
		return dot;
	}

	private void recursivelySearchInArchiveFile(File currentFile, String className) {
		
		try {
			ZipFile archiveFile = newZipFile(currentFile);
			for (ZipEntry zipEntry : Collections.list(archiveFile.entries())) {
				throwExceptionIfAborted();

				//TODO : Add support to read from a archive file which is inside a archive file.

				if (!zipEntry.getName().endsWith("/") && getSimpleZipFileName(zipEntry.getName()).equalsIgnoreCase(className)) {
				      this.outputLogger.logResult(ResultType.ARCHIVE.buildMessage(zipEntry.getName() + " Found in : " +  currentFile.getAbsolutePath()));
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
