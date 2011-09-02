package com.nadeem.app.finder.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JWindow;

public class SplashScreen extends JWindow {

	private static final int SPLASH_SCREEN_TIME_OUT = 2000;
	private static final long serialVersionUID 		= 1L;

	public SplashScreen() {

		ImageIcon splashImage = Resource.getSplashIcon();
		getContentPane().add(new JLabel(splashImage));
		setSize(splashImage.getIconWidth(), splashImage.getIconHeight());

		UiUtil.setDefaultLocation(this);
	}

	public void splash() {
		setVisible(true);
		repaint();
		 Timer cleanup = new Timer(SPLASH_SCREEN_TIME_OUT, new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				onSplashScreenTimeOut();
			}

		});
	    cleanup.setRepeats(false);
	    cleanup.start();
	}

	protected void onSplashScreenTimeOut() {

	}
}
