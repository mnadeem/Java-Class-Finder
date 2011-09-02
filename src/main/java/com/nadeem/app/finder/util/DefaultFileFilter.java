package com.nadeem.app.finder.util;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class DefaultFileFilter extends FileFilter {
	public boolean accept(File f) {
		return FileType.isCompressedFile(f.getName()) || f.isDirectory();
	}

	public String getDescription() {
		return "This file filter filters all Archive file";
	}
}