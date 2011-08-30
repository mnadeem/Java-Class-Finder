package com.nadeem.app.finder.engine;

import java.io.File;
import java.util.Set;

import com.nadeem.app.finder.util.OutputLogger;
import com.nadeem.app.finder.util.ResultType;

public class SearchEngine {

	private OutputLogger outputLogger;
	private Boolean abortSearch 		= Boolean.FALSE;
	
	public SearchEngine(OutputLogger outputLogger) {
		this.outputLogger = outputLogger;
	}
	
	public void searchForClass(String path, String className) {
		
		File searchPath = searchPath(path);
		if (searchPath.exists()) {
			doSearchForClassInDirectory(searchPath, className);
		} else {
			outputLogger.logResult(ResultType.INVALID.buildMessage(path));
		}
	}
	
	public void searchForClass(Set<String> paths, String className) {
		for (String path : paths) {
			searchForClass(path, className);
		}	
	}

	private void doSearchForClassInDirectory(File searchPath, String className) {
		
		if (abortSearch) {
			outputLogger.logResult(ResultType.ABORTED.toString());
			return; 
		}

		File[] allFiles = searchPath.listFiles();
		if (allFiles == null || allFiles.length == 0) {			
			return ;
		}
		for (File currentFile : allFiles) {
			if (isArchiveFile(currentFile)) {
				recursivelySearchInArchiveFile(currentFile, className);
			} else if (currentFile.isDirectory()) {
				doSearchForClassInDirectory(currentFile, className);
			} else {
				doSearchInFileSystem(currentFile, className);
			}
		}				
	}


	private void doSearchInFileSystem(File fileSystem, String className) {		
		if (abortSearch) {
			outputLogger.logResult(ResultType.ABORTED.toString());
			return; 
		}
		
	    if (getSimpleFileName(fileSystem).equalsIgnoreCase(className)) {
	      this.outputLogger.logResult(ResultType.DIRECTORY.buildMessage(fileSystem.getAbsolutePath()));	     
	    }		
	}
	
	private String getSimpleFileName(File file) {
		int dot = lastIndexOfDot(file);
	    return file.getName().substring(0, dot);
	}

	private int lastIndexOfDot(File file) {
		int dot = file.getName().lastIndexOf('.');
		 dot = (dot == -1 ) ? file.getName().length() : dot;
		return dot;
	}

	private void recursivelySearchInArchiveFile(File archiveFile, String className) {
		if (abortSearch) {
			outputLogger.logResult(ResultType.ABORTED.toString());
			return; 
		}
		//TODO: ADD
		
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
