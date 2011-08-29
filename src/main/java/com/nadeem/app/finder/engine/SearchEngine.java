package com.nadeem.app.finder.engine;

import java.io.File;
import java.util.Collections;
import java.util.Set;

import com.nadeem.app.finder.util.OutputLogger;
import com.nadeem.app.finder.util.ResultType;

public class SearchEngine {

	private OutputLogger outputLogger;
	private Boolean abortSearch = Boolean.FALSE;
	
	public SearchEngine(OutputLogger outputLogger) {
		this.outputLogger = outputLogger;
	}
	
	public void searchForClass(String path, String className) {
		this.searchForClass(Collections.singleton(path), className);
	}
	
	public void searchForClass(Set<String> paths, String className) {
		for (String path : paths) {
			
			if (abortSearch) {
				outputLogger.logResult(ResultType.ABORTED.toString());
				return; 
			}
			searchClassInPath(path, className);			
		}	
	}

	private void searchClassInPath(String path, String className) {		
		File searchPath = searchPath(path);
		if (searchPath.exists()) {
			doSearchForClass(searchPath, className);
		} else {
			outputLogger.logResult(ResultType.INVALID.buildMessage(path));
		}
	}

	private void doSearchForClass(File searchPath, String className) {
		File[] allFiles = searchPath.listFiles();
		if (allFiles == null || allFiles.length == 0) {
			outputLogger.logResult(ResultType.INVALID.toString());
			return ;
		}
		for (File currentFile : allFiles) {
			if (isArchiveFile(currentFile)) {
				recursivelySearchInArchiveFile(currentFile, className);
			} else {
				doSearchInFileSystem(currentFile, className);
			}
		}				
	}


	private void doSearchInFileSystem(File fileSystem, String className) {
		// TODO Auto-generated method stub
		
	}

	private void recursivelySearchInArchiveFile(File archiveFile, String className) {
		// TODO Auto-generated method stub
		
	}

	private boolean isArchiveFile(File currentFile) {
		return isZipFile(currentFile) || isJavaArchiveFile(currentFile);
	}

	private boolean isJavaArchiveFile(File currentFile) {
		return isFileOfType(currentFile, ".war") || isFileOfType(currentFile, ".jar") || isFileOfType(currentFile, ".ear");
	}

	private boolean isZipFile(File currentFile) {
		return isFileOfType(currentFile, ".zip");
	}

	private boolean isFileOfType(File currentFile, String extension) {
		return currentFile.getName().toLowerCase().endsWith(extension);
	}

	protected File searchPath(String path) {
		return new File(path);
	}

	public void abortSearch() {
		this.abortSearch = Boolean.TRUE;
	}	

}
