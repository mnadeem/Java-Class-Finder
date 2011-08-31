package com.nadeem.app.finder.util;

import java.util.Enumeration;

public class EmptyEnumeration<E> implements Enumeration<E> {

	public boolean hasMoreElements() {
		return false;
	}

	public E nextElement() {
		return null;
	}

}
