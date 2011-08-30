package com.nadeem.app.engine;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import com.nadeem.app.finder.engine.SearchEngine;
import com.nadeem.app.finder.util.OutputLogger;
import com.nadeem.app.finder.util.ResultType;

public class SearchEngineTest {
	
	private static final String SOME_PATH 	= "somePath";
	private static final String SOME_CLASS  = "SOMECLASS";
	private static final String ARCHIVE_FIE = "archiveFile.zip";

	@Mock
	private OutputLogger mockedLogger;
	@Mock
	private File mockedFile;
	@Mock
	private File nextFile;

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
		
		verify(mockedLogger, times(1)).logResult(anyString());
	}
	
	@Test
	public void shouldLogAbortedMessage() throws Exception {
		when(mockedFile.exists()).thenReturn(Boolean.TRUE);
		ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
		targetBeingTested.abortSearch();
		
		targetBeingTested.searchForClass(SOME_PATH, SOME_CLASS);
		
		verify(mockedLogger).logResult(argumentCaptor.capture());		
		assertEquals(ResultType.ABORTED.toString(), argumentCaptor.getValue());
	}
	
	@Test
	public void shouldLogInvalidFilePathMessage() throws Exception {
		when(mockedFile.exists()).thenReturn(Boolean.FALSE);
		ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
		
		targetBeingTested.searchForClass(SOME_PATH, SOME_CLASS);
		
		verify(mockedLogger).logResult(argumentCaptor.capture());		
		assertEquals(ResultType.INVALID.buildMessage(SOME_PATH), argumentCaptor.getValue());
	}
		
	@Test
	public void shouldLogToLoggerWhenClassFoundInTheDirectory () throws Exception {
		when(mockedFile.exists()).thenReturn(Boolean.TRUE);
		ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
		when(mockedFile.listFiles()).thenReturn(new File[] {new File(SOME_CLASS)});
		
		targetBeingTested.searchForClass(SOME_PATH, SOME_CLASS);
		
		verify(mockedLogger).logResult(argumentCaptor.capture());
		assertEquals(ResultType.DIRECTORY.buildMessage(new File(SOME_CLASS).getAbsolutePath()), argumentCaptor.getValue());
	}
	
	@Test
	public void shouldSearchForFileInArchive () throws Exception {
		when(mockedFile.exists()).thenReturn(Boolean.TRUE);
		when(mockedFile.listFiles()).thenReturn(new File[] {nextFile});
		when(nextFile.getName()).thenReturn(ARCHIVE_FIE);
		
		targetBeingTested.searchForClass(SOME_PATH, SOME_CLASS);
		
		verify(nextFile).getName();
		
	}
	
	@Test
	public void shouldSearchForFilesRecursivelly() throws Exception {
		when(mockedFile.exists()).thenReturn(Boolean.TRUE);		
		when(mockedFile.listFiles()).thenReturn(new File[] {nextFile});
		when(nextFile.getName()).thenReturn(SOME_CLASS);
		when(nextFile.isDirectory()).thenReturn(Boolean.TRUE);		
		when(nextFile.listFiles()).thenReturn(null);
		
		targetBeingTested.searchForClass(SOME_PATH, SOME_CLASS);
		
		verify(nextFile).isDirectory();
		
	}
	
	private class MockedSearchEngine extends SearchEngine {

		public MockedSearchEngine(OutputLogger outputLogger) {
			super(outputLogger);
		}

		@Override
		protected File searchPath(String path) {
			return mockedFile;
		}
	}

}
