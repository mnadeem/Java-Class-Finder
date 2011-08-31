package com.nadeem.app.finder.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class FileTypeTest {

	@Test
	public void itIsAZipFile() {
		assertTrue(FileType.isZipFile("xfz.zip"));
	}
	
	@Test
	public void itIsNotAZipFile() {
		assertFalse(FileType.isZipFile("xfz.cip"));
	}
	
	@Test
	public void itIsAJarFile() {
		assertTrue(FileType.isJavaArchiveFile("xfz.jar"));
	}
	
	@Test
	public void itIsAWarFile() {
		assertTrue(FileType.isJavaArchiveFile("xfz.War"));
	}
	
	@Test
	public void itIsAEarFile() {
		assertTrue(FileType.isJavaArchiveFile("xfz.eaR"));
	}
}
