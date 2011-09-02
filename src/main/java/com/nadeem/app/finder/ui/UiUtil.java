package com.nadeem.app.finder.ui;

import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;

import javax.swing.JFrame;
import javax.swing.UIManager;

public class UiUtil {

	public static void setDefaultLocation(Window window) {
		window.setLocation(getBestLocation(window));
	}
	
	public static void setDefaultSize(Window window) {
		Toolkit kit 			= Toolkit.getDefaultToolkit();
	    Dimension screenSize 	= kit.getScreenSize();
	    int screenHeight 		= screenSize.height;
	    int screenWidth 		= screenSize.width;

	    window.setSize(screenWidth / 2, screenHeight / 2);
	}

	public static void setDefaultLookAndFeel() {

		JFrame.setDefaultLookAndFeelDecorated(true);
		Toolkit.getDefaultToolkit().setDynamicLayout(true);
		System.setProperty("sun.awt.noerasebackground", "true");
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
			System.err.println("Failed to set LookAndFeel" + e);
		}
	}

	public static Point getBestLocation(Window window) {
		Rectangle deviceBounds 	= getDefaultDeviceBounds();
		Dimension deviceSize 	= deviceBounds.getSize();
		Point deviceLocation 	= deviceBounds.getLocation();

		Point bestLocation = new Point(deviceLocation.x + (deviceSize.width - window.getBounds().width) / 2, 
								deviceLocation.y + (deviceSize.height -  window.getBounds().height) / 2);
		return bestLocation;
	}
	
	private static Rectangle getDefaultDeviceBounds() {
		GraphicsDevice[] graphisDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
		return graphisDevice[0].getDefaultConfiguration().getBounds();		
	}
}
