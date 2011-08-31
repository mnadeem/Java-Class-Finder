package com.nadeem.app.finder.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class ResultTypeTest {

	@Test
	public void shouldReturnAppendedMEssage() {
		assertEquals(ResultType.ARCHIVE.toString() + " <> " + "hello", ResultType.ARCHIVE.buildMessage("hello"));
	}

}
