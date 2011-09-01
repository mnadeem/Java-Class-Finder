package com.nadeem.app.finder.engine;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import com.nadeem.app.finder.modal.SearchCriteria;
import com.nadeem.app.finder.util.FileType;
import com.nadeem.app.finder.util.LogListener;
import com.nadeem.app.finder.util.ResultType;
import com.nadeem.app.finder.util.SearchAbortedException;

public class SearchEngine {
	
	private static char FILE_SEPERATOR 	= System.getProperty("file.separator").toCharArray()[0];

	private Boolean abortSearch 		= Boolean.FALSE;
	private LogListener logListener;

	public SearchEngine(LogListener logListener) {
		this.logListener = logListener;
	}

	public void searchForClass(Set<String> paths, String fileName) {
		searchForClass(new SearchCriteria(paths, fileName));
	}

	public void searchForClass(String path, String fileName) {
		searchForClass(new SearchCriteria(path, fileName));
	}
	
	public void searchForClass(final SearchCriteria criteria) {
		for (String path : criteria.getPaths()) {
			File searchPath = searchPath(path);
			if (!searchPath.exists()) {
				logListener.onLog(ResultType.INVALID.buildMessage(path));
			} else if (FileType.isCompressedFile(searchPath.getName())) {
				searchInArchiveFile(searchPath, criteria);
			} else if (searchPath.isDirectory()) {
				recursivelySearchInDirectory(searchPath, criteria);
			} else {
				logIfCurrentFileMatched(searchPath, criteria);
			}
		}
	}
	
	private void searchInArchiveFile(File searchPath, SearchCriteria criteria) {
		if (criteria.recursiveArchiveSearch()) {
			
		} else {
			nonRecursivelySearchInArchiveFile(searchPath, criteria);
		}
	}

	private void recursivelySearchInDirectory(File searchPath, SearchCriteria criteria) {

		try {
			File[] allFiles = searchPath.listFiles();
			if (allFiles == null || allFiles.length == 0) {
				return;
			}
			for (File currentFile : allFiles) {
				throwExceptionIfAborted();
				if (FileType.isCompressedFile(currentFile.getName())) {
					nonRecursivelySearchInArchiveFile(currentFile, criteria);
				} else if (currentFile.isDirectory()) {
					recursivelySearchInDirectory(currentFile, criteria);
				} else {
					logIfCurrentFileMatched(currentFile, criteria);
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


	private void logIfCurrentFileMatched(File fileSystem, SearchCriteria criteria) {
		if (getSimpleFileName(fileSystem.getName()).equalsIgnoreCase(criteria.getFileName())) {
		      this.logListener.onLog(ResultType.DIRECTORY.buildMessage(criteria.getFileName() + " Found in : " +  fileSystem.getAbsolutePath()));
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

	private void nonRecursivelySearchInArchiveFile(File currentFile, SearchCriteria criteria) {
		ZipFile archiveFile	= null;
		try {
			archiveFile = newZipFile(currentFile);
			for (ZipEntry zipEntry : Collections.list(archiveFile.entries())) {
				throwExceptionIfAborted();
				
				if (!zipEntry.getName().endsWith(String.valueOf(FILE_SEPERATOR)) && getSimpleZipFileName(zipEntry.getName()).equalsIgnoreCase(criteria.getFileName())) {
				      this.logListener.onLog(ResultType.ARCHIVE.buildMessage(zipEntry.getName() + " Found in : " +  currentFile.getAbsolutePath()));
				}
				
			}
		} catch (ZipException e) {
			// Ignore
		} catch (IOException e) {
			// Ignore
		} finally {
			closeQuietly(archiveFile);
		}
	}

	private void closeQuietly(ZipFile archiveFile) {
		if (archiveFile != null) {
			try {
				archiveFile.close();
			} catch (Exception e) {
				// Ignore
			}
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