package com.nadeem.app.finder.engine;

import static com.nadeem.app.finder.util.FileType.isCompressedFile;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import com.nadeem.app.finder.listener.LogListener;
import com.nadeem.app.finder.listener.MatchCountChangeListener;
import com.nadeem.app.finder.modal.SearchCriteria;
import com.nadeem.app.finder.util.ResultType;
import com.nadeem.app.finder.util.SearchAbortedException;

public class SearchEngine {

	private static char FILE_SEPERATOR 		= System.getProperty("file.separator").toCharArray()[0];
	static private final int BUFFER_SIZE 	= 4096;

	private Boolean abortSearch 			= Boolean.FALSE;
	private LogListener logListener;
	private MatchCountChangeListener countListener;
	
	private int matchCount;

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
		matchCount = 0;
		for (String path : criteria.getPaths()) {
			File searchPath = searchPath(path);
			if (!searchPath.exists()) {
				logListener.onLog(ResultType.INVALID.buildMessage(path));
			} else if (isCompressedFile(searchPath.getName())) {
				searchInArchiveFile(searchPath, criteria);
			} else if (searchPath.isDirectory()) {
				recursivelySearchInDirectory(searchPath, criteria);
			} else {
				logIfCurrentFileMatched(searchPath, criteria);
			}
		}
		if (countListener == null) {
			logListener.onLog("Total Matchs = " + matchCount);
		}
		onFinish();
	}

	protected void onFinish() {
		
	}

	protected File searchPath(String path) {
		return new File(path);
	}

	private void searchInArchiveFile(File searchPath, SearchCriteria criteria) {
		if (criteria.deepSearch()) {
			recursivelySearchInArchiveFile(searchPath, criteria);
		} else {
			nonRecursivelySearchInArchiveFile(searchPath, criteria);
		}
	}

	private void recursivelySearchInArchiveFile(File searchPath, SearchCriteria criteria) {
		ZipFile archiveFile	= null;
		ZipInputStream zipInputStream = null;
		try {

			archiveFile = new ZipFile(searchPath);
			for (ZipEntry zipEntry : Collections.list(archiveFile.entries())) {
				throwExceptionIfAborted();
				if (!zipEntry.isDirectory() && !zipEntry.getName().endsWith(String.valueOf(FILE_SEPERATOR)) && getSimpleZipFileName(zipEntry.getName()).equalsIgnoreCase(criteria.getFileName())) {
				      this.logListener.onLog(ResultType.ARCHIVE.buildMessage(zipEntry.getName() + " Found in : " +  searchPath.getAbsolutePath()));
				      updateMatchCountListener();
				} else if (isCompressedFile(zipEntry.getName())) {
					zipInputStream = new ZipInputStream(archiveFile.getInputStream(zipEntry));
					recursiveSearchInArchiveFile(zipEntry.getName(), zipInputStream, searchPath, criteria);
				}

			}

		} catch (ZipException e) {

		} catch (IOException e) {

		} finally {
			closeQuietly(archiveFile);
			closeQuietly(zipInputStream);
		}
	}

	private void updateMatchCountListener() {
		matchCount ++;
		if (countListener != null) {
			countListener.onCountChange(matchCount);
		}
		
	}

	private void recursiveSearchInArchiveFile (String zipEntryName, ZipInputStream zipInputStream, File searchPath, SearchCriteria criteria) {

		while (true) {
			ZipInputStream localZipInputStream = null;
			try {
				ZipEntry localZipEntry = zipInputStream.getNextEntry();

				if (localZipEntry == null) {
					return;
				}

				if (!localZipEntry.isDirectory() && !localZipEntry.getName().endsWith(String.valueOf(FILE_SEPERATOR)) && getSimpleZipFileName(localZipEntry.getName()).equalsIgnoreCase(criteria.getFileName())) {
				      this.logListener.onLog(ResultType.ARCHIVE.buildMessage(zipEntryName + "!/" + localZipEntry.getName() + " Found in : " +  searchPath.getAbsolutePath()));
				      updateMatchCountListener();
				} else if (isCompressedFile(localZipEntry.getName())) {

					localZipInputStream = getZipInputStream(zipInputStream, localZipEntry);
					recursiveSearchInArchiveFile(zipEntryName + "!/" + localZipEntry.getName(),localZipInputStream, searchPath, criteria);
				}
			} catch (Exception e) {

			} finally {	
				closeQuietly(localZipInputStream);
			}
		}

	}

	private ZipInputStream getZipInputStream (ZipInputStream zipInputStream, ZipEntry zipEntry) throws IOException {
		int count;
		byte data[] 	= new byte[BUFFER_SIZE];
		int remaining 	= (int) zipEntry.getSize();
		ByteArrayOutputStream outputStream 			= new ByteArrayOutputStream();
		BufferedOutputStream bufferedOutputStream 	= new BufferedOutputStream(outputStream, BUFFER_SIZE);

		while (remaining > 0 && (count = zipInputStream.read(data, 0, Math.min(BUFFER_SIZE, remaining))) != -1) {
	        bufferedOutputStream.write(data, 0, count);
	        remaining -= count;
	    }
		return new ZipInputStream(new ByteArrayInputStream(outputStream.toByteArray()));
	}

	private void nonRecursivelySearchInArchiveFile(File currentFile, SearchCriteria criteria) {
		ZipFile archiveFile	= null;
		try {
			archiveFile = newZipFile(currentFile);
			for (ZipEntry zipEntry : Collections.list(archiveFile.entries())) {
				throwExceptionIfAborted();

				if (!zipEntry.getName().endsWith(String.valueOf(FILE_SEPERATOR)) && getSimpleZipFileName(zipEntry.getName()).equalsIgnoreCase(criteria.getFileName())) {
				      this.logListener.onLog(ResultType.ARCHIVE.buildMessage(zipEntry.getName() + " Found in : " +  currentFile.getAbsolutePath()));
				      updateMatchCountListener();
				}

			}
		} catch (ZipException e) {

		} catch (IOException e) {

		} finally {
			closeQuietly(archiveFile);

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
				if (isCompressedFile(currentFile.getName())) {
					searchInArchiveFile(currentFile, criteria);
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
		      updateMatchCountListener();
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
		return zipEntryName.replace('/', FILE_SEPERATOR).lastIndexOf(FILE_SEPERATOR);
	}

	private int lastIndexOfDot(String fileName, int fromIndex) {
		int dot = fileName.indexOf('.', fromIndex);
		 dot = (dot == -1) ? fileName.length() : dot;
		return dot;
	}

	private void closeQuietly(ZipFile archiveFile) {
		if (archiveFile != null) {
			try {
				archiveFile.close();
			} catch (Exception e) {

			}
		}
	}

	private void closeQuietly(Closeable closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (Exception e) {
				// Ignore
			}
		}
	}

	protected ZipFile newZipFile(File currentFile) throws ZipException, IOException {
		return new ZipFile(currentFile);
	}

	public void abortSearch() {
		this.abortSearch = Boolean.TRUE;
	}
	public void clearAbortStatus() {
		this.abortSearch = Boolean.FALSE;
	}

	public void setCountListener(MatchCountChangeListener countListener) {
		this.countListener = countListener;
	}	
}
