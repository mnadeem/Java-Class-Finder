package com.nadeem.app.finder.engine;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import com.nadeem.app.finder.listener.LogListener;
import com.nadeem.app.finder.util.EmptyEnumeration;
import com.nadeem.app.finder.util.ResultType;

public class SearchEngineTest {

	private static final String SOME_PATH 	= "somePath";
	private static final String SOME_CLASS  = "SOMECLASS";
	private static final String ARCHIVE_FIE = "archiveFile.zip";

	@Mock
	private LogListener mockedLogger;
	@Mock
	private File mockedFile;
	@Mock
	private File nextFile;
	@Mock
	private ZipFile mockedZipFile;

	private Set<String> paths;

	private SearchEngine targetBeingTested;

	@Before
	public void doBeforeEachTestCase() {
		initMocks(this);

		targetBeingTested 	=  new MockedSearchEngine(mockedLogger);
		paths	 			=  new HashSet<String>();
	}

	@Test
	public void shouldLogToLogger() {
		paths.add(SOME_PATH);

		targetBeingTested.searchForClass(paths, SOME_CLASS);

		verify(mockedLogger, times(2)).onLog(anyString());
	}
	
	@Test
	public void shouldLogInvalidFilePathMessage() throws Exception {
		when(mockedFile.exists()).thenReturn(Boolean.FALSE);
		ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);

		targetBeingTested.searchForClass(SOME_PATH, SOME_CLASS);

		verify(mockedLogger, times(2)).onLog(argumentCaptor.capture());
		assertEquals(ResultType.INVALID.buildMessage(SOME_PATH), argumentCaptor.getAllValues().get(0));
	}

	@Test
	public void shouldLogToLoggerWhenClassFoundInTheDirectory () throws Exception {
		when(mockedFile.exists()).thenReturn(Boolean.TRUE);
		when(mockedFile.isDirectory()).thenReturn(Boolean.TRUE);
		when(mockedFile.getName()).thenReturn(SOME_PATH);

		when(mockedFile.listFiles()).thenReturn(new File[] {new File(SOME_CLASS)});

		targetBeingTested.searchForClass(SOME_PATH, SOME_CLASS);

		ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
		verify(mockedLogger, times(2)).onLog(argumentCaptor.capture());
		assertEquals(ResultType.DIRECTORY.buildMessage(SOME_CLASS + " Found in : " +new File(SOME_CLASS).getAbsolutePath()), argumentCaptor.getAllValues().get(0));
	}
	
	@Test
	public void shouldLogIfCurrectFileIsBeingSearched() throws Exception {
		when(mockedFile.exists()).thenReturn(Boolean.TRUE);
		when(mockedFile.isDirectory()).thenReturn(Boolean.FALSE);
		when(mockedFile.getName()).thenReturn(SOME_CLASS);

		when(mockedFile.getAbsolutePath()).thenReturn(new File(SOME_CLASS).getAbsolutePath());

		targetBeingTested.searchForClass(SOME_PATH, SOME_CLASS);
		
		ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
		verify(mockedLogger, times(2)).onLog(argumentCaptor.capture());
		assertEquals(ResultType.DIRECTORY.buildMessage(SOME_CLASS + " Found in : " + new File(SOME_CLASS).getAbsolutePath()), argumentCaptor.getAllValues().get(0));

	}

	@Test
	public void shouldLogAbortedMessage() throws Exception {
		when(mockedFile.exists()).thenReturn(Boolean.TRUE);
		when(mockedFile.isDirectory()).thenReturn(Boolean.TRUE);
		when(mockedFile.getName()).thenReturn(SOME_PATH);

		when(mockedFile.listFiles()).thenReturn(new File[] {new File(SOME_CLASS)});
		
		targetBeingTested.abortSearch();

		targetBeingTested.searchForClass(SOME_PATH, SOME_CLASS);

		ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
		verify(mockedLogger, times(2)).onLog(argumentCaptor.capture());
		assertEquals(ResultType.ABORTED.toString(), argumentCaptor.getAllValues().get(0));
	}
	
	@Test
	public void shouldSearchForFilesRecursivelly() throws Exception {
		when(mockedFile.exists()).thenReturn(Boolean.TRUE);
		when(mockedFile.isDirectory()).thenReturn(Boolean.TRUE);
		when(mockedFile.getName()).thenReturn(SOME_PATH);

		when(mockedFile.listFiles()).thenReturn(new File[] {nextFile});
		when(nextFile.getName()).thenReturn(SOME_CLASS);
		when(nextFile.isDirectory()).thenReturn(Boolean.TRUE);
		when(nextFile.listFiles()).thenReturn(null);

		targetBeingTested.searchForClass(SOME_PATH, SOME_CLASS);

		verify(nextFile).isDirectory();
	}


	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void shouldSearchForFileInArchive () throws Exception {
		when(mockedFile.exists()).thenReturn(Boolean.TRUE);
		when(mockedFile.isDirectory()).thenReturn(Boolean.TRUE);
		when(mockedFile.getName()).thenReturn(SOME_PATH);

		when(mockedFile.listFiles()).thenReturn(new File[] {nextFile});
		when(nextFile.getName()).thenReturn(ARCHIVE_FIE);
		when(mockedZipFile.entries()).thenReturn(new EmptyEnumeration());

		targetBeingTested.searchForClass(SOME_PATH, SOME_CLASS);

		verify(mockedZipFile).entries();

	}

	private class MockedSearchEngine extends SearchEngine {

		public MockedSearchEngine(LogListener outputLogger) {
			super(outputLogger);
		}

		@Override
		protected File searchPath(String path) {
			return mockedFile;
		}
		@Override
		protected ZipFile newZipFile(File currentFile) throws ZipException,IOException {
			return mockedZipFile;
		}
	}
}
