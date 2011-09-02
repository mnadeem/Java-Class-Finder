package com.nadeem.app.finder.ui;

import java.net.URL;

import javax.swing.ImageIcon;

public class Resource {
	
	 private static final String ICON_PATH 		= "/com/nadeem/app/finder/ui/icon/";

	 public static final String ICON_SEARCH 	= "search.png";
	 public static final String ICON_ABORT 		= "abort.png";
	 public static final String ICON_SPLACH 	= "splash.png";
	 public static final String ICON_TITLE 		= "title.png";
	 
	 public static ImageIcon getIcon(String iconName) {
		
		 URL resourceUrl = Resource.class.getResource(ICON_PATH + iconName);
		 if (resourceUrl != null) {
			return new ImageIcon(resourceUrl);
		}
		 return null;		 
	 }

	 public static ImageIcon getSearchIcon() {
		 return getIcon(ICON_SEARCH);
	 }
	 
	 public static ImageIcon getAbortIcon() {
		 return getIcon(ICON_ABORT);
	 }

	 public static ImageIcon getSplashIcon() {
		 return getIcon(ICON_SPLACH);
	 }

	 public static ImageIcon getTitleIcon() {
		 return getIcon(ICON_TITLE);
	 }
}
